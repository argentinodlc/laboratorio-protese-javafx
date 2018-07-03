package controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.image.Image;
import laboratorio.Laboratorio;
import model.bean.Telefone;
import model.bean.Usuario;
import model.dao.DaoException;
import model.dao.DentistaDao;
import model.dao.EnderecoDao;
import model.dao.HistoricoDao;
import util.RestrictiveTextField;
import util.showAlert;
import view.EnumTelas;

public class FXMLNewDentistController implements Initializable {

    private static Usuario user;

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
    void addDentista(ActionEvent event){
        if (validaCampos()) {
            try {
                long cod = (new DentistaDao()).insertDentist(tfNome.getText(), 
                        (new EnderecoDao()).insertAddress(tfLogra.getText(), Integer.valueOf(tfNum.getText()), tfComp.getText(), 
                                tfBairro.getText(), tfCidade.getText(), tfCep.getText()), 
                        tbTelefone.getItems(), tfCro.getText());
                (new HistoricoDao()).insertHistoric("Adicionou dentista "+cod+": "+tfNome.getText(), user);
                back(event);
            } catch (DaoException e) {
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
                    "Tem certeza que deseja remover "+tbTelefone.getSelectionModel().getSelectedItem().toString()+"?"
                    , (new Image(this.getClass().getResource("/res/icon.png").toString())));
            if (result.get() == ButtonType.OK) {
                tbTelefone.getItems().remove(tbTelefone.getSelectionModel().getSelectedItem());
                
            }

        }
    }

    private void limpaTela() {
        for ( int i = 0; i<tbTelefone.getItems().size(); i++) {
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
            if (screen.equals(EnumTelas.newDentist)) {
                user = (Usuario) data;
            }
        });
        tfTel.setRestrict("[0-9]");
        tfTel.setMaxLength(12);
        tfTel.setAlignment(Pos.CENTER_LEFT);
        
        TableColumn<Telefone, String> colTelefone = new TableColumn<Telefone, String>("Telefone");
        colTelefone.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getTelefone()));

        tbTelefone.getColumns().setAll(colTelefone);
        
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
        tfCro.setAlignment(Pos.CENTER_LEFT);
    }

    private boolean validaCampos() {
        if (tfBairro.getText().length() > 0 && tfCidade.getText().length() > 0 && tfCro.getText().length() > 0
                && tfLogra.getText().length() > 0 && tfNome.getText().length() > 0 && tfNum.getText().length() > 0) {
            if (tbTelefone.getItems().size()>0) {
                if (tfCep.getText().length()>0 || tfComp.getText().length() > 0) {
                    if (tfCep.getText().length()==8) {
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
