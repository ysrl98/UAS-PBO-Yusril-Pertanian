/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package sys;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.DefaultComboBoxModel;

import java.io.File;
import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager; 
import net.sf.jasperreports.engine.JasperFillManager; 
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.design.JRDesignQuery; 
import net.sf.jasperreports.engine.design.JasperDesign; 
import net.sf.jasperreports.engine.xml.JRXmlLoader; 
import net.sf.jasperreports.view.JasperViewer;


/**
 *
 * @author USER
 */
public class crudTable {
    private String jdbcURL="jdbc:mysql://localhost:3306/2210010454_pertanian";
    private String username="root";
    private String password="";
    
    
    private DefaultTableModel Modelnya;
    private TableColumn Kolomnya;
    
    public crudTable(){}
    
    public Connection getKoneksiDB() throws SQLException{
        try{
            Driver mysqldriver = new com.mysql.jdbc.Driver();
            DriverManager.registerDriver(mysqldriver);
            System.out.println("Koneksi Berhasil !");
            
        } catch (Exception e) {
            System.out.println(e.toString());
        }
        return DriverManager.getConnection(jdbcURL, username, password);
    }
    
    public boolean DuplicatKey(String NamaTabel, String PrimaryKey, String IsiData){
        boolean hasil = false;
        int jumlah=0;
        try{
            String SQL="SELECT * FROM "+NamaTabel+" WHERE "+PrimaryKey+" = "+IsiData+" ";
            Statement perintah = getKoneksiDB().createStatement();
            ResultSet hasilData = perintah.executeQuery(SQL);
            while(hasilData.next()){
                jumlah++;
            } 
            if (jumlah==1) {hasil=true;} else {hasil=false;}
            
        }   catch (Exception e) {
                    System.out.println(e.toString());
        }
        return hasil;
    }
    
    public String getFieldTabel(String[] FieldTabelnya){
        String hasilnya="";
        int deteksiIndexAkhir=FieldTabelnya.length-1;
        try{
               for (int i = 0; i < FieldTabelnya.length;i++){
                if (i==deteksiIndexAkhir){
                    hasilnya=hasilnya+FieldTabelnya[i];
                } else{
                    hasilnya=hasilnya+FieldTabelnya[i]+",";
               }
            } 
        }catch (Exception e){
                System.out.println(e.toString());
        }
        return "("+hasilnya+")";
    }
    
    public String getIsiTabel(String[] IsiTabelnya){
        String hasilnya="";
        int DeteksiTabel=IsiTabelnya.length-1;
        try{
               for (int i = 0; i < IsiTabelnya.length;i++){
                if (i==DeteksiTabel){
                    hasilnya=hasilnya+"'"+IsiTabelnya[i]+"'";
                } else{
                    hasilnya=hasilnya+"'"+IsiTabelnya[i]+"',";
               }
            } 
        }catch (Exception e){
                System.out.println(e.toString());
        }
        return "("+hasilnya+")";
    }
    
   public void SimpanDinamis(String NamaTabel, String[] Fieldnya, String[] Isinya){
       try {
           String SQLSave ="INSERT INTO "+NamaTabel+" "+getFieldTabel(Fieldnya)+"VALUES "+getIsiTabel(Isinya);
           Statement suruh =getKoneksiDB().createStatement();
           suruh.executeUpdate(SQLSave);
           suruh.close();
       } catch (Exception e) {
           System.out.println(e.toString());
       }
   }
   public String getFieldValueEdit(String[] Field, String[] value){
        String hasil = "";
        int deteksi = Field.length-1;
        try {
            for (int i = 0; i < Field.length; i++) {
                if (i==deteksi){
                    hasil = hasil +Field[i]+" ='"+value[i]+"'";
                }else{
                   hasil = hasil +Field[i]+" ='"+value[i]+"',";  
                }
            }
        } catch (Exception e) {
            System.out.println(e.toString());
        }
        
        return hasil;
    }
   
   public void UbahDinamis(String NamaTabel, String PrimaryKey, String IsiPrimary,String[] Field, String[] Value){  
        try {
           String SQLUbah = "UPDATE "+NamaTabel+" SET "+getFieldValueEdit(Field, Value)+" WHERE "+PrimaryKey+"='"+IsiPrimary+"'";
           Statement perintah = getKoneksiDB().createStatement();
           perintah.executeUpdate(SQLUbah);
           perintah.close();
           getKoneksiDB().close();
        } catch (Exception e) {
            System.err.println(e.toString());
        }
        
    }
   
   public void HapusDinamis(String NamaTabel, String PK, String isi){
        try {
            String SQL="DELETE FROM "+NamaTabel+" WHERE "+PK+"='"+isi+"'";
            Statement perintah = getKoneksiDB().createStatement();
            perintah.executeUpdate(SQL);
            perintah.close();
        } catch (Exception e) {
            System.out.println(e.toString());
        }
    }   
   
   public void tampilLaporan(String laporanFile, String SQL){
      try {
          File file = new File(laporanFile);
          JasperDesign jasDes = JRXmlLoader.load(file);

           JRDesignQuery sqlQuery = new JRDesignQuery();
           sqlQuery.setText(SQL);   
           jasDes.setQuery(sqlQuery);

           JasperReport JR = JasperCompileManager.compileReport(jasDes);
           JasperPrint JP = JasperFillManager.fillReport(JR,null,getKoneksiDB()); 
           JasperViewer.viewReport(JP,false);
         } catch (Exception e) {
            JOptionPane.showMessageDialog(null,e.toString());
         }
   }
   
   public ResultSet getData(String query) {
    try {
        Connection conn = getKoneksiDB();
        Statement stmt = conn.createStatement();
        return stmt.executeQuery(query);
    } catch (SQLException e) {
        JOptionPane.showMessageDialog(null, "Error executing query: " + e.getMessage());
        return null;
    }
    }
   
   public void loadDataToTable(javax.swing.JTable table, String query) {
    DefaultTableModel model = (DefaultTableModel) table.getModel();
    model.setRowCount(0); 
    try {
        ResultSet rs = getData(query);
        while (rs.next()) {
            Object[] row = new Object[rs.getMetaData().getColumnCount()];
            for (int i = 0; i < row.length; i++) {
                row[i] = rs.getObject(i + 1); 
            }
            model.addRow(row);
        }
    } catch (SQLException e) {
        JOptionPane.showMessageDialog(null, "Gagal memuat data: " + e.getMessage());
    }
}

    
    
}



   
   
   
   

   

