package model.bean;

import java.util.Date;

public class Historico {
    private long cdHistorico;
    private String acao;
    private Usuario user;
    private Date data;
    
    public static Historico newInstance(long cdHistorico, String acao, Usuario user, Date data) {
        if (cdHistorico > 0 && !(acao.isEmpty()) && user!=null && data != null)
           return new Historico(cdHistorico, acao, user, data);
        else
            return null;
    }

    private Historico(long cdHistorico, String acao, Usuario user, Date data) {
        this.cdHistorico = cdHistorico;
        this.acao = acao;
        this.user = user;
        this.data = data;
    }

    public long getCdHistorico() {
        return cdHistorico;
    }

    public void setCdHistorico(long cdHistorico) {
        this.cdHistorico = cdHistorico;
    }

    public String getAcao() {
        return acao;
    }

    public void setAcao(String acao) {
        this.acao = acao;
    }

    public Usuario getUser() {
        return user;
    }

    public void setUser(Usuario user) {
        this.user = user;
    }

    public Date getData() {
        return data;
    }

    public void setData(Date data) {
        this.data = data;
    }
    
    
    
}
