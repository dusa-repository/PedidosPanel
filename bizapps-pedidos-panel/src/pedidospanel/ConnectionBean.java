/*
 * ConnectionBean.java
 *
 * Created on March 22, 2007, 4:56 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package pedidospanel;

import java.sql.*;
import java.util.*;
import java.io.*;

/**
 *
 * @author Alirio
 */
public class ConnectionBean {
  
  private static Connection connection;
  
  /** Creates a new instance of ConnectionBean */
  private ConnectionBean() {
  }
  
  public static boolean connect() {
    // Read properties file.
    Properties properties = new Properties();
    try {
      properties.load(new FileInputStream(new File("cfg/bd.properties")));
      String driver = properties.getProperty("driver");
      String url = properties.getProperty("url");
      String user = properties.getProperty("user");
      String pwd = properties.getProperty("pwd");
      
      try {
        Class.forName(driver);
        try {
          connection = DriverManager.getConnection(url, user, pwd);
          return true;
        } catch (SQLException ex) {
          ex.printStackTrace();
          return false;
        }
      } catch (ClassNotFoundException ex) {
        ex.printStackTrace();
        return false;
      }
      
      
    } catch (IOException e) {
      System.out.println(e);
      System.out.println("Error leyendo archivo bd.properties");
      return false;
    }
  }
  
  public static void close() {
    try {
      connection.close();
    } catch (SQLException ex) {
      ex.printStackTrace();
    }
  }
  
  public static Connection getConnection() {
    return connection;
  }
  
}
