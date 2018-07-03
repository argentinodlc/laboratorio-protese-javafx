package controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.image.Image;
import laboratorio.Laboratorio;
import model.bean.Clinica;
import model.bean.Dentista;
import model.bean.Endereco;
import model.bean.Telefone;
import model.bean.Usuario;
import model.dao.ClinicaDao;
import model.dao.DaoException;
import model.dao.DentistaDao;
import model.dao.EnderecoDao;
import model.dao.HistoricoDao;
import util.RestrictiveTextField;
import util.showAlert;
import view.EnumTelas;

public class FXMLAlterarClinicaController implements Initializable {

    private static Clinica clinic;
    private static Usuario user;

    @FXML
    private JFXButton btCancelar;

    @FXML
    private JFXButton btConfirmar;

    @FXML
    private RestrictiveTextField tfNome;

    @FXML
    private JFXTextField tfCnpj;

    @FXML
    private RestrictiveTextField tfTel;

    @FXML
    private JFXButton btAdd;

    @FXML
    private JFXButton btRemover;

    @FXML
    private TableView<Telefone> tbTelefone;

    @FXML
    private RestrictiveTextField tfCep;

    @FXML
    private RestrictiveTextField tfLogra;

    @FXML
    private RestrictiveTextField tfNum;

    @FXML
    private JFXTextField tfComp;

    @FXML
    private RestrictiveTextField tfBairro;

    @FXML
    private RestrictiveTextField tfCidade;

    @FXML
    private JFXComboBox<Dentista> cbDentistas;

    @FXML
    private JFXButton btAddD;

    @FXML
    private JFXButton btRemD;

    @FXML
    private JFXButton btBuscarD;

    @FXML
    private TableView<Dentista> tbDentistas;

    public void initialize(URL url, ResourceBundle rb) {
        Laboratorio.addOnChangeScreenListener((EnumTelas screen, Object data) -> {
            limpaTela();
            if (screen.equals(EnumTelas.updateClinica)) {
                List l = (List) data;
                user = (Usuario) l.get(0);
                clinic = (Clinica) l.get(1);
                try {
                    //Preencher cb de dentista
                    List<Dentista> aList = new ArrayList<Dentista>((new DentistaDao()).selectAllDentist());
                    for (Dentista d : aList) {
                        cbDentistas.getItems().add(d);
                    }
                } catch (DaoException ex) {
                    Logger.getLogger(FXMLNewClinicController.class.getName()).log(Level.SEVERE, null, ex);
                }
                tfNome.setText(clinic.getNome());
                tfCnpj.setText(clinic.getCNPJ());
                Endereco e = clinic.getEndereco();
                tfLogra.setText(e.getLogradouro());
                tfNum.setText(String.valueOf(e.getNumero()));
                tfComp.setText(e.getComplemento());
                tfBairro.setText(e.getBairro());
                tfCidade.setText(e.getCidade());
                tfCep.setText(e.getCep());
                for (Telefone t : clinic.getTelefones()) {
                    tbTelefone.getItems().add(t);
                }
                try {
                    for (Dentista d : (new ClinicaDao()).selectDentists(clinic.getCdClinica())) {
                        tbDentistas.getItems().add(d);
                    }
                } catch (DaoException ex) {
                    Logger.getLogger(FXMLAlterarClinicaController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });

        TableColumn<Telefone, String> colTelefone = new TableColumn<Telefone, String>("Telefone");
        colTelefone.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getTelefone()));

        tbTelefone.getColumns().setAll(colTelefone);

        TableColumn<Dentista, String> colDentista = new TableColumn<Dentista, String>("Dentista");
        colDentista.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getNome()));

        tbDentistas.getColumns().setAll(colDentista);

        tfTel.setRestrict("[0-9]");
        tfTel.setMaxLength(12);
        tfTel.setAlignment(Pos.CENTER_LEFT);
        tfNome.setRestrict("[a-z/A-Z]");
        tfNome.setMaxLength(40);
        tfNome.setAlignment(Pos.CENTER_LEFT);
        tfNum.setRestrict("[0-9]");
        tfNum.setMaxLength(5);
        tfNum.setAlignment(Pos.CENTER_LEFT);
        tfBairro.setRestrict("[a-z/A-Z]");
        tfBairro.setMaxLength(20);
        tfBairro.setAlignment(Pos.CENTER_LEFT);
        tfCidade.setRestrict("[a-z/A-Z]");
        tfCidade.setMaxLength(20);
        tfCidade.setAlignment(Pos.CENTER_LEFT);
        tfLogra.setMaxLength(40);
        tfLogra.setAlignment(Pos.CENTER_LEFT);
        tfCep.setRestrict("[0-9]");
        tfCep.setMaxLength(8);
        tfCep.setAlignment(Pos.CENTER_LEFT);
        tfCnpj.setAlignment(Pos.CENTER_LEFT);
        tfComp.setAlignment(Pos.CENTER_LEFT);
        tfNome.setLabelFloat(true);
        tfCnpj.setLabelFloat(true);
        tfTel.setLabelFloat(true);
        tfLogra.setLabelFloat(true);
        tfNum.setLabelFloat(true);
        tfComp.setLabelFloat(true);
        tfCep.setLabelFloat(true);
        tfCidade.setLabelFloat(true);
        tfBairro.setLabelFloat(true);

        cbDentistas.setPromptText("Selecione um dentista");
        cbDentistas.setLabelFloat(true);

    }

    @FXML
    void addClinica(ActionEvent event) throws DaoException {
        System.out.println("chegou");
        if (validaCampos()) {
            System.out.println("validou");
//            try {
//                long cdEndereco = (new EnderecoDao()).insertAddress(tfLogra.getText(), Integer.valueOf(tfNum.getText()), tfComp.getText(),
//                        tfBairro.getText(), tfCidade.getText(), tfCep.getText());
//                long cd = (new ClinicaDao()).insertClinica(tfNome.getText(), cdEndereco, tbTelefone.getItems(), tfCnpj.getText(), tbDentistas.getItems());
//                (new HistoricoDao()).insertHistoric("Adicionou clinica " + cd + ": " + tfNome.getText(), user);
//                back(event);
//            } catch (DaoException e) {
//                e.printStackTrace();
//            }
//        } else {
            Clinica c = Clinica.newInstance(clinic.getCdClinica(),
                    tfCnpj.getText(),
                    clinic.getCdPessoa(),
                    tfNome.getText(),
                    Endereco.newInstance(clinic.getEndereco().getCdEndereco(), tfLogra.getText(), Integer.valueOf(tfNum.getText()), tfComp.getText(), tfBairro.getText(), tfCidade.getText(), tfCep.getText()),
                    tbTelefone.getItems());
            long cd = (new ClinicaDao()).updateClinica(c, tbDentistas.getItems());
            (new HistoricoDao()).insertHistoric("Alterou clinica " + cd + ": " + tfNome.getText(), user);
            back(event);
        }
    }

    @FXML
    void addDentista(ActionEvent event) {
        boolean can = true;
        if (cbDentistas.getSelectionModel().getSelectedItem() != null) {
            for (Dentista d: tbDentistas.getItems()) {
                if (d.equals(cbDentistas.getSelectionModel().getSelectedItem()))
                    can = false;
            }
            if (can)
                tbDentistas.getItems().add(cbDentistas.getSelectionModel().getSelectedItem());
        }
    }

    @FXML
    void addTelefone(ActionEvent event) {
        if (tfTel.getText().length() > 7) {
            Telefone tel = Telefone.newInstance(1, tfTel.getText());
            tbTelefone.getItems().add(tel);
        }
    }

    @FXML
    void back(ActionEvent event) {
        Laboratorio.changeScreen(EnumTelas.clinica, user);
        limpaTela();
    }

    @FXML
    void buscarDentista(ActionEvent event) {

    }

    @FXML
    void removeTelefone(ActionEvent event) {
        if (tbTelefone.getSelectionModel().getSelectedItem() != null) {
            Optional<ButtonType> result = showAlert.showConfirmAlert("Remover",
                    "Tem certeza que deseja remover " + tbTelefone.getSelectionModel().getSelectedItem().toString() + "?",
                    (new Image(this.getClass().getResource("/res/icon.png").toString())));
            if (result.get() == ButtonType.OK) {
                tbTelefone.getItems().remove(tbTelefone.getSelectionModel().getSelectedItem());

            }

        }
    }

    @FXML
    void removerDentista(ActionEvent event) {
        if (tbDentistas.getSelectionModel().getSelectedItem() != null) {
            Optional<ButtonType> result = showAlert.showConfirmAlert("Remover",
                    "Tem certeza que deseja remover " + tbDentistas.getSelectionModel().getSelectedItem().toString() + "?",
                    (new Image(this.getClass().getResource("/res/icon.png").toString())));
            if (result.get() == ButtonType.OK) {
                tbDentistas.getItems().remove(tbDentistas.getSelectionModel().getSelectedItem());

            }

        }
    }

    private boolean validaCampos() {
        if (tfBairro.getText().length() > 0 && tfCidade.getText().length() > 0 && tfCnpj.getText().length() > 0
                && tfLogra.getText().length() > 0 && tfNome.getText().length() > 0 && tfNum.getText().length() > 0
                && tbTelefone.getItems().size() > 0 && tfCep.getText().length() > 0) {
            return true;
        } else {
            return false;
        }

    }

    private void limpaTela() {
        for (int i = 0; i < cbDentistas.getItems().size(); i++) {
            cbDentistas.getItems().clear();
        }
        for (int i = 0; i < tbTelefone.getItems().size(); i++) {
            tbTelefone.getItems().clear();
        }
        for (int i = 0; i < tbDentistas.getItems().size(); i++) {
            tbDentistas.getItems().clear();
        }
        tfBairro.setText("");
        tfCep.setText("");
        tfCidade.setText("");
        tfComp.setText("");
        tfCnpj.setText("");
        tfLogra.setText("");
        tfNome.setText("");
        tfNum.setText("");
        tfTel.setText("");
    }

}
