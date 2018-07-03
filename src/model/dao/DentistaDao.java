package model.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import model.bean.Dentista;
import model.bean.Endereco;
import model.bean.Pessoa;
import model.bean.Telefone;

/**
 *
 * @author filip
 */
public class DentistaDao extends Dao {

    private static final String INSERT
            = " insert into dentista (CRO, CD_PESSOA)"
            + " values (?, ?) ";
    private static final String SELECT
            = " select d.*, p.*, e.* from "
            + " dentista d, pessoa p, endereco e "
            + " where d.CD_PESSOA = p.CD_PESSOA and p.CD_ENDERECO = e.CD_ENDERECO ";
    private static final String SELECT_BY_CD
            = " select d.*, p.*, e.* from "
            + " dentista d, pessoa p, endereco e "
            + " where d.CD_DENTISTA = ? "
            + " and d.CD_PESSOA = p.CD_PESSOA and p.CD_ENDERECO = e.CD_ENDERECO ";
    private static final String UPDATE
            = " update dentista "
            + " set cro = ? "
            + " where CD_DENTISTA = ? ";
    private static final String DELETE
            = " delete from dentista "
            + " where CD_DENTISTA = ? ";

    public long insertDentist(String nome, long cdEndereco, List<Telefone> telefones, String cro) throws DaoException {
        long autoRet = -1;
        Connection conn = null;
        PreparedStatement stm = null;
        ResultSet rs = null;

        try {
            conn = getConnection();
            stm = conn.prepareStatement(INSERT, Statement.RETURN_GENERATED_KEYS);

            long cdPessoa = (new PessoaDao()).insertPerson(nome, cdEndereco, telefones);
            stm.setString(1, cro);
            stm.setLong(2, cdPessoa);
            stm.execute();
            rs = stm.getGeneratedKeys();
            if (rs.next()) {
                autoRet = rs.getLong(1);
            }
            close(conn, stm, rs);
        } catch (SQLException e) {
            throw new DaoException("DentistaDao.insertDentist() : " + e.getClass().getName() + "," + e.getMessage());
        }
        return autoRet;
    }

    public List<Dentista> selectAllDentist() throws DaoException {
        List<Dentista> aList = new ArrayList();

        ResultSet rs = null;
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = getConnection();
            ps = conn.prepareStatement(SELECT);
            rs = ps.executeQuery();
            while (rs.next()) {
                Dentista d = getDentistFromRs(rs);
                aList.add(d);
            }
            close(conn, ps, rs);
        } catch (SQLException e) {
            throw new DaoException("DentistaDao.selectAllDentist() : " + e.getClass().getName() + "," + e.getMessage(), e);
        }
        return aList;
    }

    public Dentista selectDentistByCd(long cdDentista) throws DaoException {
        Dentista d = null;

        ResultSet rs = null;
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = getConnection();
            ps = conn.prepareStatement(SELECT_BY_CD);
            ps.setLong(1, cdDentista);
            rs = ps.executeQuery();
            while (rs.next()) {
                d = getDentistFromRs(rs);
            }
            close(conn, ps, rs);
        } catch (SQLException e) {
            throw new DaoException("DentistaDao.selectDentistByCd() : " + e.getClass().getName() + "," + e.getMessage(), e);
        }
        return d;
    }

    public long updateDentist(Dentista d) throws DaoException {
        long autoRet = -1;
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = getConnection();
            ps = conn.prepareStatement(UPDATE);
            ps.setString(1, d.getCro());
            ps.setLong(2, d.getCdDentista());
            ps.execute();
            autoRet = d.getCdPessoa();

            Pessoa p = (new PessoaDao()).selectPersonByCd(autoRet);
            p.setEndereco(d.getEndereco());
            p.setNome(d.getNome());
            p.setTelefones(d.getTelefones());
            (new PessoaDao()).updatePerson(p);
            close(conn, ps);
        } catch (SQLException ex) {
            throw new DaoException("DentistaDao.updateDentist() : " + ex.getClass().getName() + "," + ex.getMessage());
        }
        return autoRet;
    }

    public int deleteDentist(long cdDentista) throws DaoException {
        int autoRet = -1;
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            Dentista d = selectDentistByCd(cdDentista);
            conn = getConnection();
            ps = conn.prepareStatement(DELETE);
            ps.setLong(1, cdDentista);
            ps.execute();
            autoRet = 1;
            (new PessoaDao()).deletePerson(d.getCdPessoa());
            close(conn, ps);
        } catch (SQLException ex) {
            throw new DaoException("DentistaDao.deleteDentist() : " + ex.getClass().getName() + "," + ex.getMessage());
        }
        return autoRet;
    }

    private Dentista getDentistFromRs(ResultSet rs) throws DaoException {
        long cdDentista = -1;
        String cro = null;
        Pessoa p = null;
        try {
            cdDentista = rs.getLong("CD_DENTISTA");
            cro = rs.getString("CRO");
            p = getPersonFromRs(rs);
        } catch (SQLException e) {
            throw new DaoException("DentistaDao.getDentistFromRs() : " + e.getClass().getName() + "," + e.getMessage(), e);
        }
        return Dentista.newInstance(p.getCdPessoa(), p.getNome(), p.getEndereco(), p.getTelefones(), cdDentista, cro);
    }

    private Pessoa getPersonFromRs(ResultSet rs) throws DaoException {
        long cdPessoa;
        String nome;
        Endereco endereco;
        List<Telefone> telefones = null;
        try {
            cdPessoa = rs.getLong("CD_PESSOA");
            nome = rs.getString("NOME");
            endereco = getAddressFromRs(rs);
            telefones = (new TelefoneDao()).selectPhone(cdPessoa);
        } catch (SQLException e) {
            throw new DaoException("DentistaDao.getPersonFromRs() : " + e.getClass().getName() + "," + e.getMessage(), e);
        }
        return Pessoa.newInstance(cdPessoa, nome, endereco, telefones);
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
            throw new DaoException("DentistaDao.getAddressFromRs() : " + e.getClass().getName() + "," + e.getMessage());
        } catch (Exception e) {
            throw new DaoException("DentistaDao.getAddressFromRs() : " + e.getClass().getName() + "," + e.getMessage());
        }
        return Endereco.newInstance(cdEndereco, logradouro, numero, complemento, bairro, cidade, cep);

    }
}
