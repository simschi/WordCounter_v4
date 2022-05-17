/*
import java.sql.Connection;

public static class Database {
    private static Connection conn = null;
    public static void connect() {  
        if(conn != null) return;
        try {  
            String url = "jdbc:sqlite:../config.db";  
            conn = DriverManager.getConnection(url);    
            System.out.println("Connection to SQLite has been established.");       
        } catch (SQLException e) {  
            System.out.println(e.getMessage());  
        } 
    }
    public static void disconnect() {  
        if(conn == null) return;
        conn.close(); 
    }  

    public static void SaveSetting(String key, String value){

    }
    public static String LoadSetting(String key){
        
    }
}
*/