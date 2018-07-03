/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model.bean;

import java.util.List;

/**
 *
 * @author filip
 */
public class Clinica extends Pessoa {
    private long cdClinica;
    private String CNPJ;
   
    public static Clinica newInstance(long cdClinica, String CNPJ, long cdPessoa, String nome, Endereco endereco, List<Telefone> telefones){
        if (CNPJ != null && cdPessoa > 0 && nome != null && endereco != null && telefones!=null && cdClinica > 0){
            return new Clinica(cdClinica, CNPJ, cdPessoa, nome, endereco, telefones);
        } else {
            return null;
        }
    }
    
    private Clinica(long cdClinica, String CNPJ, long cdPessoa, String nome, Endereco endereco, List<Telefone> telefones) {
        super(cdPessoa, nome, endereco, telefones);
        this.cdClinica = cdClinica;
        this.CNPJ = CNPJ;
    }

    public long getCdClinica() {
        return cdClinica;
    }

    public void setCdClinica(long cdDentista) {
        this.cdClinica = cdDentista;
    }

    public String getCNPJ() {
        return CNPJ;
    }

    public void setCNPJ(String CNPJ) {
        this.CNPJ = CNPJ;
    }
    
    
}
