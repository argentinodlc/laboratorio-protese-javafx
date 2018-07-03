/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import javax.imageio.ImageIO;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import javafx.scene.paint.ImagePattern;
import model.bean.Usuario;
import model.dao.UsuarioDao;

/**
 *
 * @author filip
 */
public class bytesToImg {
    public static byte[] toBytes(Image img) throws IOException {
            BufferedImage image = SwingFXUtils.fromFXImage(img, null);
            ByteArrayOutputStream bytesImg = new ByteArrayOutputStream();
            ImageIO.write((BufferedImage)image, "jpg", bytesImg);//seta a imagem para bytesImg
            bytesImg.flush();//limpa a variável
            byte[] byteArray = bytesImg.toByteArray();//Converte ByteArrayOutputStream para byte[] 
            bytesImg.close();//fecha a conversão
            return byteArray;
    }
    
    public static Image toImage(byte[] byteArray) throws IOException {
        BufferedImage image = ImageIO.read(new ByteArrayInputStream(byteArray));
        return SwingFXUtils.toFXImage(image, null);
    }
}
