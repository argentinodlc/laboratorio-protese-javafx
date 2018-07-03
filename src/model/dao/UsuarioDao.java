/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model.dao;

import static com.sun.javafx.scene.control.skin.Utils.getResource;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import javax.imageio.ImageIO;
import model.bean.Usuario;

/**
 *
 * @author filip
 */
public class UsuarioDao extends Dao {

    private final String INSERT
            = " insert into usuario (NOME, LOGIN, SENHA, PERMISSAO, IMAGEM) "
            + " values (?,?,?, ?, ?) ";
    private final String SELECT_USER
            = " select * from usuario where LOGIN = ? and SENHA = ? ";
    private final String SELECT_ALL
            = " select * from usuario ";
    private final String DELETE
            = " delete from usuario where CD_USUARIO = ? ";
    private final String UPDATE
            = " update usuario set NOME = ? "
            + " , LOGIN = ? "
            + " , SENHA = ? "
            + " , PERMISSAO = ? "
            + " , IMAGEM = ? "
            + " where CD_USUARIO = ? ";

    public long insertUser(String nome, String login, String senha, int permissao, byte[] imagem) throws DaoException {
        long autoRet = -1;
        Connection conn = null;
        PreparedStatement stm = null;
        ResultSet rs = null;
        try {
            conn = getConnection();
            stm = conn.prepareStatement(INSERT, Statement.RETURN_GENERATED_KEYS);
            stm.setString(1, nome);
            stm.setString(2, login);
            stm.setString(3, senha);
            stm.setInt(4, permissao);
            stm.setBytes(5, imagem);
            stm.execute();
            rs = stm.getGeneratedKeys();
            if (rs.next()) {
                autoRet = rs.getLong(1);
            }
            close(conn, stm, rs);
        } catch (SQLException e) {
            throw new DaoException("UsuarioDao.insertUser() : " + e.getClass().getName() + "," + e.getMessage());
        }
        return autoRet;
    }

    public Usuario selectUser(String login, String senha) throws DaoException {
        Usuario u = null;

        ResultSet rs = null;
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = getConnection();
            ps = conn.prepareStatement(SELECT_USER);
            ps.setString(1, login);
            ps.setString(2, senha);

            rs = ps.executeQuery();
            if (rs.next()) {
                u = getUserFromRs(rs);
            }
            close(conn, ps, rs);
        } catch (SQLException e) {
            throw new DaoException("UsuarioDao.selectUser() : " + e.getClass().getName() + "," + e.getMessage(), e);
        }
        return u;
    }

    public List<Usuario> selectAllUsers() throws DaoException {
        List<Usuario> aList = new ArrayList();

        ResultSet rs = null;
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = getConnection();
            ps = conn.prepareStatement(SELECT_ALL);

            rs = ps.executeQuery();
            while (rs.next()) {
                Usuario u = getUserFromRs(rs);
                aList.add(u);
            }
            close(conn, ps, rs);
        } catch (SQLException e) {
            throw new DaoException("UsuarioDao.selectAllUsers() : " + e.getClass().getName() + ","
                    + e.getMessage(), e);
        }
        return aList;
    }

    public void deleteUser(int cd) throws DaoException {
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = getConnection();
            ps = conn.prepareStatement(DELETE);
            ps.setInt(1, cd);
            ps.execute();
            close(conn, ps);
        } catch (SQLException e) {
            throw new DaoException("UsuarioDao.deleteUser() : " + e.getClass().getName() + ","
                    + e.getMessage(), e);
        }
    }

    public void updateUser(Usuario u) throws DaoException {
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = getConnection();
            ps = conn.prepareStatement(UPDATE);
            ps.setString(1, u.getNome());
            ps.setString(2, u.getLogin());
            ps.setString(3, u.getSenha());
            ps.setInt(4, u.getPermissao());
            ps.setBytes(5, u.getImagem());
            ps.setLong(6, u.getCdusuario());
            ps.execute();
            close(conn, ps);
        } catch (SQLException e) {
            throw new DaoException("UsuarioDao.updateUser() : " + e.getClass().getName() + ","
                    + e.getMessage(), e);
        }
    }

    private Usuario getUserFromRs(ResultSet rs) throws DaoException {
        long cd_usuario;
        String nome;
        String login;
        String senha;
        int permissao;
        byte[] imagem;

        try {
            cd_usuario = rs.getLong("CD_USUARIO");
            nome = rs.getString("NOME");
            login = rs.getString("LOGIN");
            senha = rs.getString("SENHA");
            permissao = rs.getInt("PERMISSAO");
            imagem = rs.getBytes("IMAGEM");
        } catch (SQLException e) {
            throw new DaoException("Usuario.getUserFromRs() : " + e.getClass().getName() + "," + e.getMessage(), e);
        }
        return Usuario.newInstance(cd_usuario, nome, login, senha, permissao, imagem);
    }
}
