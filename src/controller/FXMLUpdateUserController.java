package controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.stage.FileChooser;
import javafx.util.Duration;
import laboratorio.Laboratorio;
import model.bean.Usuario;
import model.dao.DaoException;
import model.dao.HistoricoDao;
import model.dao.UsuarioDao;
import tray.animations.AnimationType;
import tray.notification.NotificationType;
import tray.notification.TrayNotification;
import util.Crypt;
import util.bytesToImg;
import util.showAlert;
import view.EnumTelas;

public class FXMLUpdateUserController implements Initializable {
      
    private static Usuario user;
    private static Image image;
    
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
    private Circle circleMenu;

    @FXML
    private JFXButton btSair;

    @FXML
    private JFXButton btTools;

    @FXML
    private JFXButton tfCancelar;

    @FXML
    private JFXButton tfConfirmar;

    @FXML
    private Circle circle;

    @FXML
    private Label lbName;

    @FXML
    private ImageView btLoad;

    @FXML
    private JFXPasswordField tfSenhaAtual;

    @FXML
    private JFXPasswordField tfNovaSenha;

    @FXML
    private JFXPasswordField tfConfirmarNovaSenha;

    @FXML
    private JFXCheckBox checkSenha;
    
    @FXML
    private JFXTextField jfxtfNome;
    
    @FXML
    void liberar(ActionEvent event) {
        if (checkSenha.isSelected()) {
            tfSenhaAtual.setVisible(true);
            tfNovaSenha.setVisible(true);
            tfConfirmarNovaSenha.setVisible(true);
        } else {
            tfSenhaAtual.setVisible(false);
            tfNovaSenha.setVisible(false);
            tfConfirmarNovaSenha.setVisible(false);
            
            tfSenhaAtual.setText("");
            tfNovaSenha.setText("");
            tfConfirmarNovaSenha.setText("");
        }
    }
    
    @FXML
    void back(ActionEvent event) {
        limpa();
        Laboratorio.changeScreen(EnumTelas.principal, user);
    }

    @FXML
    void loadImage(MouseEvent event) {
       FileChooser fileChooser = new FileChooser();
       List<String> ext = new ArrayList<>();
       ext.add("*.png");
       ext.add("*.jpg");
       FileChooser.ExtensionFilter filter = new FileChooser.ExtensionFilter("Imagem (.png, .jpg)", ext);
       fileChooser.setTitle("Escolha uma imagem");
       fileChooser.getExtensionFilters().add(filter);
       File img = fileChooser.showOpenDialog(Laboratorio.stage);
       if (!(img==null)){
            image =  new Image(img.toURI().toString());
            circle.setFill(new ImagePattern (image));
            
       }
    }

    @FXML
    void logout(ActionEvent event) throws IOException {
        Optional<ButtonType> result = showAlert.showConfirmAlert("Sair", "Tem certeza que deseja sair?", (new Image(this.getClass().getResource("/res/icon.png").toString())));
        if (result.get() == ButtonType.OK){
           Laboratorio.changeScreen(EnumTelas.login);
        }
    }
    
    @FXML
    void changeScreenDentista(ActionEvent event) {
        Laboratorio.changeScreen(EnumTelas.dentista, user);
    }
    
    @FXML
    void changeScreenConfig(ActionEvent event) {
        Laboratorio.changeScreen(EnumTelas.usuarios, user);
    }

    @FXML
    void updateUser(ActionEvent event) {
        if (checkSenha.isSelected() && !(jfxtfNome.getText().isEmpty())) {
            try {
                if ((new Crypt()).encrypt("quebreioutrocelular", tfSenhaAtual.getText()).equals(user.getSenha())) {
                    if (tfNovaSenha.getText().equals(tfConfirmarNovaSenha.getText())) {
                        if (image!=null) {
                            user.setImagem(bytesToImg.toBytes(image));
                        }
                        user.setSenha((new Crypt()).encrypt("quebreioutrocelular", tfNovaSenha.getText()));
                        user.setNome(jfxtfNome.getText());
                        (new UsuarioDao()).updateUser(user);
                        TrayNotification tray = new TrayNotification("Usuário alterado", "Você alterou sua conta com sucesso", NotificationType.SUCCESS);
                        tray.setAnimationType(AnimationType.FADE);
                        tray.showAndDismiss(Duration.seconds(2));
                        newHistoric();
                        back(new ActionEvent());
                    } else {
                        TrayNotification tray = new TrayNotification("Problema ao alterar", "Senhas não batem", NotificationType.ERROR);
                        tray.setAnimationType(AnimationType.FADE);
                        tray.showAndDismiss(Duration.seconds(2));
                    }
                } else {
                    TrayNotification tray = new TrayNotification("Problema ao alterar", "Senha atual não bate", NotificationType.ERROR);
                    tray.setAnimationType(AnimationType.FADE);
                    tray.showAndDismiss(Duration.seconds(2));
                }
            } catch (DaoException dao) {
                TrayNotification tray = new TrayNotification("Problema ao alterar", "Ocorreu algum problema no banco de dados", NotificationType.ERROR);
                tray.setAnimationType(AnimationType.FADE);
                tray.showAndDismiss(Duration.seconds(2));
            } catch (Exception e) {
                TrayNotification tray = new TrayNotification("Problema ao alterar", "Ocorreu algum problema com sua senha\n"+e.getClass().getName(), NotificationType.ERROR);
                tray.setAnimationType(AnimationType.FADE);
                tray.showAndDismiss(Duration.seconds(2));
            }
        } else {
            if (image!= null && !(jfxtfNome.getText().isEmpty())) {
               try {
                    user.setImagem(bytesToImg.toBytes(image)); 
                    user.setNome(jfxtfNome.getText());
                    (new UsuarioDao()).updateUser(user);
                    TrayNotification tray = new TrayNotification("Usuário alterado", "Você alterou sua conta com sucesso", NotificationType.SUCCESS);
                    tray.setAnimationType(AnimationType.FADE);
                    tray.showAndDismiss(Duration.seconds(2));
                    newHistoric();
                    back(new ActionEvent());
            } catch (DaoException dao) {
                TrayNotification tray = new TrayNotification("Problema ao alterar", "Ocorreu algum problema no banco de dados", NotificationType.ERROR);
                tray.setAnimationType(AnimationType.FADE);
                tray.showAndDismiss(Duration.seconds(2));
            } catch (Exception e) {
                TrayNotification tray = new TrayNotification("Problema ao alterar", "Ocorreu algum problema com sua imagem\n"+e.getClass().getName(), NotificationType.ERROR);
                tray.setAnimationType(AnimationType.FADE);
                tray.showAndDismiss(Duration.seconds(2));
            }
            } else {
                TrayNotification tray = new TrayNotification("Problema ao alterar", "Não há nada a ser alterado", NotificationType.ERROR);
                tray.setAnimationType(AnimationType.FADE);
                tray.showAndDismiss(Duration.seconds(2));
            }
        }
    }
    
    private void newHistoric() {
        try {
            (new HistoricoDao()).insertHistoric("Alterou dados", user);
        } catch (DaoException e) {
            TrayNotification tray = new TrayNotification("Problema ao alterar", "Ocorreu algum problema com o banco de dados", NotificationType.ERROR);
            tray.setAnimationType(AnimationType.FADE);
            tray.showAndDismiss(Duration.seconds(2));
            e.printStackTrace();
        }
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
        circleMenu.setFill(new ImagePattern(imageUser));
        
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
            if (screen.equals(EnumTelas.updateUser) ){
                user = (Usuario) data;
                if (user.getImagem()!=null) {
                    try {
                        Image img = bytesToImg.toImage(user.getImagem());
                        circle.setFill(new ImagePattern(img));
                        circleMenu.setFill(new ImagePattern(img));
                        image = img;
                    } catch (IOException ex) {
                        Logger.getLogger(FXMLPrincipalController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                } else {
                    
                }  
                jfxtfNome.setText(user.getNome());
                lbName.setText(user.getNome());
            }
        });
        tfSenhaAtual.setVisible(false);
        tfConfirmarNovaSenha.setVisible(false);
        tfNovaSenha.setVisible(false);
    } 

    private void limpa() {
        lbName.setText("");
        tfSenhaAtual.setText("");
        tfNovaSenha.setText("");
        tfConfirmarNovaSenha.setText("");
        checkSenha.setSelected(false);
        liberar(null);
    }
    
}

