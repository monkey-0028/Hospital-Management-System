import java.sql.*;

public class DataBase {
    private Connection con=null;
    private String message;
    private Statement statement=null;
    // constructor
    DataBase(String url,String user, String pass    ){
        try{
            this.con = DriverManager.getConnection(url,user,pass);
            this.statement = con.createStatement();
            this.message = "Success connection"; 
        }
        catch(Exception e){
            this.message = "Failure Connection";
            System.out.println(e);
        }
    }
    public static void main(String[] args) {
        // String url = "jdbc:mysql://localhost:3306/GoodHospital";
        // String user = "GoodUser";
        // String pass = "GoodPass@123";
        DataBase d = new DataBase("jdbc:mysql://localhost:3306/GoodHospital", "GoodUser", "GoodPass@123");
        System.out.println(d.getMessage());
        d.init();
        return;
     }
     // methods
    String getMessage(){
        return this.message;
    }

    //creating required Table and all
    void init(){
        String query1 = "CREATE TABLE patient(name VARCHAR(100) NOT NULL, age INT NOT NULL);";
        try{
            statement.executeUpdate(query1);
            System.out.println("Success");
        }
        catch(Exception e   ){
            System.out.println(e);
        }
    }
}
