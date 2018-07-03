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
import model.bean.Clinica;
import model.bean.Dentista;
import model.bean.Endereco;
import model.bean.Pessoa;
import model.bean.Telefone;

/**
 *
 * @author filip
 */
public class ClinicaDao extends Dao {

    private static final String INSERT
            = " insert into clinica (CNPJ, CD_PESSOA)"
            + " values (?, ?) ";
    private static final String INSERT_DENTIST
            = " insert into clinica_dentista (CD_CLINICA, CD_DENTISTA) "
            + " values (?, ?) ";
    private static final String SELECT
            = " select c.*, p.*, e.* from "
            + " clinica c, pessoa p, endereco e "
            + " where c.CD_PESSOA = p.CD_PESSOA and p.CD_ENDERECO = e.CD_ENDERECO ";
    private static final String SELECT_BY_CD
            = " select c.*, p.*, e.* from "
            + " clinica c, pessoa p, endereco e "
            + " where c.CD_CLINICA = ? "
            + " and c.CD_PESSOA = p.CD_PESSOA and p.CD_ENDERECO = e.CD_ENDERECO ";
    private static final String SELECT_BY_NOME
            = " select c.*, p.*, e.* from "
            + " clinica c, pessoa p, endereco e "
            + " where p.NOME LIKE ? "
            + " and c.CD_PESSOA = p.CD_PESSOA and p.CD_ENDERECO = e.CD_ENDERECO ";
    private static final String SELECT_DENTISTS
            = " select d.* from "
            + " dentista d inner join clinica_dentista cd on d.CD_DENTISTA = cd.CD_DENTISTA "
            + " inner join clinica c on c.CD_CLINICA = cd.CD_CLINICA " 
            + " where c.CD_CLINICA = ? ";
    private static final String UPDATE
            = " update clinica "
            + " set CNPJ = ? "
            + " where CD_CLINICA = ? ";
    private static final String DELETE
            = " delete from clinica "
            + " where CD_CLINICA = ? ";
    private static final String DELETE_DENTISTS
            = " delete from clinica_dentista"
            + " where CD_CLINICA = ? ";

    public long insertClinica(String nome, long cdEndereco, List<Telefone> telefones, String CNPJ, List<Dentista> aList) throws DaoException {
        long autoRet = -1;
        Connection conn = null;
        PreparedStatement stm = null;
        ResultSet rs = null;

        try {
            conn = getConnection();
            stm = conn.prepareStatement(INSERT, Statement.RETURN_GENERATED_KEYS);

            long cdPessoa = (new PessoaDao()).insertPerson(nome, cdEndereco, telefones);
            stm.setString(1, CNPJ);
            stm.setLong(2, cdPessoa);
            stm.execute();
            rs = stm.getGeneratedKeys();
            if (rs.next()) {
                autoRet = rs.getLong(1);
                if (aList.size() > 0) {
                    stm = conn.prepareStatement(INSERT_DENTIST);
                    for (Dentista d : aList) {
                        stm.setLong(1, autoRet);
                        stm.setLong(2, d.getCdDentista());
                        stm.execute();
                    }

                }
            }
            close(conn, stm, rs);
        } catch (SQLException e) {
            throw new DaoException("ClinicaDao.insertClinica() : " + e.getClass().getName() + "," + e.getMessage());
        }
        return autoRet;
    }

    public List<Clinica> selectAllClinica() throws DaoException {
        List<Clinica> aList = new ArrayList();

        ResultSet rs = null;
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = getConnection();
            ps = conn.prepareStatement(SELECT);
            rs = ps.executeQuery();
            while (rs.next()) {
                Clinica c = getClinicaFromRs(rs);
                aList.add(c);
            }
            close(conn, ps, rs);
        } catch (SQLException e) {
            throw new DaoException("ClinicaDao.selectAllClinica() : " + e.getClass().getName() + "," + e.getMessage(), e);
        }
        return aList;
    }

    public List<Clinica> selectClinicaByNome(String nome) throws DaoException {
        Clinica c = null;
        List<Clinica> aList = new ArrayList();
        ResultSet rs = null;
        Connection conn = null;
        PreparedStatement ps = null;
        nome = "%" + nome + "%";
        try {
            conn = getConnection();
            ps = conn.prepareStatement(SELECT_BY_NOME);
            ps.setString(1, nome);
            rs = ps.executeQuery();
            while (rs.next()) {
                c = getClinicaFromRs(rs);
                aList.add(c);
            }
            close(conn, ps, rs);
        } catch (SQLException e) {
            throw new DaoException("ClinicaDao.selectClinicaByNome() : " + e.getClass().getName() + "," + e.getMessage(), e);
        }
        return aList;
    }

    public Clinica selectClinicaByCd(long cdDentista) throws DaoException {
        Clinica c = null;

        ResultSet rs = null;
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = getConnection();
            ps = conn.prepareStatement(SELECT_BY_CD);
            ps.setLong(1, cdDentista);
            rs = ps.executeQuery();
            while (rs.next()) {
                c = getClinicaFromRs(rs);
            }
            close(conn, ps, rs);
        } catch (SQLException e) {
            throw new DaoException("ClinicaDao.selectClinicaByCd() : " + e.getClass().getName() + "," + e.getMessage(), e);
        }
        return c;
    }
    
    public List<Dentista> selectDentists(long cd) throws DaoException {
        List<Dentista> aList = new ArrayList();

        ResultSet rs = null;
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = getConnection();
            ps = conn.prepareStatement(SELECT_DENTISTS);
            ps.setLong(1,cd);
            rs = ps.executeQuery();
            while (rs.next()) {
                Dentista d = getDentistFromRs(rs);
                aList.add(d);
            }
            close(conn, ps, rs);
        } catch (SQLException e) {
            throw new DaoException("ClinicaDao.selectDentists() : " + e.getClass().getName() + "," + e.getMessage(), e);
        }
        return aList;
    }

    public long updateClinica(Clinica c, List<Dentista> aList) throws DaoException {
        long autoRet = -1;
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = getConnection();
            ps = conn.prepareStatement(UPDATE);
            ps.setString(1, c.getCNPJ());
            ps.setLong(2, c.getCdClinica());
            ps.execute();
            autoRet = c.getCdPessoa();
            ps = conn.prepareStatement(DELETE_DENTISTS);
            ps.setLong(1,c.getCdClinica());
            ps.execute();
            if (aList.size() > 0) {
                    ps = conn.prepareStatement(INSERT_DENTIST);
                    for (Dentista d : aList) {
                        ps.setLong(1, c.getCdClinica());
                        ps.setLong(2, d.getCdDentista());
                        ps.execute();
                    }

                }
            Pessoa p = (new PessoaDao()).selectPersonByCd(autoRet);
            p.setEndereco(c.getEndereco());
            p.setNome(c.getNome());
            p.setTelefones(c.getTelefones());
            (new PessoaDao()).updatePerson(p);
            close(conn, ps);
            autoRet = c.getCdClinica();
        } catch (SQLException ex) {
            throw new DaoException("ClinicaDao.updateClinica() : " + ex.getClass().getName() + "," + ex.getMessage());
        }
        return autoRet;
    }

    public int deleteClinica(long cdClinica) throws DaoException {
        int autoRet = -1;
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            Clinica c = selectClinicaByCd(cdClinica);
            conn = getConnection();
            ps = conn.prepareStatement(DELETE_DENTISTS);
            ps.setLong(1,c.getCdClinica());
            ps.execute();
            ps = conn.prepareStatement(DELETE);
            ps.setLong(1, cdClinica);
            ps.execute();
            autoRet = 1;    
            (new PessoaDao()).deletePerson(c.getCdPessoa());
            close(conn, ps);
        } catch (SQLException ex) {
            throw new DaoException("ClinicaDao.deleteClinica() : " + ex.getClass().getName() + "," + ex.getMessage());
        }
        return autoRet;
    }

    private Clinica getClinicaFromRs(ResultSet rs) throws DaoException, SQLException {
        long cdClinica = -1;
        String cnpj = null;
        Pessoa p = null;
        System.out.println(rs.getLong("CD_PESSOA"));
        try {
            cdClinica = rs.getLong("CD_CLINICA");
            cnpj = rs.getString("CNPJ");
            p = getPersonFromRs(rs);
        } catch (SQLException e) {
            throw new DaoException("ClinicaDao.getClinicaFromRs() : " + e.getClass().getName() + "," + e.getMessage(), e);
        }
        return Clinica.newInstance(cdClinica, cnpj, p.getCdPessoa(), p.getNome(), p.getEndereco(), p.getTelefones());
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
    
    private Dentista getDentistFromRs(ResultSet rs) throws DaoException {
        long cdDentista = -1;
        String cro = null;
        Pessoa p = null;
        try {
            cdDentista = rs.getLong("CD_DENTISTA");
            cro = rs.getString("CRO");
            for (Pessoa pessoa: (new PessoaDao()).selectAllPerson()) {
                if (pessoa.getCdPessoa() == rs.getLong("CD_PESSOA")){
                    p = (new PessoaDao()).selectPersonByCd(pessoa.getCdPessoa());
                    break;
                }
            }
        } catch (SQLException e) {
            throw new DaoException("DentistaDao.getDentistFromRs() : " + e.getClass().getName() + "," + e.getMessage(), e);
        }
        return Dentista.newInstance(p.getCdPessoa(), p.getNome(), p.getEndereco(), p.getTelefones(), cdDentista, cro);
    }
}
