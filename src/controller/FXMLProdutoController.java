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
import javafx.fxml.Initializable;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.InputMethodEvent;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.util.Duration;
import laboratorio.Laboratorio;
import model.bean.Clinica;
import model.bean.Dentista;
import model.bean.Produto;
import model.bean.Usuario;
import model.dao.ClinicaDao;
import model.dao.DaoException;
import model.dao.DentistaDao;
import model.dao.HistoricoDao;
import model.dao.ProdutoDao;
import tray.animations.AnimationType;
import tray.notification.NotificationType;
import tray.notification.TrayNotification;
import util.bytesToImg;
import util.showAlert;
import view.EnumTelas;

public class FXMLProdutoController implements Initializable {

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
    private TableView<Produto> tbProduto;

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
        list.add(tbProduto.getSelectionModel().getSelectedItem());
        list.add(user);
        Laboratorio.changeScreen(EnumTelas.principal, list);
    }

    @FXML
    void alterarClinica(ActionEvent event) {
        if (tbProduto.getSelectionModel().getSelectedItem()!=null){
        List l = new ArrayList();
        l.add(user);
        l.add(tbProduto.getSelectionModel().getSelectedItem());
        if (isAdm())
            Laboratorio.changeScreen(EnumTelas.updateProduto, l);
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
            Laboratorio.changeScreen(EnumTelas.newProduto, user);
        }
    }

    @FXML
    void logout(ActionEvent event) throws IOException {
        if (showAlert.showConfirmAlert("Sair", "Tem certeza que deseja sair?", (new Image(this.getClass().getResource("/res/icon.png").toString()))).get() == ButtonType.OK){
           Laboratorio.changeScreen(EnumTelas.login);
        }
    }

    @FXML
    void removeClinica(ActionEvent event) {
        if (isAdm()) {
            if (tbProduto.getSelectionModel().getSelectedItem() != null) {
                Optional<ButtonType> result = showAlert.showConfirmAlert("Remover",
                        "Tem certeza que deseja remover " + tbProduto.getSelectionModel().getSelectedItem().getNome() + "?",
                         (new Image(this.getClass().getResource("/res/icon.png").toString())));
                if (result.get() == ButtonType.OK) {
                    try {
                        if ((new ProdutoDao()).deleteProduto(tbProduto.getSelectionModel().getSelectedItem().getCdProduto()) == 1) {
                            Produto p = tbProduto.getSelectionModel().getSelectedItem();
                            tbProduto.getItems().remove(tbProduto.getSelectionModel().getSelectedItem());
                            TrayNotification tray = new TrayNotification("Produto removida", "Você removeu o produto com sucesso", NotificationType.SUCCESS);
                            tray.setAnimationType(AnimationType.FADE);
                            tray.showAndDismiss(Duration.seconds(1));
                            (new HistoricoDao()).insertHistoric("Removeu produto" + p.getCdProduto()+ ": " + p.getNome(), user);
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
            if (screen.equals(EnumTelas.produto)) {
                user = (Usuario) data;
                if (user.getImagem() != null) {
                    try {
                        Image img = bytesToImg.toImage(user.getImagem());
                        circle.setFill(new ImagePattern(img));
                    } catch (IOException ex) {
                        Logger.getLogger(FXMLPrincipalController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                tfNome.setText(user.getNome());
                List<Produto> aList = new ArrayList<Produto>();
                try {
                    aList = (new ProdutoDao()).selectProduto();
                } catch (DaoException e) {
                    e.printStackTrace();
                }
                preencherTabela(aList);
            }
        });
        TableColumn<Produto, String> colNome = new TableColumn("Nome");
        colNome.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getNome()));
        TableColumn<Produto, String> colValor = new TableColumn("Valor");
        colValor.setCellValueFactory(data -> new SimpleStringProperty("R$ " + String.valueOf(data.getValue().getValor())));

        tbProduto.getColumns().setAll(colNome, colValor);

    }

    private void preencherTabela(List<Produto> produtos) {
        for (int i = 0; i < tbProduto.getItems().size(); i++) {
            tbProduto.getItems().clear();
        }
        for (Produto p : produtos) {
            tbProduto.getItems().add(p);
        }
        tbProduto.autosize();
    }

    @FXML
    void filtrar(KeyEvent event) throws DaoException {
        if (event.getCode() == KeyCode.ENTER) {
            if (pesquisa.getText() != null) {
                try {
                    long codigo = Long.parseLong(pesquisa.getText());
                    Produto p = (new ProdutoDao()).selectProdutoByCd(codigo);
                    List<Produto> aList = new ArrayList<Produto>();
                    aList.add(p);
                    preencherTabela(aList);
                } catch (Exception e) {
                    List<Produto> aList = (new ProdutoDao()).selectProdutoByName(pesquisa.getText());
                    preencherTabela(aList);
                }
            } else {
                List<Produto> aList = new ArrayList<Produto>();
                aList = (new ProdutoDao()).selectProduto();
                preencherTabela(aList);
            }
        }
    }

    private boolean isAdm() {
        return user.getPermissao() == 1;
    }

}
