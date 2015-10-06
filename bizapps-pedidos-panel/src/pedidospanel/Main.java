package pedidospanel;
/*
 * Main.java
 *
 * Created on March 22, 2007, 4:49 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

import gui.Panel;

import java.util.*;
import java.io.*;

/**
 *
 * @author Alirio
 */
public class Main {
  
  /** Creates a new instance of Main */
  public Main() {
  }
  
  /**
   * @param args the command line arguments
   */
  public static void main(String[] args) {
    if (args.length == 0) {
      System.out.println("Usage: PedidosPanel {panel|agent} {vendedores productos clientes ordenes facturas cheques status}");
      System.exit(0);
    }
    
    if (args[0].equals("panel")) {
      if (ConnectionBean.connect()) {
        java.awt.EventQueue.invokeLater(new Runnable() {
          public void run() {
            new Panel().setVisible(true);
          }
        });
      }
    } else {
      if (args[0].equals("agent")) {
        if (ConnectionBean.connect()) {
          
          Set arguments = new HashSet();
          for (Object elem : args) {
            arguments.add((String)elem);
          }
          
          if (arguments.contains("vendedores")) {
            FilesHelper.readSalesmenFile();
            System.out.println("Archivo de vendedores uploaded");
          }
          
          if (arguments.contains("clientes")) {
            FilesHelper.readCustomersFile();
            System.out.println("Archivo de clientes uploaded");
          }
          
          if (arguments.contains("productos")) {
            FilesHelper.readProductsFile();
            System.out.println("Archivo de productos uploaded");
          }
          
          if (arguments.contains("ordenes")) {
            FilesHelper.writeOrderFiles();
            System.out.println("Archivo de Ordenes created");
          }
          
          if (arguments.contains("facturas")) {
            FilesHelper.readEdoCtaFile();
            System.out.println("Archivo de facturas uploaded");
          }
          
          if (arguments.contains("cheques")) {
            FilesHelper.readCheques();
            System.out.println("Archivo de cheques uploaded");
          }
          
          if (arguments.contains("plan")) {
            FilesHelper.readPlan();
            System.out.println("Archivo de plan uploaded");
          }
          
          if (arguments.contains("status")) {
            FilesHelper.readOrderStatus();
            System.out.println("Archivo de status uploaded");
          }
          
          System.out.println("Batch finalizado!");
          ConnectionBean.close();
        }
      } else {
        System.out.println("Usage: PedidosPanel {panel|agent} {vendedores productos clientes ordenes facturas cheques status}");
      }
    }
    
    
    
    
  }
}
