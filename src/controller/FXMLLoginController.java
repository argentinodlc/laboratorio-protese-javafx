package controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.validation.RequiredFieldValidator;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.util.Duration;
import laboratorio.Laboratorio;
import model.bean.Usuario;
import model.dao.DaoException;
import model.dao.UsuarioDao;
import tray.animations.AnimationType;
import tray.notification.NotificationType;
import tray.notification.TrayNotification;
import util.Crypt;
import view.EnumTelas;

public class FXMLLoginController implements Initializable {

    @FXML
    private JFXTextField tfLogin;

    @FXML
    private JFXPasswordField tfSenha;

    @FXML
    private JFXButton btEntrar;

    @FXML
    private JFXButton btNovo;

    @FXML
    void newUser(ActionEvent event) throws IOException {
        clean();
        Laboratorio.changeScreen(EnumTelas.newuser);
    }

    @FXML
    void validaLogin() throws IOException {
        Usuario u = valida();
        System.out.println(u);
        if (u != null) {
            Laboratorio.changeScreen(EnumTelas.principal, u);
            clean();
        } else {
            TrayNotification tray = new TrayNotification("Problema ao entrar", "Login e/ou senha incorretos", NotificationType.ERROR);
            tray.setAnimationType(AnimationType.FADE);
            tray.showAndDismiss(Duration.seconds(1));
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        tfLogin.setLabelFloat(true);
        tfSenha.setLabelFloat(true);
        RequiredFieldValidator validator = new RequiredFieldValidator();
        validator.setMessage("Não deixe este campo em branco");
        RequiredFieldValidator validator2 = new RequiredFieldValidator();
        validator2.setMessage("Não deixe este campo em branco");
        tfLogin.getValidators().add(validator);
        tfLogin.focusedProperty().addListener((o, oldVal, newVal) -> {
            if (!newVal) {
                tfLogin.validate();
            }
        });
        tfSenha.getValidators().add(validator2);
        tfSenha.focusedProperty().addListener((o, oldVal, newVal) -> {
            if (!newVal) {
                tfSenha.validate();
            }
        });
    }

    void clean() {
        tfLogin.setText("");
        tfSenha.setText("");
    }

    Usuario valida() {
        try {
            Usuario u = (new UsuarioDao()).selectUser(tfLogin.getText(), (new Crypt()).encrypt(("quebreioutrocelular"), tfSenha.getText()));
            return u;
        } catch (DaoException ex) {
            Logger.getLogger(FXMLLoginController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception e) {

        }
        return null;
    }
}
