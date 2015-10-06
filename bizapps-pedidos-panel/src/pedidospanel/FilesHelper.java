/*
 * FilesHelper.java
 *
 * Created on March 22, 2007, 4:54 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor...
 */

package pedidospanel;

import java.io.*;
import java.sql.*;
import java.text.*;
import java.util.*;

/**
 *
 * @author Alirio
 */
public class FilesHelper {
  
  /** Creates a new instance of FilesHelper */
  public FilesHelper() {
  }
  
  /* Elimina los apostrofes encontrados en las descripciones */
  private static String parseSQLDescription(String desc) {
    return desc.replaceAll("'", "´");
  }
  
  /* Devuelve un numero formateado (0.00) desde una cadena cuyos decimales estan al final */
  private static String parseFloat(String num, int dec) {
    StringBuilder sb = new StringBuilder(num);
    StringBuilder sb2 = new StringBuilder();
    sb2.append(sb.substring(0, sb.length() - dec));
    sb2.append(".");
    sb2.append(sb.substring(sb.length() - dec, sb.length()));
    return sb2.toString();
  }
  
  /* Devuelve un numero formateado (0.00) desde una cadena cuyo separador decimal es una coma */
  private static String parseFloat2(String num) {
    StringTokenizer st = new StringTokenizer(num, ",");
    if (st.countTokens() == 2) {
      return st.nextToken().trim() + "." + st.nextToken().trim();
    } else {
      return st.nextToken().trim();
    }
  }
  
  /* Formatea el numero de la orden */
  private static String fillOrderNumber(int order) {
    NumberFormat formatter = new DecimalFormat("00000000");
    String s = formatter.format(order);
    return s;
  }
  
  /* rellena el codigo del cliente */
  private static String fillOrderCustomer(String customer) {
    String result = customer;
    if (customer.length() < 8) {
      int len = customer.length();
      for (int i=1; i <= (8 - len); i++ ) {
        result = "0" + result; //OJO PREGUNTAR
      }
    }
    return result;
  }
  
  private static String fillOrderComments(String customer) {
    String result = customer;
    if (customer.length() < 150) {
      int len = customer.length();
      for (int i=1; i <= (150 - len); i++ ) {
        result = " " + result; //OJO PREGUNTAR
      }
    }
    return result;
  }
  
  /* rellena el codigo del almacen */
  private static String fillOrderWarehouse(String w) {
    String result = w;
    if (w.length() < 12) {
      int len = w.length();
      for (int i=1; i <= (12 - len); i++ ) {
        result = " " + result; //OJO PREGUNTAR
      }
    }
    return result;
  }
  
  /* rellena el codigo del producto */
  private static String fillOrderProduct(String product) {
    String result = product;
    if (product.length() < 10) {
      int len = product.length();
      for (int i=1; i <= (10 - len); i++ ) {
        result = "0" + result; //OJO PREGUNTAR
      }
    }
    return result;
  }
  
  /* formatea los comentarios */
  private static String formatComments(String comments) {
    String result = comments;
    if (result.length() < 120) {
      result = result + spaces(120 - comments.length());
    }
    return result;
  }
  
  /* formatea la cantidad con espacios en blanco que viene en doble*/
  private static String fillOrderQuantity(double qty) {
    NumberFormat formatter = new DecimalFormat("0");
    String qtyStr = formatter.format(qty);
    String result = qtyStr;
    if (qtyStr.length() < 8) {
      int len = qtyStr.length();
      for (int i=1; i <= (8 - len); i++ ) {
        result = " " + result;
      }
    }
    return result;
  }
  
  /* function para devolver espacios en blanco */
  private static String spaces(int spaces) {
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < spaces; i++) {
      sb.append(" ");
    }
    return sb.toString();
  }
  
  public static String formatStringDate(String dateYyyyMmDd) {
    return (dateYyyyMmDd.substring(6,8) + "/" + dateYyyyMmDd.substring(4, 6) + "/" + dateYyyyMmDd.substring(0, 4));
  }
  
  public static String formatStringDate2(String dateYyyyMmDd) {
    return (dateYyyyMmDd.substring(6,8) + dateYyyyMmDd.substring(4, 6) + dateYyyyMmDd.substring(0, 4));
  }
  
  public static String formatStringDate3(String dateDdMmYyyy) {
    return (dateDdMmYyyy.substring(0,2) + "/" + dateDdMmYyyy.substring(2, 4) + "/" + dateDdMmYyyy.substring(4, 8));
  }
  
  /* convierte una hora en formato hh:mm:ss -> hhmmss */
  public static String formatStringTime(String time) {
    return (time.substring(0,2) + time.substring(3, 5) + time.substring(6, 8));
  }
  
  /* METODOS QUE SUBEN/BAJAN LOS ARCHIVOS AL/DEL SERVER */
  
  
  /*********************/
  /* depuracion / historico */
  /*********************/
  
  public static void debug() {
    Statement stmt;
    Statement stmt2;
    Statement stmt3;
    Statement stmt4;
    ResultSet rs;
    ResultSet rs2;
    
    /* instanciamos el logger */
    Logger logger = new Logger("historical");
    
    try {
      
      /* creamos el statement */
      stmt = ConnectionBean.getConnection().createStatement();
      stmt2 = ConnectionBean.getConnection().createStatement();
      stmt3 = ConnectionBean.getConnection().createStatement();
      stmt4 = ConnectionBean.getConnection().createStatement();
      rs = stmt.executeQuery("select * from orders where status='PRO'");
      while(rs.next()) {
        long orderId = rs.getLong("order_id");
        String sql = "insert into orders_history (order_id, order_date, order_time, salesman_id, customer_id, delivery_date, status, amount) " +
            "values(" + orderId + ",'" + rs.getString("order_date") + "','" + rs.getString("order_time") + "'," +
            "'" + rs.getString("salesman_id") + "','" + rs.getString("customer_id") + "','" + rs.getString("delivery_date") + "'," +
            "'" + rs.getString("status") + "'," + rs.getDouble("amount") + ")";
        stmt2.executeUpdate(sql);
        
        rs2 = stmt3.executeQuery("select * from orders_details where order_id=" + orderId);
        while (rs2.next()) {
          String sql2 = "insert into orders_details_history (order_id, product_id, unit, qty, subtotal) " +
              "values(" + orderId + ",'" + rs2.getString("product_id") + "','" + rs2.getString("unit") + "'," +
              rs2.getDouble("qty") + "," + rs2.getString("subtotal") + ")";
          stmt2.executeUpdate(sql2);
        }
        stmt4.executeUpdate("delete from orders_details where order_id=" + orderId);
        stmt4.executeUpdate("delete from orders where order_id=" + orderId);
        
      }
      rs.close();
      stmt4.close();
      stmt3.close();
      stmt2.close();
      stmt.close();
    } catch (SQLException e) {
      logger.log(e.getMessage());
    }
    logger.close();
  }
  
  
  /*********************/
  /* cheques devueltos */
  /*********************/
  
  public static void readCheques() {
    Statement stmt;
    String sql;
    StringTokenizer tk;
    boolean allIsOk;
    boolean exists;
    
    /* Variables locales para uso del archivo */
    String customerId;
    String checkCode;
    String checkNumber;
    String amount;
    
    /* instanciamos el logger */
    Logger logger = new Logger("webchkdv");
    
    try {
      
      BufferedReader in = new BufferedReader(new FileReader("files/webchkdv.txt"));
      String str;
      
      try {
        
        /* creamos el statement */
        stmt = ConnectionBean.getConnection().createStatement();
        
        boolean entering = true;
        
        while ((str = in.readLine()) != null) {
          
          tk = new StringTokenizer(str, ";");
          if (tk.hasMoreTokens()) {
            
            if (entering) {
              sql = "delete from cheques";
              long n = stmt.executeUpdate(sql);
              entering = false;
            }
            customerId = tk.nextToken().trim();
            checkCode = tk.nextToken().trim();
            checkNumber = tk.nextToken().trim();
            amount = parseFloat(tk.nextToken().trim(), 2);
            sql = "insert into cheques (" +
                "customer_id," +
                "check_code," +
                "check_number," +
                "amount) " +
                "values (" +
                "'"+ customerId + "'," +
                "'" + checkCode + "'," +
                "'" + checkNumber + "'," +
                amount + ")";
            //System.out.println(sql);
            logger.log(sql);
            long n2 = stmt.executeUpdate(sql);
          }
        }
        stmt.close();
      } catch (SQLException ex) {
        ex.printStackTrace();
        logger.log(ex.getMessage());
      }
      
      in.close();
      
      /* borro el archivo */
      boolean success = (new File("files/webchkdv.txt")).delete();
      if (!success) {
        System.out.println("No se pudo eliminar el archivo webchkdv.txt");
      }
    } catch (IOException e) {
      System.out.println("Error leyendo el archivo webchkdv.txt : " + e);
      logger.log("Error leyendo el archivo webchkdv.txt : " + e.getMessage());
    }
    
    logger.close();
  }
  
  /*********************/
  /* status de pedidos */
  /*********************/
  
  public static void readOrderStatus() {
    Statement stmt;
    String sql;
    StringTokenizer tk;
    boolean allIsOk;
    boolean exists;
    
    /* Variables locales para uso del archivo */
    String customerId;
    String orderId;
    String orderDate;
    String statusId;
    String statusDesc;
    
    /* instanciamos el logger */
    Logger logger = new Logger("webstatus");
    
    try {
      
      BufferedReader in = new BufferedReader(new FileReader("files/webstatus.txt"));
      String str;
      
      try {
        
        ConnectionBean.getConnection().setAutoCommit(false);
        
        /* creamos el statement */
        stmt = ConnectionBean.getConnection().createStatement();
        
        //boolean entering = true;
        sql = "delete from orders_status";
        long n = stmt.executeUpdate(sql);
        
        while ((str = in.readLine()) != null) {
          
          tk = new StringTokenizer(str, ";");
          if (tk.hasMoreTokens()) {
            
            /*if (entering) {
              sql = "delete from orders_status";
              long n = stmt.executeUpdate(sql);
              entering = false;
            }*/
            orderDate = formatStringDate3(tk.nextToken().trim());
            orderId = tk.nextToken().trim();
            customerId = tk.nextToken().trim();
            statusId = tk.nextToken().trim();
            statusDesc = tk.nextToken().trim();
            sql = "insert into orders_status (" +
                "order_date," +
                "order_id," +
                "customer_id," +
                "status_id," +
                "status_description) " +
                "values (" +
                "'"+ orderDate + "'," +
                orderId + "," +
                "'" + customerId + "'," +
                "'" + statusId + "'," +
                "'" + statusDesc + "')";
            //System.out.println(sql);
            logger.log(sql);
            long n2 = stmt.executeUpdate(sql);
          }
        }
        ConnectionBean.getConnection().commit();
        stmt.close();
        ConnectionBean.getConnection().setAutoCommit(true);
      } catch (SQLException ex) {
        try {
          ConnectionBean.getConnection().rollback();
        } catch (SQLException ex2) {
          ex2.printStackTrace();
          logger.log(ex2.getMessage());
        }
        ex.printStackTrace();
        logger.log("ROLLING BACK THE TRANSACTION... " + ex.getMessage());
      }
      
      in.close();
      
      /* borro el archivo */
      boolean success = (new File("files/webstatus.txt")).delete();
      if (!success) {
        System.out.println("No se pudo eliminar el archivo webstatus.txt");
      }
    } catch (IOException e) {
      System.out.println("Error leyendo el archivo webstatus.txt : " + e);
      logger.log("Error leyendo el archivo webstatus.txt : " + e.getMessage());
    }
    
    logger.close();
  }
  
  
  /*********************/
  /* plan de ventas */
  /*********************/
  
  public static void readPlan() {
    Statement stmt;
    String sql;
    StringTokenizer tk;
    boolean allIsOk;
    boolean exists;
    
    /* Variables locales para uso del archivo */
    String fiscalYear;
    String year;
    String month;
    String salesmanId;
    String productId;
    String plan;
    String real;
    
    
    /* instanciamos el logger */
    Logger logger = new Logger("webplan");
    
    try {
      
      BufferedReader in = new BufferedReader(new FileReader("files/webplan.txt"));
      String str;
      
      try {
        
        /* creamos el statement */
        stmt = ConnectionBean.getConnection().createStatement();
        
        boolean entering = true;
        
        while ((str = in.readLine()) != null) {
          
          tk = new StringTokenizer(str, ";");
          if (tk.hasMoreTokens()) {
            if (entering) {
              sql = "delete from salesplan";
              long n = stmt.executeUpdate(sql);
              entering = false;
            }
            fiscalYear = tk.nextToken().trim();
            year = tk.nextToken().trim();
            month = tk.nextToken().trim();
            salesmanId = tk.nextToken().trim();
            productId = tk.nextToken().trim();
            plan = tk.nextToken().trim();
            real = tk.nextToken().trim();
            sql = "insert into salesplan (" +
                "fiscal_year," +
                "year," +
                "month," +
                "salesman_id," +
                "product_id," +
                "planned," +
                "done) " +
                "values (" +
                "'"+ fiscalYear + "'," +
                "'" + year + "'," +
                "'" + month + "'," +
                "'" + salesmanId + "'," +
                "'" + productId + "'," +
                plan + "," +
                real + ")";
            //System.out.println(sql);
            logger.log(sql);
            long n2 = stmt.executeUpdate(sql);
          }
        }
        stmt.close();
      } catch (SQLException ex) {
        ex.printStackTrace();
        logger.log(ex.getMessage());
      }
      
      in.close();
      
      /* borro el archivo */
      boolean success = (new File("files/webplan.txt")).delete();
      if (!success) {
        System.out.println("No se pudo eliminar el archivo webplan.txt");
      }
    } catch (IOException e) {
      System.out.println("Error leyendo el archivo webplan.txt : " + e);
      logger.log("Error leyendo el archivo webplan.txt : " + e.getMessage());
    }
    
    logger.close();
  }
  
  /*********************/
  /* metodo para crear los archivos de texto de las ordenes SQL > Txt */
  /*********************/
  
  public static void writeOrderFiles() {
    Statement stmt;
    Statement stmt2;
    ResultSet rs;
    ResultSet rs2;
    String sql;
    String line;
    ArrayList content = new ArrayList();
    ArrayList content2 = new ArrayList();
    
    /* instanciamos el logger */
    Logger logger = new Logger("orders");
    
    try {
      
      /* creamos el statement */
      stmt = ConnectionBean.getConnection().createStatement();
      stmt2 = ConnectionBean.getConnection().createStatement();
      
      /* seleccion y construccion del contenido para el detalle */
      sql = "select orders_details.* " + "" +
          "from orders_details, orders " + "" +
          "where orders.order_id = orders_details.order_id and orders.status = 'PEN' " +
          "order by orders_details.order_id";
      rs = stmt.executeQuery(sql);
      while (rs.next()) {
        line = "";
        line = fillOrderNumber(rs.getInt("order_id")) +
            fillOrderProduct(rs.getString("product_id").trim()) +
            fillOrderQuantity(rs.getFloat("qty")) +
            spaces(8) +
            rs.getString("unit").trim() +
            fillOrderCustomer(rs.getString("customer_id").trim()) + //ojo hay q crear este campo en la tabla detalle
            fillOrderWarehouse(rs.getString("warehouse").trim()) + //almacen ojo tambien crear en la table detalle y averiguar de donde se saca
            formatStringDate2(rs.getString("required_date")) +
            System.getProperty("line.separator");
        content2.add(line);
      }
      rs.close();
      
      /* seleccion y construccion del contenido para la cabecera */
      
      int orderId;
      //sql = "select * from orders where status = 'PEN' order by order_id";
       /*sql = "select orders_details.*,orders.* " + "" +
          "from orders_details, orders " + "" +
          "where orders.order_id = orders_details.order_id and orders.status = 'PEN' " +
          "order by orders_details.order_id";*/
       
       sql="select orders.*,(select distinct(warehouse) from orders_details where orders_details.order_id=orders.order_id) as warehouse from orders  where  orders.status = 'PEN' order by orders.order_id";
       
      rs2 = stmt.executeQuery(sql);
      while (rs2.next()) {
        line = "";
        orderId = rs2.getInt("order_id");
        
       /* line = fillOrderNumber(orderId) +
            fillOrderCustomer(rs2.getString("customer_id").trim()) +
            spaces(2) +
            spaces(4) +
            spaces(4) +
            spaces(4) +
            spaces(20) +
            spaces(1) +
            spaces(8) +
            formatComments(rs2.getString("comments")) +
            spaces(1) +
            spaces(8) +
            spaces(5) +
            formatStringDate2(rs2.getString("order_date")) +
            formatStringTime(rs2.getString("order_time")) +
            spaces(2) +
            spaces(120) +
            spaces(20) +
            spaces(20) +
            spaces(20) +
            spaces(2) +
            spaces(7) +
            spaces(1) +
            spaces(4) +
            "N" +
            "N" + System.getProperty("line.separator");
        content.add(line);*/
        
        
           line = fillOrderNumber(orderId) +
            fillOrderCustomer(rs2.getString("customer_id").trim()) +
            fillOrderWarehouse(rs2.getString("warehouse").trim())+
            formatStringDate2(rs2.getString("required_date"))+
            spaces(157) +
            formatStringDate2(rs2.getString("order_date")) +
            formatStringTime(rs2.getString("order_time")) +
            fillOrderComments(rs2.getString("comments")) +
            spaces(46) +
            "N" +
            "N" + System.getProperty("line.separator");
        content.add(line);
        
        
        
        
        /* actualizando el status de la orden */
        Format formatter = new SimpleDateFormat("hh:mm:ssa");
        String s = formatter.format(new java.util.Date());
        Format formatter2 = new SimpleDateFormat("dd/MM/yyyy");
        String s2 = formatter2.format(new java.util.Date());
        sql = "update orders set status='PRO',status_changed_on='" + s2 + "',status_changed_at='"+ s +"' where order_id=" + orderId;
        long ar = stmt2.executeUpdate(sql);
        logger.log(sql);
      }
      rs2.close();
      stmt.close();
      stmt2.close();
      
      if (content.size() > 0) { //cabecera
        
        ///* borro el archivo cabecera */
        //boolean success = (new File("files/orders.txt")).delete();
        //if (!success) {
        //    System.out.println("No se pudo eliminar el archivo orders.txt");
        //}
        
        /* creando la cabecera */
        try {
          BufferedWriter out = new BufferedWriter(new FileWriter("files/orders.txt", true));
          for (Object elem : content) {
            out.write((String) elem);
          }
          out.close();
          
          ///* borro el archivo detalles */
          //boolean success2 = (new File("files/odetails.txt")).delete();
          //if (!success2) {
          //  System.out.println("No se pudo eliminar el archivo odetails.txt");
          //}
          
          /* creando el detalle */
          try {
            BufferedWriter out2 = new BufferedWriter(new FileWriter("files/odetails.txt", true));
            for (Object elem : content2) {
              out2.write((String) elem);
            }
            out2.close();
          } catch (IOException e) {
            logger.log("Error escribiendo en el archivo de detalles de ordenes: " + e.getMessage());
            
          }
        } catch (IOException e) {
          logger.log("Error escribiendo en el archivo de cabecera de ordenes: " + e.getMessage());
        }
      }
    } catch (SQLException ex) {
      logger.log(ex.getMessage());
    }
    logger.close();
  }
  
  /*********************/
  /* metodo que procesa el archivo de texto de clientes */
  /*********************/
  
  public static void readCustomersFile() {
    Statement stmt;
    ResultSet rs;
    String sql;
    StringTokenizer tk;
    boolean allIsOk;
    boolean exists;
    
    /* Variables locales para uso del archivo */
    String id;
    String name;
    String creditLimit;
    String region;
    String salesmanId;
    String priceIndex;
    String warehouse;
    String diasRequerido;
    String listaPrecio;
    String warehouseFacturacion;
    
    /* instanciamos el logger */
    Logger logger = new Logger("webclien");
    
    try {
      
      BufferedReader in = new BufferedReader(new FileReader("files/webclien.txt"));
      String str;
      
      try {
        
        ConnectionBean.getConnection().setAutoCommit(false);
        
        /* creamos el statement */
        stmt = ConnectionBean.getConnection().createStatement();
        boolean entering = true;
        
        while ((str = in.readLine()) != null) {
          
          tk = new StringTokenizer(str, ";");
          if (tk.hasMoreTokens()) {
            
            if (entering) {
              sql = "delete from customers";
              long n = stmt.executeUpdate(sql);
              entering = false;
            }
            
            id = tk.nextToken().trim();
            name = parseSQLDescription(tk.nextToken().trim());
            creditLimit = parseFloat(tk.nextToken().trim(), 2);
            region = tk.nextToken().trim();
            salesmanId = tk.nextToken().trim();
            priceIndex = tk.nextToken().trim();
            warehouse = tk.nextToken().trim();
            diasRequerido= tk.nextToken().trim();
            listaPrecio= tk.nextToken().trim();
            warehouseFacturacion= tk.nextToken().trim();
            
            if (warehouseFacturacion.trim().compareTo("")==0)
            {
                warehouseFacturacion="0";
            }

            // CAMBIO EL 23/09/2015
            // PORQUE EL ARCHIVO VIENE SIN LISTA DE PRECIOS NI CUSTOMER_FATURACION
            /*sql = "insert into customers (customer_id, name, credit_limit, region, salesman_id, price_index,warehouse,diasRequerido,lista_precio,warehouse_facturacion) " +
                "values ('"+ id + "','" + name + "'," + creditLimit + ",'" + region + "','" + salesmanId + "'," + priceIndex + ",'" + warehouse + "',"+ diasRequerido +",'" + listaPrecio.trim() + "','" + warehouseFacturacion.trim() + "')";*/
            
            sql = "insert into customers (customer_id, name, credit_limit, region, salesman_id, price_index,warehouse,diasRequerido) " +
                "values ('"+ id + "','" + name + "'," + creditLimit + ",'" + region + "','" + salesmanId + "'," + priceIndex + ",'" + warehouse + "',"+ diasRequerido +")";
            
            logger.log(sql);
            long n2 = stmt.executeUpdate(sql);
            
          }
        }
        
        ConnectionBean.getConnection().commit();
        stmt.close();
        ConnectionBean.getConnection().setAutoCommit(true);
        
      } catch (SQLException ex) {
        try {
          ConnectionBean.getConnection().rollback();
        } catch (SQLException ex2) {
          ex2.printStackTrace();
          logger.log(ex2.getMessage());
        }
        ex.printStackTrace();
        logger.log("ROLLING BACK THE TRANSACTION... " + ex.getMessage());
      }
      in.close();
      
      /* borro el archivo */
      boolean success = (new File("files/webclien.txt")).delete();
      if (!success) {
        System.out.println("No se pudo eliminar el archivo webclien.txt");
      }
    } catch (IOException e) {
      System.out.println("Error leyendo el archivo webclien.txt : " + e);
      logger.log(e.getMessage());
    }
    
    logger.close();
  }
  
  /*********************/
  /* procesar el archivo de productos  */
  /*********************/
  
  public static void readProductsFile() {
    Statement stmt;
    ResultSet rs;
    String sql;
    StringTokenizer tk;
    boolean allIsOk;
    boolean exists;
    
    /* Variables locales para uso del archivo */
    String id;
    String description;
    String stock;
    String price1;
    String price2;
    String price3;
    String price4;
    String price5;
    String price6;
    String price7;
    String price8;
    String price9;
    String price10;
    String primaryUnit;
    String alternativeUnit;
    String conversionRate;
    String warehouse;
    String brand;
    String target;
    String estado;
    
    /* instanciamos el logger */
    Logger logger = new Logger("webprodu");
    
    try {
      
      BufferedReader in = new BufferedReader(new FileReader("files/webprodu.txt"));
      String str;
      
      try {
        
        ConnectionBean.getConnection().setAutoCommit(false);
        
        /* creamos el statement */
        stmt = ConnectionBean.getConnection().createStatement();
        boolean entering = true;
        
        while ((str = in.readLine()) != null) {
          
          tk = new StringTokenizer(str, ";");
          if (tk.hasMoreTokens()) {
            
            if (entering) {
              /*sql = "delete from products";
              long n = stmt.executeUpdate(sql);*/
              entering = false;
            }
            
            id = tk.nextToken().trim();
            description = parseSQLDescription(tk.nextToken().trim());
            stock = parseFloat2(tk.nextToken().trim());
            price1 = parseFloat2(tk.nextToken().trim());
            price2 = parseFloat2(tk.nextToken().trim());
            price3 = parseFloat2(tk.nextToken().trim());
            price4 = parseFloat2(tk.nextToken().trim());
            price5 = parseFloat2(tk.nextToken().trim());
            price6 = parseFloat2(tk.nextToken().trim());
            price7 = parseFloat2(tk.nextToken().trim());
            price8 = parseFloat2(tk.nextToken().trim());
            price9 = parseFloat2(tk.nextToken().trim());
            price10 = parseFloat2(tk.nextToken().trim());
            primaryUnit = tk.nextToken().trim();
            alternativeUnit = tk.nextToken().trim();
            conversionRate = tk.nextToken().trim();
            warehouse = tk.nextToken().trim();
            brand = tk.nextToken().trim();
            target = tk.nextToken().trim();
            estado="1";
            /* chequeo si esta el registro */
                        sql = "select count(product_id) as c from products where product_id='" + id + "' and estado='0'";
                        rs = stmt.executeQuery(sql);
                        if (rs.next()) {
                            if (rs.getInt("c") > 0) {
                                exists = true;
                            } else {
                                exists = false;
                            }
                        } else {
                            exists = false;
                        }
                        rs.close();
                         
                        if (!exists) {
                            sql = "delete from products where product_id='" + id+ "'";
                            long n = stmt.executeUpdate(sql);
                        }
            if  (!exists)
            {
             sql = "insert into products (product_id, description, stock, price1, price2, price3, price4, price5, price6, price7, price8, price9, price10, primary_unit, alternative_unit,conversion_rate, warehouse, brand, target,estado) " +
                "values ('"+ id + "','" + description + "'," + stock + "," + price1 + "," + price2 + "," + price3 + "," + price4 + "," + price5 + "," + price6 + "," + price7 + "," + price8 + "," + price9 + "," + price10 + ",'" + primaryUnit + "','" + alternativeUnit + "'," + conversionRate + ",'" + warehouse + "','" + brand + "','" + target + "','1')";
            logger.log(sql);
            long n2 = stmt.executeUpdate(sql);
            }
                   
          }
        }
        ConnectionBean.getConnection().commit();
        stmt.close();
        ConnectionBean.getConnection().setAutoCommit(true);
        
        //llamar la pagina jsp para cargar el cache
       // HttpHelper.callHttpPage();
        
      } catch (SQLException ex) {
        try {
          ConnectionBean.getConnection().rollback();
        } catch (SQLException ex2) {
          ex2.printStackTrace();
          logger.log(ex2.getMessage());
        }
        ex.printStackTrace();
        logger.log("ROLLING BACK THE TRANSACTION... " + ex.getMessage());
      }
      
      in.close();
      
      /* borro el archivo */
      boolean success = (new File("files/webprodu.txt")).delete();
      if (!success) {
        System.out.println("No se pudo eliminar el archivo webprodu.txt");
      }
    } catch (IOException e) {
      System.out.println("Error leyendo el archivo webprodu.txt : " + e);
      logger.log(e.getMessage());
    }
    
    logger.close();
  }
  
  /*********************/
  /* cuentas por cobrar */
  /*********************/
  
  public static void readEdoCtaFile() {
    Statement stmt;
    String sql;
    StringTokenizer tk;
    boolean allIsOk;
    boolean exists;
    
    /* Variables locales para uso del archivo */
    String customerId;
    String docType;
    String docNumber;
    String issDate;
    String expDate;
    String amount1; // MONTO DE FACTURA
    String amount2; // N/C DEVOLUCION
    String amount3; // MTO. GRAV. AJUSTADO
    String amount4; // IVA NETO FACTURA
    String amount5; // N/C APLICADA BS
    String amount6; // IVA N/C APLICADA BS
    String amount7; // N/C DSCTO FINANCIERO
    String amount8; // IVA N/C DSCTO FINANCIERO
    String amount9; // IVA NETO GENERAL
    String amount10;// RETENCION IVA
    String amount11;// ABONOS
    String amount12;// SALDO FACTURA
    String expDays;
    
    /* instanciamos el logger */
    Logger logger = new Logger("webedocta");
    
    try {
      
      BufferedReader in = new BufferedReader(new FileReader("files/webedocta.txt"));
      String str;
      
      try {
        
        /* creamos el statement */
        stmt = ConnectionBean.getConnection().createStatement();
        
        boolean entering = true;
        
        while ((str = in.readLine()) != null) {
          
          tk = new StringTokenizer(str, ";");
          if (tk.hasMoreTokens()) {
            if (entering) {
              sql = "delete from balances";
              long n = stmt.executeUpdate(sql);
              entering = false;
            }
            customerId = tk.nextToken().trim();
            docType = tk.nextToken().trim();
            docNumber = tk.nextToken().trim();
            issDate = tk.nextToken().trim();
            expDate = tk.nextToken().trim();
            amount1 = parseFloat(tk.nextToken().trim(), 2);
            amount2 = parseFloat(tk.nextToken().trim(), 2);
            amount3 = parseFloat(tk.nextToken().trim(), 2);
            amount4 = parseFloat(tk.nextToken().trim(), 2);
            amount5 = parseFloat(tk.nextToken().trim(), 2);
            amount6 = parseFloat(tk.nextToken().trim(), 2);
            amount7 = parseFloat(tk.nextToken().trim(), 2);
            amount8 = parseFloat(tk.nextToken().trim(), 2);
            amount9 = parseFloat(tk.nextToken().trim(), 2);
            amount10 = parseFloat(tk.nextToken().trim(), 2);
            amount11 = parseFloat(tk.nextToken().trim(), 2);
            amount12 = parseFloat(tk.nextToken().trim(), 2);
            expDays = tk.nextToken().trim();
            
            sql = "insert into balances (" +
                "customer_id," +
                "doc_type," +
                "doc_number," +
                "issue_date," +
                "exp_date," +
                "invoice_amount," +
                "credit_note_dev," +
                "adjusted_taxable_amount," +
                "net_iva," +
                "applied_credit_note," +
                "applied_credit_note_iva," +
                "credit_note_fin_discount," +
                "credit_note_fin_discount_iva," +
                "general_net_iva," +
                "iva_retention," +
                "payments," +
                "invoice_balance," +
                "exp_days) " +
                "values (" +
                "'"+ customerId + "'," +
                "'" + docType + "'," +
                "'" + docNumber + "'," +
                "'" + issDate + "'," +
                "'" + expDate + "'," +
                amount1 + "," +
                amount2 + "," +
                amount3 + "," +
                amount4 + "," +
                amount5 + "," +
                amount6 + "," +
                amount7 + "," +
                amount8 + "," +
                amount9 + "," +
                amount10 + "," +
                amount11 + "," +
                amount12 + "," +
                expDays + ")";
            long n2 = stmt.executeUpdate(sql);
            logger.log(sql);
          }
        }
        stmt.close();
      } catch (SQLException ex) {
        ex.printStackTrace();
        logger.log(ex.getMessage());
      }
      
      in.close();
      
      /* borro el archivo */
      boolean success = (new File("files/webedocta.txt")).delete();
      if (!success) {
        System.out.println("No se pudo eliminar el archivo webedocta.txt");
      }
    } catch (IOException e) {
      System.out.println("Error leyendo el archivo webedocta.txt : " + e);
      logger.log("Error leyendo el archivo webedocta.txt : " + e.getMessage());
    }
    
    logger.close();
  }
  
  /*********************/
  /* procesar vendedores */
  /*********************/
  
  public static void readSalesmenFile() {
    Statement stmt;
    ResultSet rs;
    String sql;
    StringTokenizer tk;
    boolean allIsOk;
    boolean exists;
    
    /* Variables locales para uso del archivo */
    String id;
    String name;
    String region;
    String topSerial;
    String bottomSerial;
    String password;
    
    /* instanciamos el logger */
    Logger logger = new Logger("webvende");
    
    try {
      
      BufferedReader in = new BufferedReader(new FileReader("files/webvende.txt"));
      String str;
      
      try {
        
        /* creamos el statement */
        stmt = ConnectionBean.getConnection().createStatement();
        
        while ((str = in.readLine()) != null) {
          
          tk = new StringTokenizer(str, ";");
          if (tk.hasMoreTokens()) {
            id = tk.nextToken().trim();
            name = parseSQLDescription(tk.nextToken().trim());
            region = tk.nextToken().trim();
            topSerial = tk.nextToken().trim();
            bottomSerial = tk.nextToken().trim();
            password = tk.nextToken().trim();
            
            /* chequeo si esta el registro FALTA GUARDAR EL PWD */
            sql = "select count(salesman_id) as c from salesmen where salesman_id='" + id + "'";
            rs = stmt.executeQuery(sql);
            if (rs.next()) {
              if (rs.getInt("c") > 0) {
                exists = true;
              } else {
                exists = false;
              }
            } else {
              exists = false;
            }
            rs.close();
            
            if (exists) {
              sql = "delete from salesmen where salesman_id='" + id+ "'";
              long n = stmt.executeUpdate(sql);
            }
            
            sql = "insert into salesmen (salesman_id, name, region, top_serial, bottom_serial, comments) " +
                "values ('"+ id + "','" + name + "','" + region + "'," + topSerial + "," + bottomSerial + ",'" + password + "')";
            System.out.println(sql);
            long n2 = stmt.executeUpdate(sql);
            logger.log(sql);
          }
        }
        stmt.close();
      } catch (SQLException ex) {
        ex.printStackTrace();
        logger.log(ex.getMessage());
      }
      
      in.close();
      
      /* borro el archivo */
      boolean success = (new File("files/webvende.txt")).delete();
      if (!success) {
        System.out.println("No se pudo eliminar el archivo webvende.txt");
      }
    } catch (IOException e) {
      System.out.println("Error leyendo el archivo webvende.txt : " + e);
      logger.log(e.getMessage());
    }
    
    logger.close();
    
  }
  
}