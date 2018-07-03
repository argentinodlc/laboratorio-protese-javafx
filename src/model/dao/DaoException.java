package model.dao;

public class DaoException extends Exception {
    public DaoException(String s) {
        super(s);
    }
    
     public DaoException(String s, Throwable e) {
        super(s, e);
    }
}
