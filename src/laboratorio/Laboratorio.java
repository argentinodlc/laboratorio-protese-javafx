/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package laboratorio;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.Region;
import javafx.stage.Stage;
import javafx.util.Duration;
import tray.animations.AnimationType;
import tray.notification.NotificationType;
import tray.notification.TrayNotification;
import view.EnumTelas;

/**
 *
 * @author filip
 */
public class Laboratorio extends Application {
    
    public static Stage stage = new Stage();
    private static Scene sceneLogin;
    private static Scene sceneNewUser;
    private static Scene scenePrincipal;
    private static Scene sceneUpdtUser;   
    private static Scene sceneDentista;   
    private static Scene sceneNewDentist;   
    private static Scene sceneUpdateDentist;
    private static Scene sceneUsuarios;
    private static Scene sceneClinica;
    private static Scene sceneProduto;
    private static Scene sceneNewClinica;
    private static Scene sceneUpdateClinica;
    private static Scene sceneNewProduto;
    private static Scene sceneUpdateProduto;
    
    public static Stage getStage(){
        return stage;
    }
       
    public static void main(String[] args) {
        launch(args);
    }
    
    @Override
    public void start(Stage primaryStage) throws IOException {
        stage = primaryStage;
        //configuracoes do stage
        primaryStage.setTitle("Sistema Dent Art");
        primaryStage.setMinHeight(760);
        primaryStage.setMinWidth(1024);
        primaryStage.getIcons().add(new Image(getClass().getResource("/res/icon.png").toExternalForm()));
            
        //Tela login
        Region fxmlLogin = FXMLLoader.load(getClass().getResource("/view/FXMLLogin.fxml"));
        sceneLogin = new Scene(fxmlLogin);
        
        //tela new user
        Region fxmlnewuser=  FXMLLoader.load(getClass().getResource("/view/FXMLNewUser.fxml"));
        sceneNewUser = new Scene(fxmlnewuser);
        
        //Tela principal
        Region fxmlPrincipal=  FXMLLoader.load(getClass().getResource("/view/FXMLPrincipal.fxml"));
        scenePrincipal = new Scene(fxmlPrincipal);
        
        //Tela updateUser
        Region fxmlupdtUser = FXMLLoader.load(getClass().getResource("/view/FXMLUpdateUser.fxml"));
        sceneUpdtUser = new Scene(fxmlupdtUser);
        
        //Tela dentista
        Region fxmlDentista = FXMLLoader.load(getClass().getResource("/view/FXMLDentista.fxml"));
        sceneDentista = new Scene(fxmlDentista);
        
        //Tela novo dentista
        Region fxmlNewDentist = FXMLLoader.load(getClass().getResource("/view/FXMLNewDentist.fxml"));
        sceneNewDentist = new Scene(fxmlNewDentist);
        
        //Tela alterar dentista
        Region fxmlUpdateDentist = FXMLLoader.load(getClass().getResource("/view/FXMLAlterarDentista.fxml"));
        sceneUpdateDentist = new Scene(fxmlUpdateDentist);
        
        //Tela usuarios
        Region fxmlUsuarios = FXMLLoader.load(getClass().getResource("/view/FXMLUsuarios.fxml"));
        sceneUsuarios = new Scene(fxmlUsuarios);
        
        //Tela clinica
        Region fxmlClinica = FXMLLoader.load(getClass().getResource("/view/FXMLClinica.fxml"));
        sceneClinica = new Scene(fxmlClinica);

        //Tela produto
        Region fxmlProduto = FXMLLoader.load(getClass().getResource("/view/FXMLProduto.fxml"));
        sceneProduto = new Scene(fxmlProduto);
        
         //Tela nova clinica
        Region fxmlNewClinica = FXMLLoader.load(getClass().getResource("/view/FXMLNewClinic.fxml"));
        sceneNewClinica = new Scene(fxmlNewClinica);
        
        //Tela Alterar clinica
        Region fxmlUpdateClinica = FXMLLoader.load(getClass().getResource("/view/FXMLAlterarClinica.fxml"));
        sceneUpdateClinica = new Scene(fxmlUpdateClinica);
        
         //Tela novo produto
        Region fxmlNewProduto = FXMLLoader.load(getClass().getResource("/view/FXMLNewProduto.fxml"));
        sceneNewProduto = new Scene(fxmlNewProduto);   
        
         //Tela Alterar produto
        Region fxmlUpdateProduto = FXMLLoader.load(getClass().getResource("/view/FXMLUpdateProduto.fxml"));
        sceneUpdateProduto = new Scene(fxmlUpdateProduto);   

        //seta tela de login
        primaryStage.setScene(sceneLogin);
        primaryStage.show();
    }
    
    public static void changeScreen(EnumTelas screen, Object data){
        try {
            Stage previousState = stage;
            switch (screen){  
                case login:
                    stage.setScene(sceneLogin);
                    notifyAllListeners(EnumTelas.login, data);
                    break;
                case newuser:
                    stage.setScene(sceneNewUser);
                    notifyAllListeners(EnumTelas.newuser, data);
                    break;         
                case principal:
                    stage.setScene(scenePrincipal);
                    notifyAllListeners(EnumTelas.principal, data);
                    break;
                case updateUser:
                    stage.setScene(sceneUpdtUser);
                    notifyAllListeners(EnumTelas.updateUser, data);
                    break;
                case dentista:
                    stage.setScene(sceneDentista);
                    notifyAllListeners(EnumTelas.dentista, data);
                    break;
                case newDentist:
                    stage.setScene(sceneNewDentist);
                    notifyAllListeners(EnumTelas.newDentist, data);
                    break;
                case updateDentist:
                    stage.setScene(sceneUpdateDentist);
                    notifyAllListeners(EnumTelas.updateDentist, data);
                    break;
                case usuarios:
                    stage.setScene(sceneUsuarios);
                    notifyAllListeners(EnumTelas.usuarios, data);
                    break;
                case clinica:
                    stage.setScene(sceneClinica);
                    notifyAllListeners(EnumTelas.clinica, data);
                    break;
                case produto:
                    stage.setScene(sceneProduto);
                    notifyAllListeners(EnumTelas.produto, data);
                    break;
                case newClinica:
                    stage.setScene(sceneNewClinica);
                    notifyAllListeners(EnumTelas.newClinica, data);
                    break;
                case updateClinica:
                    stage.setScene(sceneUpdateClinica);
                    notifyAllListeners(EnumTelas.updateClinica, data);
                    break;
                case newProduto:
                    stage.setScene(sceneNewProduto);
                    notifyAllListeners(EnumTelas.newProduto, data);
                    break;
                case updateProduto:
                    stage.setScene(sceneUpdateProduto);
                    notifyAllListeners(EnumTelas.updateProduto, data);
                    break;
            }
//            resizeScreen(previousState);
        } catch (Exception e) {
              TrayNotification tray = new TrayNotification("Ocorreu um problema", "O sistema não conseguiu ir para "+screen+",\n "+e.getClass().getName(), NotificationType.ERROR);
              tray.setAnimationType(AnimationType.FADE);
              tray.showAndDismiss(Duration.seconds(2));
              e.printStackTrace();
        }
    }
    
     public static void changeScreen(EnumTelas screen) throws IOException{
        changeScreen(screen, null);
    }
    
//      private static void resizeScreen(Stage previousState) {
//       if (previousState.isFullScreen()) 
//           stage.setFullScreen(true);
//       else if (previousState.isMaximized())
//           stage.setMaximized(true);
//       else {
//           stage.setWidth(previousState.getWidth());
//           stage.setHeight(previousState.getHeight());
//       } 
//    }
    
    private static List<OnChangeScreen> listeners = new ArrayList<>();
    
    public static interface OnChangeScreen {
        void onScreenChange(EnumTelas screen, Object data);
    }
    
    public static void addOnChangeScreenListener(OnChangeScreen newListener) {
        listeners.add(newListener);
    }
    
    private static void notifyAllListeners(EnumTelas screen, Object data){
       for (OnChangeScreen l : listeners)
           l.onScreenChange(screen, data);
    }
    
}
