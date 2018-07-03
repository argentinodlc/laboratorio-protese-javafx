package controller;

import com.jfoenix.controls.JFXButton;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.util.Duration;
import laboratorio.Laboratorio;
import model.bean.Dentista;
import model.bean.Usuario;
import model.dao.DaoException;
import model.dao.DentistaDao;
import model.dao.HistoricoDao;
import tray.animations.AnimationType;
import tray.notification.NotificationType;
import tray.notification.TrayNotification;
import util.bytesToImg;
import util.showAlert;
import view.EnumTelas;

public class FXMLDentistaController implements Initializable {

    private static Usuario user;

    @FXML
    private JFXButton btInicio;

    @FXML
    private JFXButton btServicos;

    @FXML
    private JFXButton btRelatorios;

    @FXML
    private JFXButton btClientes;

    @FXML
    private JFXButton btFuncionarios;

    @FXML
    private JFXButton btFinanceiro;

    @FXML
    private Circle circle;

    @FXML
    private Label tfNome;

    @FXML
    private ImageView btUpdtUser;

    @FXML
    private JFXButton btSair;

    @FXML
    private JFXButton btTools;

    @FXML
    private TableView<Dentista> tbDentista;

    @FXML
    private JFXButton btNovo;

    @FXML
    private JFXButton btAlterar;

    @FXML
    private JFXButton btExcluir;

    @FXML
    private JFXButton btSelecionar;

    @FXML
    void selecionarDentista(ActionEvent event) {
        List list = new ArrayList<>();
        list.add(tbDentista.getSelectionModel().getSelectedItem());
        list.add(user);
        Laboratorio.changeScreen(EnumTelas.principal, list);
    }

    @FXML
    void changeScreenNewDentist(ActionEvent event) {
        if (isAdm()) {
            Laboratorio.changeScreen(EnumTelas.newDentist, user);
        }
    }

    @FXML
    void changeScreenConfig(ActionEvent event) {
        Laboratorio.changeScreen(EnumTelas.usuarios, user);
    }

    @FXML
    void logout(ActionEvent event) throws IOException {
        Optional<ButtonType> result = showAlert.showConfirmAlert("Sair", "Tem certeza que deseja sair?", (new Image(this.getClass().getResource("/res/icon.png").toString())));
        if (result.get() == ButtonType.OK) {
            Laboratorio.changeScreen(EnumTelas.login);
        }
    }

    @FXML
    void updtUser(MouseEvent event) {
        Laboratorio.changeScreen(EnumTelas.updateUser, user);
    }

    @FXML
    void back(ActionEvent event) {
//        limpa();
        Laboratorio.changeScreen(EnumTelas.principal, user);
    }

    @FXML
    void removeDentista(ActionEvent event) {
        if (isAdm()) {
            if (tbDentista.getSelectionModel().getSelectedItem() != null) {
                Optional<ButtonType> result = showAlert.showConfirmAlert("Remover",
                        "Tem certeza que deseja remover " + tbDentista.getSelectionModel().getSelectedItem().getNome() + "?",
                        (new Image(this.getClass().getResource("/res/icon.png").toString())));
                if (result.get() == ButtonType.OK) {
                    try {
                        if ((new DentistaDao()).deleteDentist(tbDentista.getSelectionModel().getSelectedItem().getCdDentista()) == 1) {
                            Dentista d = tbDentista.getSelectionModel().getSelectedItem();
                            tbDentista.getItems().remove(tbDentista.getSelectionModel().getSelectedItem());
                            TrayNotification tray = new TrayNotification("Dentista removido", "Você removeu o dentista com sucesso", NotificationType.SUCCESS);
                            tray.setAnimationType(AnimationType.FADE);
                            tray.showAndDismiss(Duration.seconds(1));
                            (new HistoricoDao()).insertHistoric("Removeu dentista " + d.getCdDentista() + ": " + d.getNome(), user);
                        } else {
                            Dentista d = tbDentista.getSelectionModel().getSelectedItem();
                            TrayNotification tray = new TrayNotification("Erro ao remover dentista", d.getNome() + " provavelmente está vinculado\nà uma clínica ou a um serviço", NotificationType.ERROR);
                            tray.setAnimationType(AnimationType.FADE);
                            tray.showAndDismiss(Duration.seconds(1));
                        }
                    } catch (DaoException e) {
                        Dentista d = tbDentista.getSelectionModel().getSelectedItem();
                        TrayNotification tray = new TrayNotification("Erro ao remover dentista", d.getNome() + " provavelmente está vinculado\n à uma clínica ou a um serviço", NotificationType.ERROR);
                        tray.setAnimationType(AnimationType.FADE);
                        tray.showAndDismiss(Duration.seconds(1));
                    }
                }
            }
        }
    }

    @FXML
    void alterarDentista(ActionEvent event) {
        if (isAdm()) {
            if (tbDentista.getSelectionModel().getSelectedItem() != null) {
                List aList = new ArrayList();
                aList.add(tbDentista.getSelectionModel().getSelectedItem());
                aList.add(user);
                Laboratorio.changeScreen(EnumTelas.updateDentist, aList);
            }
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Image imageInicio = new Image(getClass().getResource("/res/home.png").toExternalForm(), 100, 100, true, true);
        ImageView imageViewInicio = new ImageView(imageInicio);

        imageViewInicio.setFitWidth(32);

        imageViewInicio.setFitHeight(32);
        btInicio.setGraphic(imageViewInicio);

        Image imageServico = new Image(getClass().getResource("/res/services.png").toExternalForm(), 100, 100, true, true);
        ImageView imageViewServico = new ImageView(imageServico);

        imageViewServico.setFitWidth(32);

        imageViewServico.setFitHeight(32);

        btServicos.setGraphic(imageViewServico);

        Image imageRelatorio = new Image(getClass().getResource("/res/report.png").toExternalForm(), 100, 100, true, true);
        ImageView imageViewRelatorio = new ImageView(imageRelatorio);

        imageViewRelatorio.setFitWidth(32);

        imageViewRelatorio.setFitHeight(32);

        btRelatorios.setGraphic(imageViewRelatorio);

        Image imageCliente = new Image(getClass().getResource("/res/customer.png").toExternalForm(), 100, 100, true, true);
        ImageView imageViewCliente = new ImageView(imageCliente);

        imageViewCliente.setFitWidth(32);

        imageViewCliente.setFitHeight(32);

        btClientes.setGraphic(imageViewCliente);

        Image imageFuncionario = new Image(getClass().getResource("/res/employee.png").toExternalForm(), 100, 100, true, true);
        ImageView imageViewFuncionario = new ImageView(imageFuncionario);

        imageViewFuncionario.setFitWidth(32);

        imageViewFuncionario.setFitHeight(32);

        btFuncionarios.setGraphic(imageViewFuncionario);

        Image imageFinanceiro = new Image(getClass().getResource("/res/financial.png").toExternalForm(), 100, 100, true, true);
        ImageView imageViewFinanceiro = new ImageView(imageFinanceiro);

        imageViewFinanceiro.setFitWidth(32);

        imageViewFinanceiro.setFitHeight(32);

        btFinanceiro.setGraphic(imageViewFinanceiro);

        Image imageUser = new Image(getClass().getResource("/res/Steam.jpg").toExternalForm(), 100, 100, true, true);
        ImageView imageViewUser = new ImageView(imageUser);
        circle.setFill(new ImagePattern(imageUser));

        Image imageSair = new Image(getClass().getResource("/res/doorway.png").toExternalForm(), 100, 100, true, true);
        ImageView imageViewSair = new ImageView(imageSair);

        imageViewSair.setFitWidth(32);

        imageViewSair.setFitHeight(32);

        btSair.setGraphic(imageViewSair);

        Image imageTools = new Image(getClass().getResource("/res/tools.png").toExternalForm(), 100, 100, true, true);
        ImageView imageViewTools = new ImageView(imageTools);

        imageViewTools.setFitWidth(32);

        imageViewTools.setFitHeight(32);

        btTools.setGraphic(imageViewTools);

        Laboratorio.addOnChangeScreenListener((EnumTelas screen, Object data) -> {
            if (screen.equals(EnumTelas.dentista)) {
                user = (Usuario) data;
                preencherTabela();
                if (user.getImagem() != null) {
                    try {
                        Image img = bytesToImg.toImage(user.getImagem());
                        circle.setFill(new ImagePattern(img));
                    } catch (IOException ex) {
                        Logger.getLogger(FXMLPrincipalController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                } else {

                }
                tfNome.setText(user.getNome());
            }
        });
        TableColumn<Dentista, String> colNome = new TableColumn("Nome");
        colNome.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getNome()));
        TableColumn<Dentista, String> colCro = new TableColumn("CRO");
        colCro.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getCro()));
        TableColumn<Dentista, String> colEndereco = new TableColumn("Endereco");
        colEndereco.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getEndereco().toString()));
        TableColumn<Dentista, String> colTelefone = new TableColumn<Dentista, String>("Telefone");
        colTelefone.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getTelefones().toString()));

        tbDentista.getColumns().setAll(colNome, colCro, colEndereco, colTelefone);

    }

    private void preencherTabela() {
        try {
            for (int i = 0; i < tbDentista.getItems().size(); i++) {
                tbDentista.getItems().clear();
            }
            List<Dentista> aList = (new DentistaDao()).selectAllDentist();
            for (Dentista d : aList) {
                tbDentista.getItems().add(d);
            }
            tbDentista.autosize();
        } catch (DaoException e) {
            e.printStackTrace();
        }

    }

    private boolean isAdm() {
        if (user.getPermissao() != 1) {
            showAlert.showMessageAlert(Alert.AlertType.WARNING, "Permissao necessaria",
                    "Voce nao pode realizar esta acao.",
                    (new Image(this.getClass().getResource("/res/icon.png").toString())));
            return false;
        } else {
            return true;
        }
    }

}
