package project;
import java.sql.Connection;
import java.sql.DriverManager;

public class DbConnection {

    static Connection con;
    static  String DRIVER = "com.mysql.cj.jdbc.Driver";
    static  String DB_URL="jdbc:mysql://localhost/trial";
    static  String USER= "root";
    static  String PASSWORD="";

    public static Connection createDBConnection(){

        try{
            //Load driver
            Class.forName(DRIVER);
            //get Connection
            con= DriverManager.getConnection(DB_URL,USER,PASSWORD);

        }catch (Exception ex){
            ex.printStackTrace();
        }
        return con;
    }

}
