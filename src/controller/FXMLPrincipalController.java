/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
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
import laboratorio.Laboratorio;
import model.bean.Clinica;
import model.bean.Dentista;
import model.bean.Produto;
import model.bean.Usuario;
import util.bytesToImg;
import util.showAlert;
import view.EnumTelas;

/**
 * FXML Controller class
 *
 * @author filip
 */
public class FXMLPrincipalController implements Initializable {
    private static Usuario user;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        tfCdClinica.setLabelFloat(true);
        tfClinica.setLabelFloat(true);
        tfCdDentista.setLabelFloat(true);
        tfDentista.setLabelFloat(true);
        tfUsuario.setLabelFloat(true);
        tfCdUsuario.setLabelFloat(true);
        tfCdHistorico.setLabelFloat(true);
        tfHistorico.setLabelFloat(true);
        
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
            if (screen.equals(EnumTelas.principal) ){
                if (data instanceof List){
                    Object selected = ((List) data).get(0);
                    if (selected instanceof Usuario) {
                        tfCdUsuario.setText(String.valueOf(((Usuario) selected).getCdusuario()));
                        tfUsuario.setText(((Usuario) selected).getNome());
                    } else {
                        if (selected instanceof Dentista) {
                            tfCdDentista.setText(String.valueOf(((Dentista) selected).getCdDentista()));
                            tfDentista.setText(((Dentista) selected).getNome());
                        } else {
                            if (selected instanceof Clinica) {
                                tfCdClinica.setText(String.valueOf(((Clinica) selected).getCdClinica()));
                                tfClinica.setText(((Clinica) selected).getNome());
                            } else {
                                tfCdHistorico.setText(String.valueOf(((Produto) selected).getCdProduto()));
                                tfHistorico.setText(((Produto) selected).getNome());
                            }
                        }
                    }
                    data = ((List) data).get(1);
                }
                user = (Usuario) data;
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
            }
        });
        
    }   
    
    @FXML
    void logout(ActionEvent event) throws IOException {
        Optional<ButtonType> result = showAlert.showConfirmAlert("Sair", "Tem certeza que deseja sair?", (new Image(this.getClass().getResource("/res/icon.png").toString())));
        if (result.get() == ButtonType.OK){
           Laboratorio.changeScreen(EnumTelas.login);
        }
    }
    
    @FXML
    void updtUser(MouseEvent event) {
        Laboratorio.changeScreen(EnumTelas.updateUser, user);
    }
   
    //MUDAR PARA DENTISTA
    @FXML
    void changeScreenDentista(ActionEvent event) {
        Laboratorio.changeScreen(EnumTelas.dentista, user);
    }
    
    @FXML
    void changeScreenProduto(ActionEvent event) {
        Laboratorio.changeScreen(EnumTelas.produto, user);
    }
    
    @FXML
    void changeScreenClinica(ActionEvent event) {
        Laboratorio.changeScreen(EnumTelas.clinica, user);
    }
    
    @FXML
    void changeScreenConfig(ActionEvent event) {
        Laboratorio.changeScreen(EnumTelas.usuarios, user);
    }
    
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
    private JFXButton btSair;
    
    @FXML
    private JFXButton btTools;
    
    @FXML
    private ImageView btUpdtUser;
    
    @FXML
    private JFXTextField tfCdUsuario;

    @FXML
    private JFXTextField tfUsuario;

    @FXML
    private JFXTextField tfCdHistorico;

    @FXML
    private JFXTextField tfHistorico;

    @FXML
    private JFXTextField tfCdDentista;

    @FXML
    private JFXTextField tfDentista;

    @FXML
    private JFXTextField tfCdClinica;

    @FXML
    private JFXTextField tfClinica;

    @FXML
    private JFXButton btUsuario;

    @FXML
    private JFXButton btHistorico;

    @FXML
    private JFXButton btDentista;

    @FXML
    private JFXButton btClinica;

}
