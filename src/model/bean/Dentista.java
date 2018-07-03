package model.bean;

import java.util.List;

/**
 *
 * @author filip
 */
public class Dentista extends Pessoa {
    private long cdDentista;
    private String cro;

    private Dentista(long cdPessoa, String nome, Endereco endereco, List<Telefone> telefones, long cdDentista, String cro) {
        super(cdPessoa, nome, endereco, telefones);
        this.cdDentista = cdDentista;
        this.cro = cro;
    }
    
    public static Dentista newInstance(long cdPessoa, String nome, Endereco endereco, List<Telefone> telefones, long cdDentista, String cro) {
        if (cdPessoa>0 && !(nome.isEmpty()) && endereco!=null && telefones.size()>0 && cdDentista > 0 && cro.length()>3)
            return new Dentista(cdPessoa, nome, endereco, telefones, cdDentista, cro);
        else 
            return null;
    }

    public long getCdDentista() {
        return cdDentista;
    }

    public void setCdDentista(long cdDentista) {
        this.cdDentista = cdDentista;
    }

    public String getCro() {
        return cro;
    }

    public void setCro(String cro) {
        this.cro = cro;
    }
    
    
    @Override
    public String toString(){
        return this.getNome();
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 67 * hash + (int) (this.cdDentista ^ (this.cdDentista >>> 32));
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Dentista other = (Dentista) obj;
        if (this.cdDentista != other.cdDentista) {
            return false;
        }
        return true;
    }
   
}
