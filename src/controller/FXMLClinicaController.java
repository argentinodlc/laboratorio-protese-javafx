package controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.InputMethodEvent;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.util.Duration;
import laboratorio.Laboratorio;
import model.bean.Clinica;
import model.bean.Dentista;
import model.bean.Usuario;
import model.dao.ClinicaDao;
import model.dao.DaoException;
import model.dao.DentistaDao;
import model.dao.HistoricoDao;
import tray.animations.AnimationType;
import tray.notification.NotificationType;
import tray.notification.TrayNotification;
import util.ActionButtonTableCell;
import util.bytesToImg;
import util.showAlert;
import view.EnumTelas;

public class FXMLClinicaController implements Initializable {

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
    private TableView<Clinica> tbClinica;

    @FXML
    private JFXButton btNovo;

    @FXML
    private JFXButton btAlterar;

    @FXML
    private JFXButton btExcluir;

    @FXML
    private JFXTextField pesquisa;

    @FXML
    private JFXButton btSelecionar;

    @FXML
    void selecionarClinica(ActionEvent event) {
        List list = new ArrayList<>();
        list.add(tbClinica.getSelectionModel().getSelectedItem());
        list.add(user);
        Laboratorio.changeScreen(EnumTelas.principal, list);
    }

    @FXML
    void alterarClinica(ActionEvent event) {
        if (isAdm() && tbClinica.getSelectionModel().getSelectedItem() != null) {
            Clinica c = tbClinica.getSelectionModel().getSelectedItem();
            List l = new ArrayList();
            l.add(user);
            l.add(c);
            Laboratorio.changeScreen(EnumTelas.updateClinica, l);
        }
    }

    @FXML
    void back(ActionEvent event) {
        Laboratorio.changeScreen(EnumTelas.principal, user);
    }

    @FXML
    void changeScreenConfig(ActionEvent event) {
        Laboratorio.changeScreen(EnumTelas.usuarios, user);
    }

    @FXML
    void changeScreenNewClinica(ActionEvent event) {
        if (isAdm()) {
            Laboratorio.changeScreen(EnumTelas.newClinica, user);
        }

    }

    @FXML
    void logout(ActionEvent event) throws IOException {
        Optional<ButtonType> result = showAlert.showConfirmAlert("Sair", "Tem certeza que deseja sair?", (new Image(this.getClass().getResource("/res/icon.png").toString())));
        if (result.get() == ButtonType.OK) {
            Laboratorio.changeScreen(EnumTelas.login);
        }
    }

    @FXML
    void removeClinica(ActionEvent event) {
        if (isAdm()) {
            if (tbClinica.getSelectionModel().getSelectedItem() != null) {
                Optional<ButtonType> result = showAlert.showConfirmAlert("Remover",
                        "Tem certeza que deseja remover " + tbClinica.getSelectionModel().getSelectedItem().getNome() + "?",
                         (new Image(this.getClass().getResource("/res/icon.png").toString())));
                if (result.get() == ButtonType.OK) {
                    try {
                        if ((new ClinicaDao()).deleteClinica(tbClinica.getSelectionModel().getSelectedItem().getCdClinica()) == 1) {
                            Clinica c = tbClinica.getSelectionModel().getSelectedItem();
                            tbClinica.getItems().remove(tbClinica.getSelectionModel().getSelectedItem());
                            TrayNotification tray = new TrayNotification("Clínica removida", "Você removeu a clínica com sucesso", NotificationType.SUCCESS);
                            tray.setAnimationType(AnimationType.FADE);
                            tray.showAndDismiss(Duration.seconds(1));
                            (new HistoricoDao()).insertHistoric("Removeu clínica " + c.getCdClinica() + ": " + c.getNome(), user);
                        } else {
                        }
                    } catch (DaoException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    @FXML
    void updtUser(MouseEvent event) {
        Laboratorio.changeScreen(EnumTelas.updateUser, user);
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

        TableColumn<Clinica, String> colNome = new TableColumn("Nome");
        colNome.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getNome()));
        TableColumn<Clinica, String> colCro = new TableColumn("CNPJ");
        colCro.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getCNPJ()));
        TableColumn<Clinica, String> colEndereco = new TableColumn("Endereco");
        colEndereco.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getEndereco().toString()));
        TableColumn<Clinica, String> colTelefone = new TableColumn<Clinica, String>("Telefone");
        colTelefone.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getTelefones().toString()));
        TableColumn<Clinica, Button> col = new TableColumn<Clinica, Button>("Dentistas");
        col.setCellFactory(ActionButtonTableCell.<Clinica>forTableColumn("Ver dentistas", (Clinica c) -> {
            try {
                List<Dentista> l = (new ClinicaDao()).selectDentists(c.getCdClinica());
                List list = new ArrayList();
                list.add(c);
                list.add(l);
                Stage s = new Stage();
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/FXMLTabela.fxml"));
                loader.setController((new FXMLTabelaController(list)));
                s.setScene((new Scene(loader.load())));
                s.getIcons().add(new Image(getClass().getResource("/res/icon.png").toExternalForm()));
                s.initOwner(Laboratorio.getStage());
                s.initModality(Modality.APPLICATION_MODAL);
                s.showAndWait();
            } catch (DaoException ex) {
                Logger.getLogger(FXMLClinicaController.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(FXMLClinicaController.class.getName()).log(Level.SEVERE, null, ex);
            }
            return c;
        }));

        tbClinica.getColumns().setAll(colNome, colCro, colEndereco, colTelefone, col);

        Laboratorio.addOnChangeScreenListener((EnumTelas screen, Object data) -> {
            if (screen.equals(EnumTelas.clinica)) {
                user = (Usuario) data;
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

                List<Clinica> aList = new ArrayList<Clinica>();
                try {
                    aList = (new ClinicaDao()).selectAllClinica();
                } catch (DaoException e) {
                    e.printStackTrace();
                }
                preencherTabela(aList);
            }
        });
    }

    private void preencherTabela(List<Clinica> clinicas) {
        for (int i = 0; i < tbClinica.getItems().size(); i++) {
            tbClinica.getItems().clear();
        }
        for (Clinica c : clinicas) {
            tbClinica.getItems().add(c);
        }
        tbClinica.autosize();
    }

    @FXML
    void filtrar(KeyEvent event) throws DaoException {
        if (event.getCode() == KeyCode.ENTER) {
            if (pesquisa.getText() != null) {
                try {
                    long codigo = Long.parseLong(pesquisa.getText());
                    Clinica c = (new ClinicaDao()).selectClinicaByCd(codigo);
                    List<Clinica> aList = new ArrayList<Clinica>();
                    aList.add(c);
                    preencherTabela(aList);
                } catch (Exception e) {
                    List<Clinica> aList = (new ClinicaDao()).selectClinicaByNome(pesquisa.getText());
                    preencherTabela(aList);
                }
            } else {
                List<Clinica> aList = new ArrayList<Clinica>();
                try {
                    aList = (new ClinicaDao()).selectAllClinica();
                } catch (DaoException e) {
                    e.printStackTrace();
                }
                preencherTabela(aList);
            }
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
