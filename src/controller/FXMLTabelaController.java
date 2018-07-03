/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.Initializable;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import laboratorio.Laboratorio;
import model.bean.Clinica;
import model.bean.Dentista;
import view.EnumTelas;

/**
 * FXML Controller class
 *
 * @author filip
 */
public class FXMLTabelaController implements Initializable {

    List list;
    Clinica c;

    public FXMLTabelaController(List list) {
        this.c = (Clinica) list.get(0);
        this.list = (List) list.get(1);
    }
    @FXML
    private Label label;

    @FXML
    private TableView<Dentista> table;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        label.setText("Dentistas da Clínica " + c.getNome());
        TableColumn<Dentista, String> colNome = new TableColumn("Nome");
        colNome.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getNome()));
        TableColumn<Dentista, String> colCro = new TableColumn("CRO");
        colCro.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getCro()));
        TableColumn<Dentista, String> colEndereco = new TableColumn("Endereco");
        colEndereco.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getEndereco().toString()));
        TableColumn<Dentista, String> colTelefone = new TableColumn("Telefone");
        colTelefone.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getTelefones().toString()));

        table.getColumns().setAll(colNome, colCro, colEndereco, colTelefone);

        preencherTabela();

    }

    public void preencherTabela() {
        for (int i = 0; i < table.getItems().size(); i++) {
            table.getItems().clear();
        }
        for (Dentista d : (List<Dentista>) list) {
            table.getItems().add(d);
        }
        table.autosize();
    }

}
