package model.bean;

/**
 *
 * @author filip
 */
public class Telefone {
    private long cdTelefone;
    private String telefone;

    public long getCdTelefone() {
        return cdTelefone;
    }

    public void setCdTelefone(long cdTelefone) {
        this.cdTelefone = cdTelefone;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    private Telefone(long cdTelefone, String telefone) {
        this.cdTelefone = cdTelefone;
        this.telefone = telefone;
    }
    
    public static Telefone newInstance(long cdTelefone, String telefone){
        if (cdTelefone > 0 && telefone.length()>7) 
            return new Telefone(cdTelefone, telefone);
        else
            return null;
    }
    
    @Override
    public String toString(){
        return telefone;
    }
}
