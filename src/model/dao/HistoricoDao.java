package model.dao;

import java.sql.Connection;
import java.util.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import model.bean.Usuario;
import model.bean.Historico;

public class HistoricoDao extends Dao {

    private static final String INSERT
            = " insert into historico (ACAO, DATA, CD_USUARIO) "
            + " values (?,?,?) ";
    private static final String SELECT_ALL
            = " select h.*, u.* "
            + " from historico h, usuario u "
            + " where h.CD_USUARIO = u.CD_USUARIO "
            + " ORDER BY h.DATA DESC";
    private static final String SELECT_BY_USER
            = " select h.*, u.* "
            + " from historico h, usuario u "
            + " where u.CD_USUARIO = ? and "
            + " h.CD_USUARIO = u.CD_USUARIO "
            + " ORDER BY h.DATA DESC";

    public long insertHistoric(String acao, Usuario user) throws DaoException {
        long autoRet = -1;
        Connection conn = null;
        PreparedStatement stm = null;
        ResultSet rs = null;

        try {
            conn = getConnection();
            stm = conn.prepareStatement(INSERT, Statement.RETURN_GENERATED_KEYS);
            stm.setString(1, acao);
            stm.setDate(2, new java.sql.Date(new Date().getTime()));
            stm.setLong(3, user.getCdusuario());
            stm.execute();
            rs = stm.getGeneratedKeys();
            if (rs.next()) {
                autoRet = rs.getInt(1);
            }
            close(conn, stm, rs);
        } catch (SQLException e) {
            throw new DaoException("UsuarioDao.insertUser() : " + e.getClass().getName() + "," + e.getMessage());
        }
        return autoRet;
    }

    public List selectAllHistoric() throws DaoException {
        List<Historico> aList = new ArrayList();

        ResultSet rs = null;
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = getConnection();
            ps = conn.prepareStatement(SELECT_ALL);
            rs = ps.executeQuery();
            while (rs.next()) {
                Historico h = getHistoricFromRs(rs);
                aList.add(h);
            }
            close(conn, ps, rs);
        } catch (SQLException e) {
            throw new DaoException("UsuarioDao.selectAllUsers() : " + e.getClass().getName() + "," + e.getMessage(), e);
        }
        return aList;
    }

    public List selectHistoricByUser(long cdUsuario) throws DaoException {
        List<Historico> aList = new ArrayList();

        ResultSet rs = null;
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = getConnection();
            ps = conn.prepareStatement(SELECT_BY_USER);
            ps.setLong(1, cdUsuario);
            rs = ps.executeQuery();
            while (rs.next()) {
                Historico h = getHistoricFromRs(rs);
                aList.add(h);
            }
            close(conn, ps, rs);
        } catch (SQLException e) {
            throw new DaoException("UsuarioDao.selectAllUsers() : " + e.getClass().getName() + "," + e.getMessage(), e);
        }
        return aList;
    }

    private Historico getHistoricFromRs(ResultSet rs) throws DaoException {
        Historico h = null;
        try {
            Usuario u = getUsuarioFromRs(rs);
            h = Historico.newInstance(rs.getLong("CD_HISTORICO"), rs.getString("ACAO"), u, rs.getDate("DATA"));
        } catch (Exception e) {
            throw new DaoException("HistoricoDao.getHistoricFromRs() : " + e.getClass().getName() + "," + e.getMessage());
        }
        return h;
    }

    private Usuario getUsuarioFromRs(ResultSet rs) throws DaoException {
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
