/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.util.Duration;
import laboratorio.Laboratorio;
import model.dao.DaoException;
import model.dao.UsuarioDao;
import tray.animations.AnimationType;
import tray.notification.NotificationType;
import tray.notification.TrayNotification;
import util.Crypt;
import util.bytesToImg;
import util.showAlert;
import view.EnumTelas;

/**
 * FXML Controller class
 *
 * @author filip
 */
public class FXMLNewUserController implements Initializable {
    private Image image;
     @FXML
    private JFXButton btEnviarImagem;
    
    @FXML
    void enviarImagem(ActionEvent event) throws IOException, DaoException {
       FileChooser fileChooser = new FileChooser();
       List<String> ext = new ArrayList<>();
       ext.add("*.png");
       ext.add("*.jpg");
       ExtensionFilter filter = new ExtensionFilter("Imagem (.png, .jpg)", ext);
       fileChooser.setTitle("Escolha uma imagem");
       fileChooser.getExtensionFilters().add(filter);
       File img = fileChooser.showOpenDialog(Laboratorio.stage);
       if (!(img==null)){
            fillCircle(img);
            image =  new Image(img.toURI().toString());
       }
    }
    
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
         Image img = new Image(getClass().getResource("/res/Steam.jpg").toExternalForm(), 100, 100, true, true);
         try {
             fillCircle(img);
         } catch (DaoException ex) {
             Logger.getLogger(FXMLNewUserController.class.getName()).log(Level.SEVERE, null, ex);
         } catch (IOException ex) {
             Logger.getLogger(FXMLNewUserController.class.getName()).log(Level.SEVERE, null, ex);
         }
        Image folderupload = new Image(getClass().getResource("/res/folder-upload.png").toExternalForm(), 100, 100, true, true);
        ImageView folder = new ImageView(folderupload);

	folder.setFitWidth(32);

	folder.setFitHeight(32);

	 
        btEnviarImagem.setGraphic(folder);
        image = null;
        
        tfNome.setAlignment(Pos.CENTER_LEFT);
        tfLogin.setAlignment(Pos.CENTER_LEFT);
      
    }    
    private void fillCircle(File file){
        Image img = new Image(file.toURI().toString());
        imagem.setFill(new ImagePattern (img));
    }
    
    private void fillCircle(Image img) throws DaoException, IOException{
        imagem.setFill(new ImagePattern (img));
    }
    @FXML
    private Circle imagem;
    
     @FXML
    private JFXPasswordField tfSenha;

    @FXML
    private JFXPasswordField tfConfirmarSenha;
   
    @FXML
    private JFXTextField tfNome;

    @FXML
    private JFXTextField tfLogin;
    
    @FXML
    private JFXButton btCancelar;

    @FXML
    private JFXButton btCadastrar;

    @FXML
    void cadastrar(ActionEvent event) throws IOException, DaoException {
        String nome = tfNome.getText();
        String login = tfLogin.getText();
        String senha = tfSenha.getText();
        String senhaConfirmada = tfConfirmarSenha.getText();
        if ( nome.length() > 0 && login.length() > 0 ) {
            if (senha.equals(senhaConfirmada)) {
                try {
                if (image != null) { 
                   if ((new UsuarioDao()).insertUser(nome, login, (new Crypt()).encrypt(("quebreioutrocelular"), senha), 0, bytesToImg.toBytes(image)) != -1){
                     
                       TrayNotification tray = new TrayNotification("Usuário cadastrado", "Usuário cadastrado", NotificationType.SUCCESS);
                       tray.setAnimationType(AnimationType.FADE);
                        tray.showAndDismiss(Duration.seconds(2));
                        clean();
                        Laboratorio.changeScreen(EnumTelas.login);
                   }
                } else { 
                   if ((new UsuarioDao()).insertUser(nome, login, (new Crypt()).encrypt(("quebreioutrocelular"), senha), 0, null)!= -1){
                      
                       TrayNotification tray = new TrayNotification("Usuário cadastrado", "Usuário cadastrado", NotificationType.SUCCESS);
                       tray.setAnimationType(AnimationType.FADE);
                       tray.showAndDismiss(Duration.seconds(2));
                       clean();
                       Laboratorio.changeScreen(EnumTelas.login);
                   };
                 }         
                } catch (DaoException dao){
                
                } catch (Exception e) {
                    
                }
            } else {
                showAlert.showMessageAlert(Alert.AlertType.WARNING, "Problema ao cadastrar", "As senhas não conferem", (new Image(this.getClass().getResource("/res/icon.png").toString())));
            }
        } else {
            showAlert.showMessageAlert(Alert.AlertType.WARNING, "Problema ao cadastrar", "Preencha os campos corretamente", (new Image(this.getClass().getResource("/res/icon.png").toString())));
        }
    }

    @FXML
    void cancelar(ActionEvent event) throws IOException {
        clean();
        Laboratorio.changeScreen(EnumTelas.login);
    }

    void clean(){
        tfNome.setText("");
        tfLogin.setText("");
        tfSenha.setText("");
        tfConfirmarSenha.setText("");
        Image img = new Image(getClass().getResource("/res/Steam.jpg").toExternalForm(), 100, 100, true, true);
         try {
             fillCircle(img);
         } catch (DaoException ex) {
             
         } catch (IOException ex) {
             
         }
    }
    
}
