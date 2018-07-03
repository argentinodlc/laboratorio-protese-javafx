package model.bean;

import java.util.Objects;

public class Usuario {
    private long cdusuario;
    private String nome;
    private String login;
    private String senha;
    private int permissao;
    private byte[] imagem;

    private Usuario(long cd_usuario, String nome, String login, String senha, int permissao, byte[] byteArray) {
        this.cdusuario = cd_usuario;
        this.nome = nome;
        this.login = login;
        this.senha = senha;
        this.permissao = permissao;
        this.imagem = byteArray;
    }
    
    public static Usuario newInstance(long cd_usuario, String nome, String login, String senha, int permissao, byte[] byteArray) {
        if(cd_usuario != -1 && nome.length()>0 && login.length()>0 && senha.length() > 0)
            return new Usuario(cd_usuario, nome, login, senha, permissao    , byteArray);
        else 
            return null;
        
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 23 * hash + (int) (this.cdusuario ^ (this.cdusuario >>> 32));
        hash = 23 * hash + Objects.hashCode(this.login);
        hash = 23 * hash + Objects.hashCode(this.senha);
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
        final Usuario other = (Usuario) obj;
        if (this.cdusuario != other.cdusuario) {
            return false;
        }
        if (!Objects.equals(this.login, other.login)) {
            return false;
        }
        if (!Objects.equals(this.senha, other.senha)) {
            return false;
        }
        return true;
    }
    
   
    
    @Override
    public String toString(){
        return getNome();
    }
    
    public long getCdusuario() {
        return cdusuario;
    }

    public void setCdusuario(long cdusuario) {
        this.cdusuario = cdusuario;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public int getPermissao() {
        return permissao;
    }

    public void setPermissao(int permissao) {
        this.permissao = permissao;
    } 

    public String getLogin() {
        return login;
    }
    
    public void setLogin(String login) {
        this.login = login;
    }

    public byte[] getImagem() {
        return imagem;
    }

    public void setImagem(byte[] imagem) {
        this.imagem = imagem;
    }
    
    public String isAdm(){
        if (permissao == 0) {
            return "Usuário comum";
        } else
            return "Administrador";
    }
}
