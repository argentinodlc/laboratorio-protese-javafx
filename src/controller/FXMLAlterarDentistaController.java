package controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.image.Image;
import javafx.util.Duration;
import laboratorio.Laboratorio;
import model.bean.Dentista;
import model.bean.Endereco;
import model.bean.Telefone;
import model.bean.Usuario;
import model.dao.DaoException;
import model.dao.DentistaDao;
import model.dao.EnderecoDao;
import model.dao.HistoricoDao;
import tray.animations.AnimationType;
import tray.notification.NotificationType;
import tray.notification.TrayNotification;
import util.RestrictiveTextField;
import util.showAlert;
import view.EnumTelas;

public class FXMLAlterarDentistaController implements Initializable {

    private static Usuario user;
    private static Dentista d;

    @FXML
    private RestrictiveTextField tfNome;

    @FXML
    private JFXTextField tfCro;

    @FXML
    private TableView<Telefone> tbTelefone;

    @FXML
    private TableColumn<Telefone, String> colTelefone;

    @FXML
    private JFXButton btAdd;

    @FXML
    private JFXButton btRemover;

    @FXML
    private RestrictiveTextField tfTel;

    @FXML
    private RestrictiveTextField tfNum;

    @FXML
    private RestrictiveTextField tfLogra;

    @FXML
    private JFXTextField tfComp;

    @FXML
    private RestrictiveTextField tfCep;

    @FXML
    private RestrictiveTextField tfBairro;

    @FXML
    private RestrictiveTextField tfCidade;

    @FXML
    private JFXButton btCancelar;

    @FXML
    private JFXButton btConfirmar;

    @FXML
    void back(ActionEvent event) {
        Laboratorio.changeScreen(EnumTelas.dentista, user);
        limpaTela();
    }

    @FXML
    void updateDentista(ActionEvent event) {
        if (validaCampos()) {
            try {
                Endereco e = (new EnderecoDao()).selectAddress(d.getEndereco().getCdEndereco());
                e.setBairro(tfBairro.getText());
                e.setCep(tfCep.getText());
                e.setCidade(tfCidade.getText());
                e.setComplemento(tfComp.getText());
                e.setLogradouro(tfLogra.getText());
                e.setNumero(Integer.valueOf(tfNum.getText()));
                d.setEndereco(e);
                d.setNome(tfNome.getText());
                d.setCro(tfCro.getText());
                d.setTelefones(tbTelefone.getItems());
                if ((new DentistaDao()).updateDentist(d) == d.getCdDentista()) {
                    back(event);
                    TrayNotification tray = new TrayNotification("Dentista alterado", "Você alterou o dentista com sucesso", NotificationType.SUCCESS);
                    tray.setAnimationType(AnimationType.FADE);
                    tray.showAndDismiss(Duration.seconds(2));
                    (new HistoricoDao()).insertHistoric("Alterou dentista " + d.getCdDentista() + ": " + d.getNome(), user);
                } else {
                    TrayNotification tray = new TrayNotification("Problema ao alterar", "Ocorreu um problema ao alterar o dentista", NotificationType.WARNING);
                    tray.setAnimationType(AnimationType.FADE);
                    tray.showAndDismiss(Duration.seconds(2));
                }

            } catch (DaoException e) {
                TrayNotification tray = new TrayNotification("Problema ao alterar", e.getMessage(), NotificationType.ERROR);
                tray.setAnimationType(AnimationType.FADE);
                tray.showAndDismiss(Duration.seconds(2));
                e.printStackTrace();
            }
        }
    }

    @FXML
    void addTelefone(ActionEvent event) {
        if (tfTel.getText().length() > 7) {
            Telefone tel = Telefone.newInstance(1, tfTel.getText());
            tbTelefone.getItems().add(tel);
        } else {

        }
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

    private void limpaTela() {
        for (int i = 0; i < tbTelefone.getItems().size(); i++) {
            tbTelefone.getItems().clear();
        }
        tfBairro.setText("");
        tfCep.setText("");
        tfCidade.setText("");
        tfComp.setText("");
        tfCro.setText("");
        tfLogra.setText("");
        tfNome.setText("");
        tfNum.setText("");
        tfTel.setText("");
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        Laboratorio.addOnChangeScreenListener((EnumTelas screen, Object data) -> {
            if (screen.equals(EnumTelas.updateDentist)) {
                List objetos = (List) data;
                d = (Dentista) objetos.get(0);
                user = (Usuario) objetos.get(1);
                tbTelefone.getItems().addAll(d.getTelefones());

                tfNome.setText(d.getNome());
                tfNome.setAlignment(Pos.CENTER_LEFT);
                tfNum.setText(String.valueOf(d.getEndereco().getNumero()));
                tfNum.setAlignment(Pos.CENTER_LEFT);
                tfBairro.setText(d.getEndereco().getBairro());
                tfBairro.setAlignment(Pos.CENTER_LEFT);
                tfCidade.setText(d.getEndereco().getCidade());
                tfCidade.setAlignment(Pos.CENTER_LEFT);
                tfLogra.setText(d.getEndereco().getLogradouro());
                tfLogra.setAlignment(Pos.CENTER_LEFT);
                tfCep.setText(d.getEndereco().getCep());
                tfCep.setAlignment(Pos.CENTER_LEFT);
                tfCro.setText(d.getCro());
                tfCro.setAlignment(Pos.CENTER_LEFT);
                if (tfCep.getText().equals("null")) {
                    tfCep.setText("");
                }
                if (tfComp.getText().equals("null")) {
                    tfComp.setText("");
                    tfComp.setAlignment(Pos.CENTER_LEFT);
                }
            }
        });
        tfTel.setRestrict("[0-9]");
        tfTel.setMaxLength(12);
        tfTel.setAlignment(Pos.CENTER_LEFT);

        TableColumn<Telefone, String> colTelefone = new TableColumn<Telefone, String>("Telefone");
        colTelefone.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getTelefone()));

        tfNome.setRestrict("[a-z/A-Z]");
        tfNome.setMaxLength(40);
        tfNum.setRestrict("[0-9]");
        tfNum.setMaxLength(5);
        tfBairro.setRestrict("[a-z/A-Z]");
        tfBairro.setMaxLength(20);
        tfCidade.setRestrict("[a-z/A-Z]");
        tfCidade.setMaxLength(20);
        tfLogra.setMaxLength(40);
        tfCep.setRestrict("[0-9]");
        tfCep.setMaxLength(8);
        tbTelefone.getColumns().setAll(colTelefone);

    }

    private boolean validaCampos() {
        if (tfBairro.getText().length() > 0 && tfCidade.getText().length() > 0 && tfCro.getText().length() > 0
                && tfLogra.getText().length() > 0 && tfNome.getText().length() > 0 && tfNum.getText().length() > 0) {
            if (tbTelefone.getItems().size() > 0) {
                if (tfCep.getText().length() > 0 || tfComp.getText().length() > 0) {
                    if (tfCep.getText().length() == 8) {
                        return true;
                    } else {
                        return false;
                    }
                } else {
                    return true;
                }
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

}
