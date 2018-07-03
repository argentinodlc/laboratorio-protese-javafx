package model.dao;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import util.Config;

public abstract class Dao {
    private static Config conf;
    private static String JDBC_DRIVER;
    private static String DATABASE_URL;
    private static String DATABASE_USER;
    private static String DATABASE_PWD;

    protected final Connection getConnection() throws DaoException {
        carregaConf();
        Statement stmt = null;
        ResultSet rs = null;
        Connection conn = null;
        try {
            Class.forName(JDBC_DRIVER);
            conn = DriverManager.getConnection(DATABASE_URL, DATABASE_USER, DATABASE_PWD);
        } catch (ClassNotFoundException e) {
            throw new DaoException("Erro no getConnection - Biblioteca de acesso ao BD não impotada. " + e.getClass().getName() + " - " + e.getMessage(), e);
        } catch (SQLException e) {
            throw new DaoException("Erro no getConnection - Falha na conexão " + e.getClass().getName() + " - " + e.getMessage(), e);
        } catch (Exception e) {
            throw new DaoException("Erro no getConnection - Falha desconhecida " + e.getClass().getName() + " - " + e.getMessage(), e);
        }
        return conn;

    }

    private static void carregaConf() throws DaoException{
        if (conf==null) {
            try {
                conf = Config.getInstance();
                JDBC_DRIVER = conf.getValue("JDBC_DRIVER");
                System.out.println(JDBC_DRIVER);
                DATABASE_URL = conf.getValue("DATABASE_URL");
                System.out.println(DATABASE_URL);
                DATABASE_USER = conf.getValue("DATABASE_USER");
                System.out.println(DATABASE_USER);
                DATABASE_PWD = conf.getValue("DATABASE_PWD");
                
            } catch (IOException e) {
                throw new DaoException("Errp no carregaConf() - Verifique o arquivo properties " + e.getClass().getName() + " - " + e.getMessage(), e);
            }
        }

    }

    protected void close(Connection conn, PreparedStatement ps, ResultSet rs) {
        if (rs != null) {
            try {
                rs.close();
            } catch (Exception e) {
                //nenhum tratamento a ser feito
            }
        }
        close(conn, ps);
    }

    protected void close(Connection conn, PreparedStatement ps) {
        if (ps != null) {
            try {
                ps.close();
            } catch (Exception e) {
                //nenhum tratamento a ser feito
            }
        }
        close(conn);
    }

    protected void close(Connection conn) {
        if (conn != null) {
            try {
                conn.close();
            } catch (Exception e) {
                //nenhum tratamento a ser feito
            }
        }
    }

}
