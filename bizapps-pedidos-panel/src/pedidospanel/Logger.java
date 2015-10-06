/*
 * Logger.java
 *
 * Created on April 26, 2007, 10:25 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package pedidospanel;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

/**
 *
 * @author Alirio
 */
public class Logger {
  
  private BufferedWriter out; 
  
  /** Creates a new instance of Logger */
  public Logger(String fileName) {
    try {
      out = new BufferedWriter(new FileWriter("files/" + fileName + ".log"));
    } catch (IOException ex) {
      ex.printStackTrace();
    }
  }
  
  public void log(String msg) {
    try {
      out.write(msg + System.getProperty("line.separator"));
    } catch (IOException ex) {
      ex.printStackTrace();
    }
  }
  
  public void close() {
    try {
      out.close();
    } catch (IOException ex) {
      ex.printStackTrace();
    }
  }
}
