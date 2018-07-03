package model.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import model.bean.Endereco;

public class EnderecoDao extends Dao {

    private final String INSERT = " insert into endereco "
            + " (LOGRADOURO, NUMERO, COMPLEMENTO, BAIRRO, CIDADE, CEP) values (?,?,?,?,?,?) ";
    private final String UPDATE = " update endereco "
            + " set LOGRADOURO = ?, "
            + " NUMERO = ?, "
            + " COMPLEMENTO = ?, "
            + " BAIRRO = ?, "
            + " CIDADE = ?, "
            + " CEP = ? "
            + " WHERE CD_ENDERECO = ? ";
    private final String DELETE = " delete from endereco "
            + " where CD_ENDERECO = ? ";
    private final String SELECT = " select * from endereco ";
    private final String SELECT_BY_ID = " select * from endereco "
            + " where CD_ENDERECO = ? ";

    public long insertAddress(String logradouro, int numero, String complemento, String bairro, String cidade, String cep) throws DaoException {
        long autoRet = -1;
        Connection conn = null;
        PreparedStatement stm = null;
        ResultSet rs = null;
        try {
            conn = getConnection();
            stm = conn.prepareStatement(INSERT, Statement.RETURN_GENERATED_KEYS);
            stm.setString(1, logradouro);
            stm.setInt(2, numero);
            stm.setString(3, complemento);
            stm.setString(4, bairro);
            stm.setString(5, cidade);
            stm.setString(6, cep);
            stm.execute();
            rs = stm.getGeneratedKeys();
            if (rs.next()) {
                autoRet = rs.getLong(1);
            }
            close(conn, stm, rs);
        } catch (SQLException e) {
            throw new DaoException("EnderecoDao.insertAddress() : " + e.getClass().getName() + "," + e.getMessage());
        }
        return autoRet;
    }

    public Endereco selectAddress(long cdEndereco) throws DaoException {
        Endereco e = null;

        ResultSet rs = null;
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = getConnection();
            ps = conn.prepareStatement(SELECT_BY_ID);
            ps.setLong(1, cdEndereco);

            rs = ps.executeQuery();
            if (rs.next()) {
                e = getAddressFromRs(rs);
            }
            close(conn, ps, rs);
        } catch (SQLException ex) {
            throw new DaoException("EnderecoDao.selectAddress() : " + ex.getClass().getName() + "," + ex.getMessage());
        }
        return e;
    }

    public List<Endereco> selectAllAdresses() throws DaoException {
        List<Endereco> e = new ArrayList();

        ResultSet rs = null;
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = getConnection();
            ps = conn.prepareStatement(SELECT);

            rs = ps.executeQuery();
            if (rs.next()) {
                e.add(getAddressFromRs(rs));
            }
            close(conn, ps, rs);
        } catch (SQLException ex) {
            throw new DaoException("EnderecoDao.selectAddress() : " + ex.getClass().getName() + "," + ex.getMessage());
        }
        return e;
    }

    public long updateAddress(Endereco e) throws DaoException {
        long autoRet = -1;
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = getConnection();
            ps = conn.prepareStatement(UPDATE);
            ps.setString(1, e.getLogradouro());
            ps.setInt(2, e.getNumero());
            ps.setString(3, e.getComplemento());
            ps.setString(4, e.getBairro());
            ps.setString(5, e.getCidade());
            ps.setString(6, e.getCep());
            ps.setLong(7, e.getCdEndereco());
            ps.execute();
            autoRet = e.getCdEndereco();
            close(conn, ps);
        } catch (SQLException ex) {
            throw new DaoException("EnderecoDao.updateAddress() : " + ex.getClass().getName() + "," + ex.getMessage());
        }
        return autoRet;
    }

    public int deleteAddress(long cdEndereco) throws DaoException {
        int autoRet = -1;
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = getConnection();
            ps = conn.prepareStatement(DELETE);
            ps.setLong(1, cdEndereco);
            ps.execute();
            autoRet = 1;
            close(conn, ps);
        } catch (SQLException ex) {
            throw new DaoException("EnderecoDao.deleteAddress() : " + ex.getClass().getName() + "," + ex.getMessage());
        }
        return autoRet;
    }

    private Endereco getAddressFromRs(ResultSet rs) throws DaoException {
        long cdEndereco = -1;
        String logradouro = null;
        int numero = -1;
        String complemento = null;
        String bairro = null;
        String cidade = null;
        String cep = null;
        try {
            cdEndereco = rs.getLong("CD_ENDERECO");
            logradouro = rs.getString("LOGRADOURO");
            numero = rs.getInt("NUMERO");
            complemento = rs.getString("COMPLEMENTO");
            bairro = rs.getString("BAIRRO");
            cidade = rs.getString("CIDADE");
            cep = rs.getString("CEP");
        } catch (SQLException e) {
            throw new DaoException("EnderecoDao.getAddressFromRs() : " + e.getClass().getName() + "," + e.getMessage());
        } catch (Exception e) {
            throw new DaoException("EnderecoDao.getAddressFromRs() : " + e.getClass().getName() + "," + e.getMessage());
        }
        return Endereco.newInstance(cdEndereco, logradouro, numero, complemento, bairro, cidade, cep);

    }
}
