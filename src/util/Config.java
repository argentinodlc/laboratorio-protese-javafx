/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import java.io.IOException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
/**
 *
 * @author filip
 */
public class Config {
    private static Config config;
    private Properties prop;
    private final String CONFIG = "config.properties";
    
    private Config() throws IOException{
        prop = new Properties();
        System.out.println("CRIOU O PROP");
        prop.load(getClass().getResourceAsStream(CONFIG));
        System.out.println("CARREGOU O PROP");
    };
    
    public static Config getInstance() throws IOException{
        if (config==null)
            return (config = new Config());
        else
            return config;
    }
    
    public String getValue(String key) {
        return prop.getProperty(key);
    }
    
    public static void main(String[] args) {
        try {
            Config c = Config.getInstance();
            System.out.println(c.getValue("CHAVE"));
        } catch (IOException ex) {
            Logger.getLogger(Config.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
