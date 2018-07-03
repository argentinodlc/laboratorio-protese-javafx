/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import model.bean.Produto;

/**
 *
 * @author filip
 */
public class ProdutoDao extends Dao {
    private final String INSERT = " insert into produto (NOME, VALOR) values (?, ?) ";
    private final String SELECT = " select * from produto ";
    private final String SELECT_BY_CD = " select * from produto where cd_produto = ? ";
    private final String SELECT_BY_NAME = " select * from produto where NOME like ? ";
    private final String UPDATE = " update produto set nome = ?, valor = ? where cd_produto = ?";
    private final String DELETE = " delete from produto where CD_PRODUTO = ? ";
    
    public long insertProduto(String nome, double valor) throws DaoException {
        long autoRet = -1;
        Connection conn = null;
        PreparedStatement stm = null;
        ResultSet rs = null;

        try {
            conn = getConnection();
            stm = conn.prepareStatement(INSERT, Statement.RETURN_GENERATED_KEYS);
            stm.setString(1, nome);
            stm.setDouble(2, valor);
            stm.execute();
            rs = stm.getGeneratedKeys();
            if (rs.next()) {
                autoRet = rs.getInt(1);
            }
            close(conn, stm, rs);
        } catch (SQLException e) {
            throw new DaoException("ProdutoDao.insertProduto() : " + e.getClass().getName() + "," + e.getMessage());
        }
        return autoRet;
    }

    public List selectProduto() throws DaoException {
        List<Produto> aList = new ArrayList();

        ResultSet rs = null;
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = getConnection();
            ps = conn.prepareStatement(SELECT);
            rs = ps.executeQuery();
            while (rs.next()) {
                Produto p = getProdutoFromRs(rs);
                aList.add(p);
            }
            close(conn, ps, rs);
        } catch (SQLException e) {
            throw new DaoException("ProdutoDao.selectProduto() : " + e.getClass().getName() + "," + e.getMessage(), e);
        }
        return aList;
    }
    
      public Produto selectProdutoByCd(long cdProduto) throws DaoException {
        Produto p = null;

        ResultSet rs = null;
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = getConnection();
            ps = conn.prepareStatement(SELECT_BY_CD);
            ps.setLong(1, cdProduto);
            rs = ps.executeQuery();
            while (rs.next()) {
                p = getProdutoFromRs(rs);
            }
            close(conn, ps, rs);
        } catch (SQLException e) {
            throw new DaoException("ProdutoDao.selectProdutoByCd() : " + e.getClass().getName() + "," + e.getMessage(), e);
        }
        return p;
    }
      
      public List<Produto> selectProdutoByName(String nome) throws DaoException {
        Produto p = null;
        List<Produto> aList = new ArrayList();
        ResultSet rs = null;
        Connection conn = null;
        PreparedStatement ps = null;
        nome = "%"+nome+"%";
        try {
            conn = getConnection();
            ps = conn.prepareStatement(SELECT_BY_NAME);
            ps.setString(1, nome);
            rs = ps.executeQuery();
            while (rs.next()) {
                p = getProdutoFromRs(rs);
                aList.add(p);
            }
            close(conn, ps, rs);
        } catch (SQLException e) {
            throw new DaoException("ProdutoDao.selectProdutoByName() : " + e.getClass().getName() + "," + e.getMessage(), e);
        }
        return aList;
    }
    
    public long updateProduto(Produto p) throws DaoException {
        long autoRet = -1;
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = getConnection();
            ps = conn.prepareStatement(UPDATE);
            ps.setString(1, p.getNome());
            ps.setDouble(2, p.getValor());
            ps.setLong(3, p.getCdProduto());
            ps.execute();
            autoRet = p.getCdProduto();
            close(conn, ps);
        } catch (SQLException ex) {
            throw new DaoException("ProdutoDao.updateProduto() : " + ex.getClass().getName() + "," + ex.getMessage());
        }
        return autoRet;
    }

    public int deleteProduto(long cdProduto) throws DaoException {
        int autoRet = -1;
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            Produto p = selectProdutoByCd(cdProduto);
            conn = getConnection();
            ps = conn.prepareStatement(DELETE);
            ps.setLong(1, cdProduto);
            ps.execute();
            autoRet = 1;
            close(conn, ps);
        } catch (SQLException ex) {
            throw new DaoException("ProdutoDao.deleteProduto() : " + ex.getClass().getName() + "," + ex.getMessage());
        }
        return autoRet;
    }

    private Produto getProdutoFromRs(ResultSet rs) throws DaoException {
        long cdProduto;
        String nome;
        double valor;
        try {
            cdProduto = rs.getLong("CD_PRODUTO");
            nome = rs.getString("NOME");
            valor = rs.getDouble("VALOR");
        } catch (SQLException e) {
            throw new DaoException("ProdutoDao.getProdutoFromRs() : " + e.getClass().getName() + "," + e.getMessage(), e);
        }
        return Produto.newInstance(cdProduto, nome, valor);
    }
}
