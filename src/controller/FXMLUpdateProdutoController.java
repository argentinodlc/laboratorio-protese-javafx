/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import com.jfoenix.controls.JFXTextField;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import laboratorio.Laboratorio;
import model.bean.Produto;
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
public class FXMLUpdateProdutoController implements Initializable {

    private static Usuario user;
    private static Produto p;

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        Laboratorio.addOnChangeScreenListener((EnumTelas screen, Object data) -> {
            if (screen.equals(EnumTelas.updateProduto)) {
                user = (Usuario) ((List) data).get(0);
                p = (Produto) ((List) data).get(1);
                limpaTela();
                tfNome.setText(p.getNome());
                tfValor.setText(String.valueOf(p.getValor()));
            }
        });
    }

    @FXML
    private JFXTextField tfValor;

    @FXML
    private JFXTextField tfNome;

    @FXML
    void alterarProduto(ActionEvent event) throws DaoException {
        if (validaCampos()) {
            p.setNome(tfNome.getText());
            p.setValor(Double.parseDouble(tfValor.getText()));
            long cod = (new ProdutoDao()).updateProduto(p);
            if (cod != -1) {
                (new HistoricoDao()).insertHistoric("Alterou produto " + cod + ": " + tfNome.getText(), user);
                back(event);
            }
        }
    }

    @FXML
    void back(ActionEvent event) {
        Laboratorio.changeScreen(EnumTelas.produto, user);
    }

    private boolean validaCampos() {
        try {
            return tfNome.getText() != null && Double.parseDouble(tfValor.getText()) >= 0;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private void limpaTela() {
        tfNome.setText("");
        tfValor.setText("");
    }

}
