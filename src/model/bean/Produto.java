/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model.bean;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

public class Produto{

    private long cdProduto;
    private String nome;
    private double valor;
    
    public static Produto newInstance(long cdProduto, String nome, double valor){
        if (cdProduto > 0 && nome!=null && valor >= 0)
            return new Produto(cdProduto, nome, valor);
        else 
            return null;
    }
    
    private Produto(long cdProduto, String nome, double valor) {
        this.cdProduto = cdProduto;
        this.nome = nome;
        this.valor = valor;
    }

    public long getCdProduto() {
        return cdProduto;
    }

    public void setCdProduto(long cdProduto) {
        this.cdProduto = cdProduto;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public double getValor() {
        return valor;
    }

    public void setValor(double valor) {
        this.valor = valor;
    }

    
}
