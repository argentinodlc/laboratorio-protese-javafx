package model.bean;

import java.util.List;

public class Pessoa {
    private long cdPessoa;
    private String nome;
    private Endereco endereco;
    private List<Telefone> telefones;

    Pessoa(long cdPessoa, String nome, Endereco endereco, List<Telefone> telefones) {
        this.cdPessoa = cdPessoa;
        this.nome = nome;
        this.endereco = endereco;
        this.telefones = telefones;
    }
    
    public static Pessoa newInstance(long cdPessoa, String nome, Endereco endereco, List<Telefone> telefones){
        if(cdPessoa>0 && !(nome.isEmpty()) && endereco!=null && telefones.size()>0)
            return new Pessoa(cdPessoa, nome, endereco, telefones);
        else
            return null;
    }

    public long getCdPessoa() {
        return cdPessoa;
    }

    public void setCdPessoa(long cdPessoa) {
        this.cdPessoa = cdPessoa;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Endereco getEndereco() {
        return endereco;
    }

    public void setEndereco(Endereco endereco) {
        this.endereco = endereco;
    }

    public List<Telefone> getTelefones() {
        return telefones;
    }

    public void setTelefones(List<Telefone> telefones) {
        this.telefones = telefones;
    }
    
    
}
