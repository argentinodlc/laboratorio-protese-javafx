package model.bean;
public class Endereco {
    private long cdEndereco;
    private String logradouro;
    private int numero;
    private String complemento;
    private String bairro;
    private String cidade;
    private String cep;

    private Endereco(long cdEndereco, String logradouro, int numero, String complemento, String bairro, String cidade, String cep) {
        this.cdEndereco = cdEndereco;
        this.logradouro = logradouro;
        this.numero = numero;
        this.complemento = complemento;
        this.cidade = cidade;
        this.bairro = bairro;
        this.cep = cep;
    }

    public static Endereco newInstance(long cdEndereco, String logradouro, int numero, String complemento, String bairro, String cidade, String cep) {
        if (cdEndereco > 0 && logradouro.length()>0 && numero > 0 && bairro.length()>0 && cidade.length()>0)
            return new Endereco(cdEndereco, logradouro, numero, complemento, bairro, cidade, cep);
        else
            return null;
    }
    
    public long getCdEndereco() {
        return cdEndereco;
    }

    public void setCdEndereco(long cdEndereco) {
        this.cdEndereco = cdEndereco;
    }

    public String getLogradouro() {
        return logradouro;
    }

    public void setLogradouro(String logradouro) {
        this.logradouro = logradouro;
    }

    public int getNumero() {
        return numero;
    }

    public void setNumero(int numero) {
        this.numero = numero;
    }

    public String getComplemento() {
        return complemento;
    }

    public void setComplemento(String complemento) {
        this.complemento = complemento;
    }

    public String getBairro() {
        return bairro;
    }

    public void setBairro(String bairro) {
        this.bairro = bairro;
    }

    public String getCidade() {
        return cidade;
    }

    public void setCidade(String cidade) {
        this.cidade = cidade;
    }

    public String getCep() {
        return cep;
    }

    public void setCep(String cep) {
        this.cep = cep;
    }

    @Override
    public String toString() {
        if (!(complemento.equals("null")) && !(cep.equals("null")))
            return logradouro + ", " + numero + " " + complemento + ", " + bairro + ", " + cidade + ", " + cep;
        else if (complemento.equals("null") && !(cep.equals("null"))) 
            return logradouro + ", " + numero + ", " + bairro + ", " + cidade + ", " + cep;
        else if (!(complemento.equals("null")) && cep.equals("null"))
            return logradouro + ", " + numero + " " + complemento + ", " + bairro + ", " + cidade;
        else
            return logradouro + ", " + numero + ", " + bairro + ", " + cidade;
    }

    
    
}
