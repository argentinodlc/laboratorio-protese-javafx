package controller;

import com.jfoenix.controls.JFXButton;
import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
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
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.util.Duration;
import laboratorio.Laboratorio;
import model.bean.Dentista;
import model.bean.Historico;
import model.bean.Usuario;
import model.dao.DaoException;
import model.dao.HistoricoDao;
import model.dao.UsuarioDao;
import tray.animations.AnimationType;
import tray.notification.NotificationType;
import tray.notification.TrayNotification;
import util.bytesToImg;
import util.showAlert;
import view.EnumTelas;

public class FXMLUsuariosController implements Initializable{
    private Usuario user;
    
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
    private TableView<Usuario> tbUsuarios;
    
    @FXML
    private TableView<Historico> tbHistorico;

    @FXML
    private JFXButton btPermissao;

    @FXML
    private JFXButton btHistorico;

    @FXML
    void selecionarUsuario(ActionEvent event) {
        List list = new ArrayList<>();
        list.add(tbUsuarios.getSelectionModel().getSelectedItem());
        list.add(user);
         Laboratorio.changeScreen(EnumTelas.principal, list);
    }
    
    @FXML
    void back(ActionEvent event) {
         Laboratorio.changeScreen(EnumTelas.principal, user);
    }

    @FXML
    void logout(ActionEvent event) {
         Optional<ButtonType> result = showAlert.showConfirmAlert("Sair", "Tem certeza que deseja sair?", (new Image(this.getClass().getResource("/res/icon.png").toString())));
        if (result.get() == ButtonType.OK){
             try {
                 Laboratorio.changeScreen(EnumTelas.login);
             } catch (IOException ex) {
                 Logger.getLogger(FXMLUsuariosController.class.getName()).log(Level.SEVERE, null, ex);
             }
        }
    }

    @FXML
    void updtUser(MouseEvent event) {
        Laboratorio.changeScreen(EnumTelas.updateUser, user); 
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
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
            if (screen.equals(EnumTelas.usuarios) ){
                user = (Usuario) data;
                preencherTabela();
                if (user.getImagem()!=null) {
                    try {
                        Image img = bytesToImg.toImage(user.getImagem());
                        circle.setFill(new ImagePattern(img));   
                    } catch (IOException ex) {
                        Logger.getLogger(FXMLPrincipalController.class.getName()).log(Level.SEVERE, null, ex);
                    } 
                } else {
                    
                }
                tfNome.setText(user.getNome());
                preencherHistorico();
                preencherTabela();
            }
        });
        TableColumn<Usuario, String> colNome = new TableColumn("Nome");
        colNome.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getNome()));
        TableColumn<Usuario,String> colLogin = new TableColumn("Login");
        colLogin.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getLogin()));
        TableColumn<Usuario,String> colPermissao = new TableColumn("Permissão");
        colPermissao.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().isAdm()));
        
        tbUsuarios.getColumns().setAll(colNome, colLogin, colPermissao);
       
        
        TableColumn<Historico, String> colUser = new TableColumn("Usuario");
        colUser.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getUser().getLogin()));
        TableColumn<Historico,String> colAcao = new TableColumn("Ação");
        colAcao.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getAcao()));
        TableColumn<Historico,String> colData = new TableColumn("Data");
        colData.setCellValueFactory(data -> new SimpleStringProperty((new SimpleDateFormat("dd/MM/yyyy")).format(data.getValue().getData())));
        
        tbHistorico.getColumns().setAll(colUser,colAcao,colData); 
      
    }    

    private void preencherTabela() {
          try {
            for ( int i = 0; i<tbUsuarios.getItems().size(); i++) {
                tbUsuarios.getItems().clear();
            }
            List<Usuario> aList = (new UsuarioDao()).selectAllUsers();
            for (Usuario u: aList) {
                tbUsuarios.getItems().add(u);
            }
            tbUsuarios.autosize();
        } catch (DaoException e) {
            e.printStackTrace();
        }
    }
    
    @FXML
    private void preencherTabelaHistorico() {
        tbHistorico.setVisible(true);
        try {
            for ( int i = 0; i<tbHistorico.getItems().size(); i++) {
                tbHistorico.getItems().clear();
            }
            List<Historico> aList = (new HistoricoDao()).selectHistoricByUser(tbUsuarios.getSelectionModel().getSelectedItem().getCdusuario());
            for (Historico h: aList) {
                tbHistorico.getItems().add(h);
            }
            tbHistorico.autosize();
        } catch (DaoException e) {
            e.printStackTrace();
        }
    }
    
    private void preencherHistorico() {
        tbHistorico.setVisible(true);
        try {
            for ( int i = 0; i<tbHistorico.getItems().size(); i++) {
                tbHistorico.getItems().clear();
            }
            List<Historico> aList = (new HistoricoDao()).selectAllHistoric();
            for (Historico h: aList) {
                tbHistorico.getItems().add(h);
            }
            tbHistorico.autosize();
        } catch (DaoException e) {
            e.printStackTrace();
        }
    }
    
    @FXML
    void changeScreenDentistas(ActionEvent event) {
        Laboratorio.changeScreen(EnumTelas.dentista, user);
    }
    
    @FXML
    void concederPermissao(ActionEvent event) {
        if(isAdm()){
            if (tbUsuarios.getSelectionModel().getSelectedItem()!=null){
                Usuario u = tbUsuarios.getSelectionModel().getSelectedItem();
                if (u.getPermissao()==0) {
                     Optional<ButtonType> result = showAlert.showConfirmAlert("Permissão", "Quer tornar "+u.getNome()+" administrador?", (new Image(this.getClass().getResource("/res/icon.png").toString())));
                     if (result.get() == ButtonType.OK){ 
                            u.setPermissao(1);
                         try {
                            (new UsuarioDao()).updateUser(u);
                            TrayNotification tray = new TrayNotification("Permissão concedida", u.getNome()+" agora é um administrador.", NotificationType.SUCCESS);
                            tray.setAnimationType(AnimationType.FADE);
                            tray.showAndDismiss(Duration.seconds(2));
                            (new HistoricoDao()).insertHistoric("Tornou "+u.getNome()+" administrador", user);
                            showAlert.showMessageAlert(Alert.AlertType.INFORMATION, "Continuar", "Por favor, faça login novamente", (new Image(this.getClass().getResource("/res/icon.png").toString())));
                            Laboratorio.changeScreen(EnumTelas.login);
                         } catch (DaoException ex) {
                            TrayNotification tray = new TrayNotification("Problema ao alterar", ex.getMessage(), NotificationType.ERROR);
                            tray.setAnimationType(AnimationType.FADE);
                            tray.showAndDismiss(Duration.seconds(2));
                         } catch (IOException ex) {
                             Logger.getLogger(FXMLUsuariosController.class.getName()).log(Level.SEVERE, null, ex);
                         }
                     }
                } else {
                    TrayNotification tray = new TrayNotification("Problema ao alterar", "Usuário já possui permissão", NotificationType.WARNING);
                    tray.setAnimationType(AnimationType.FADE);
                    tray.showAndDismiss(Duration.seconds(2));
                }
            }
        }
    }
    
    private boolean isAdm() {
        if (user.getPermissao()!=1){
            showAlert.showMessageAlert(Alert.AlertType.WARNING,"Permissao necessaria", 
                    "Voce nao pode realizar esta acao."
                    , (new Image(this.getClass().getResource("/res/icon.png").toString())));
            return false;
        } else {
            return true;
        }
    }

}
