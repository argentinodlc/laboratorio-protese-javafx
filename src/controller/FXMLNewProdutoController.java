/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.Initializable;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import laboratorio.Laboratorio;
import model.bean.Usuario;
import model.dao.DaoException;
import model.dao.HistoricoDao;
import model.dao.ProdutoDao;
import view.EnumTelas;
/**
 * FXML Controller class
 *
 * @author filip
 */
public class FXMLNewProdutoController implements Initializable {
    private static Usuario user;
    /**
     * Initializes the controller class.
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        Laboratorio.addOnChangeScreenListener((EnumTelas screen, Object data) -> {
            if (screen.equals(EnumTelas.newProduto)) {
                user = (Usuario) data;
                limpaTela();
            }
        });
    }    
    
    
    @FXML
    private JFXButton btCancelar;

    @FXML
    private JFXButton btConfirmar;

    @FXML
    private JFXTextField tfValor;

    @FXML
    private JFXTextField tfNome;

    @FXML
    void addProduto(ActionEvent event) throws DaoException {
        if (validaCampos()){
            long cod = (new ProdutoDao()).insertProduto(tfNome.getText(), Double.parseDouble(tfValor.getText()));
            if (cod != -1){
                (new HistoricoDao()).insertHistoric("Adicionou produto "+cod+": "+tfNome.getText(), user);
                back(event);
            }
        }
    }

    @FXML
    void back(ActionEvent event) {
        Laboratorio.changeScreen(EnumTelas.produto, user);
    }
    
    private boolean validaCampos(){
        try {
            return tfNome.getText()!=null && Double.parseDouble(tfValor.getText())>=0;
        } catch (NumberFormatException e){ 
            return false;
        }
    }
    
    private void limpaTela(){
        tfNome.setText("");
        tfValor.setText("");
    }
}
