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
import model.bean.Endereco;
import model.bean.Pessoa;
import model.bean.Telefone;

/**
 *
 * @author filip
 */
public class PessoaDao extends Dao {

    private static final String INSERT
            = " insert into pessoa (NOME, CD_ENDERECO) "
            + " values (?,?) ";
    private static final String SELECT_ALL
            = " select p.*, e.*, t.TELEFONE "
            + " from pessoa p, endereco e, telefone t"
            + " where p.CD_ENDERECO = e.CD_ENDERECO and "
            + " p.CD_PESSOA = t.CD_PESSOA ";
    private static final String SELECT_BY_CD
            = " select p.*, e.* "
            + " from pessoa p, endereco e "
            + " where p.CD_PESSOA = ? "
            + " and p.CD_ENDERECO = e.CD_ENDERECO ";
    private static final String UPDATE
            = " update pessoa "
            + " set NOME = ?, "
            + " CD_ENDERECO = ? "
            + " where CD_PESSOA = ? ";
    private static final String DELETE
            = " delete from pessoa where CD_PESSOA = ?";

    public long insertPerson(String nome, long cdEndereco, List<Telefone> telefones) throws DaoException {
        long autoRet = -1;
        Connection conn = null;
        PreparedStatement stm = null;
        ResultSet rs = null;

        try {
            conn = getConnection();
            stm = conn.prepareStatement(INSERT, Statement.RETURN_GENERATED_KEYS);
            stm.setString(1, nome);
            stm.setLong(2, cdEndereco);
            stm.execute();
            rs = stm.getGeneratedKeys();
            if (rs.next()) {
                autoRet = rs.getInt(1);
            }
            for (Telefone t : telefones) {
                (new TelefoneDao()).insertPhone(t.getTelefone(), autoRet);
            }
            close(conn, stm, rs);
        } catch (SQLException e) {
            throw new DaoException("PessoaDao.insertPerson() : " + e.getClass().getName() + "," + e.getMessage());
        }
        return autoRet;
    }

    public List<Pessoa> selectAllPerson() throws DaoException {
        List<Pessoa> aList = new ArrayList();

        ResultSet rs = null;
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = getConnection();
            ps = conn.prepareStatement(SELECT_ALL);
            rs = ps.executeQuery();
            while (rs.next()) {
                Pessoa p = getPersonFromRs(rs);
                aList.add(p);
            }
            close(conn, ps, rs);
        } catch (SQLException e) {
            throw new DaoException("PessoaDao.selectAllPerson() : " + e.getClass().getName() + "," + e.getMessage(), e);
        }
        return aList;
    }

    public Pessoa selectPersonByCd(long cdPessoa) throws DaoException {
        Pessoa p = null;

        ResultSet rs = null;
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = getConnection();
            ps = conn.prepareStatement(SELECT_BY_CD);
            ps.setLong(1, cdPessoa);
            rs = ps.executeQuery();
            while (rs.next()) {
                p = getPersonFromRs(rs);
            }
            close(conn, ps, rs);
        } catch (SQLException e) {
            throw new DaoException("PessoaDao.selectPersonByCd() : " + e.getClass().getName() + "," + e.getMessage(), e);
        }
        return p;
    }

    public long updatePerson(Pessoa p) throws DaoException {
        long autoRet = -1;
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = getConnection();
            ps = conn.prepareStatement(UPDATE);
            ps.setString(1, p.getNome());
            ps.setLong(2, (new EnderecoDao()).updateAddress(p.getEndereco()));
            ps.setLong(3, p.getCdPessoa());
            ps.execute();
            autoRet = p.getCdPessoa();
            (new TelefoneDao()).deletePhoneByPerson(p.getCdPessoa());
            for (Telefone t : p.getTelefones()) {
                (new TelefoneDao()).insertPhone(t.getTelefone(), p.getCdPessoa());
            }
            close(conn, ps);
        } catch (SQLException ex) {
            throw new DaoException("PessoaDao.updatePerson() : " + ex.getClass().getName() + "," + ex.getMessage());
        }
        return autoRet;
    }

    public int deletePerson(long cdPessoa) throws DaoException {
        int autoRet = -1;
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            Pessoa p = selectPersonByCd(cdPessoa);
            for (Telefone t : p.getTelefones()) {
                (new TelefoneDao()).deletePhone(t.getCdTelefone());
            }
            conn = getConnection();
            ps = conn.prepareStatement(DELETE);
            ps.setLong(1, cdPessoa);
            ps.execute();
            autoRet = 1;
            (new EnderecoDao()).deleteAddress(p.getEndereco().getCdEndereco());
            close(conn, ps);
        } catch (SQLException ex) {
            throw new DaoException("PessoaDao.deletePerson() : " + ex.getClass().getName() + "," + ex.getMessage());
        }
        return autoRet;
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
            throw new DaoException("PessoaDao.getPersonFromRs() : " + e.getClass().getName() + "," + e.getMessage(), e);
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
            throw new DaoException("PessoaDao.getAddressFromRs() : " + e.getClass().getName() + "," + e.getMessage());
        } catch (Exception e) {
            throw new DaoException("PessoaDao.getAddressFromRs() : " + e.getClass().getName() + "," + e.getMessage());
        }
        return Endereco.newInstance(cdEndereco, logradouro, numero, complemento, bairro, cidade, cep);

    }
}
