package model.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import model.bean.Telefone;

/**
 *
 * @author filip
 */
public class TelefoneDao extends Dao {

    private static final String INSERT
            = " insert into telefone (TELEFONE, CD_PESSOA) "
            + " values (?,?) ";
    private static final String SELECT
            = " select * from telefone "
            + " where CD_PESSOA = ? ";
    private static final String UPDATE
            = " update telefone "
            + " set TELEFONE = ? "
            + " where CD_TELEFONE = ? ";
    private static final String DELETE
            = " delete from telefone "
            + " where CD_TELEFONE = ? ";
    private static final String DELETE_BY_PERSON
            = " delete from telefone "
            + " where CD_PESSOA = ? ";

    public long insertPhone(String telefone, long cdPessoa) throws DaoException {
        long autoRet = -1;

        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = getConnection();
            ps = conn.prepareStatement(INSERT, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, telefone);
            ps.setLong(2, cdPessoa);
            ps.execute();
            rs = ps.getGeneratedKeys();
            if (rs.next()) {
                autoRet = rs.getInt(1);
            }
            close(conn, ps, rs);
        } catch (SQLException e) {
            throw new DaoException("TelefoneDao.insertPhone() : " + e.getClass().getName() + "," + e.getMessage());
        }
        return autoRet;
    }

    public List<Telefone> selectPhone(long cdPessoa) throws DaoException {
        List<Telefone> aList = new ArrayList();

        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = getConnection();
            ps = conn.prepareStatement(SELECT);
            ps.setLong(1, cdPessoa);
            rs = ps.executeQuery();
            while (rs.next()) {
                Telefone t = getPhoneFromRs(rs);
                aList.add(t);
            }
            close(conn, ps, rs);
        } catch (SQLException e) {
            throw new DaoException("TelefoneDao.selectPhone() : " + e.getClass().getName() + "," + e.getMessage());
        }
        return aList;
    }

    public long updatePhone(String telefone, long cdTelefone) throws DaoException {
        long autoRet = -1;

        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = getConnection();
            ps = conn.prepareStatement(UPDATE);
            ps.setString(1, telefone);
            ps.setLong(2, cdTelefone);
            ps.execute();
            autoRet = 1;
            close(conn, ps);
        } catch (SQLException e) {
            throw new DaoException("TelefoneDao.updatePhone() : " + e.getClass().getName() + "," + e.getMessage());
        }
        return autoRet;
    }

    public long deletePhone(long cdTelefone) throws DaoException {
        long autoRet = -1;

        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = getConnection();
            ps = conn.prepareStatement(DELETE);
            ps.setLong(1, cdTelefone);
            ps.execute();
            autoRet = 1;
            close(conn, ps);
        } catch (SQLException e) {
            throw new DaoException("TelefoneDao.deletePhone() : " + e.getClass().getName() + "," + e.getMessage());
        }
        return autoRet;
    }

    public long deletePhoneByPerson(long cdPessoa) throws DaoException {
        long autoRet = -1;

        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = getConnection();
            ps = conn.prepareStatement(DELETE_BY_PERSON);
            ps.setLong(1, cdPessoa);
            ps.execute();
            autoRet = 1;
            close(conn, ps);
        } catch (SQLException e) {
            throw new DaoException("TelefoneDao.deletePhone() : " + e.getClass().getName() + "," + e.getMessage());
        }
        return autoRet;
    }

    private Telefone getPhoneFromRs(ResultSet rs) throws DaoException {
        long cdTelefone = -1;
        String telefone = null;
        try {
            cdTelefone = rs.getLong("CD_TELEFONE");
            telefone = rs.getString("TELEFONE");
        } catch (SQLException e) {
            throw new DaoException("TelefoneDao.getPhoneFromRs() : " + e.getClass().getName() + "," + e.getMessage());
        }
        return Telefone.newInstance(cdTelefone, telefone);
    }
}
