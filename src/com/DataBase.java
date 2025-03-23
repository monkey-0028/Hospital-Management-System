package src.com;
import java.sql.*;

public class DataBase {
    private Connection con=null;
    private String status;
    private Statement statement=null;
    // constructor
    public DataBase(String url,String user, String pass    ){
        try{
            this.con = DriverManager.getConnection(url,user,pass);
            this.statement = con.createStatement();
            this.status = "Ok"; 
        }
        catch(Exception e){
            this.status = "ERR --> Problem in Connecting to mysql";
            System.out.println(e);
        }
    }
    String getStatus(){
        return this.status;
    }
    
    //creating required Table and all
    boolean init(){
        // create patient table
        String queryPatientTable = "CREATE TABLE patient(aadhaar VARCHAR(12) PRIMARY KEY,name VARCHAR(60) NOT NULL, number VARCHAR(13) NOT NULL,age INT NOT NULL, sex CHAR NOT NULL);";
        try{
            statement.executeUpdate(queryPatientTable);
            return true;
        }
        catch(Exception e   ){
            if(e.toString().contains("already exists")){
                this.status = "Ok";
                return true;
            }
            else{
                this.status = "ERR - problem in initizing table";
                return false;
            }
        }
    }
    // simply adds patient data, without checking wether the name or other variables are set or not. this will be managed by Manager.
    // handle duplicates --> DONE
    boolean addData(Patient p){
        String queryAddData = "INSERT INTO patient (aadhaar, name, number, age, sex) VALUES (?,?,?,?,?)";
        try{
            PreparedStatement pStatement = con.prepareStatement(queryAddData);
            pStatement.setString(1,p.getAdhaarNum());
            pStatement.setString(2, p.getName());
            pStatement.setString(3, p.getPhoneNum());   
            pStatement.setInt(4, p.getAge());
            pStatement.setString(5, String.valueOf(p.getSex()));

            pStatement.executeUpdate();

            this.status = "Ok";
            return true;
        }
        catch(Exception e   ){
            if(e.toString().contains("Duplicate entry")){
                this.status = "ERR --> Duplicate entry found! ";
            }
            return false;
        }
    }
    // where searchINFO = "name = satyaprakash"
    Patient [] selectData(String searchINFO){
        String query = "SELECT * FROM patient WHERE "+searchINFO+";";
        Patient [] resultArray;
        try{
            int size = getResultSetSize(searchINFO);
            if(size != -1){
                resultArray = new Patient[size];
            }
            else{
                this.status = "ERR --> can't get the size of ResultSet";
                return null;
            }

            ResultSet r = this.statement.executeQuery(query);
            int ptr = 0;
            while (r.next()) {
                resultArray[ptr] = new Patient(r.getString("aadhaar"));
                resultArray[ptr ].setName(r.getString("name"));
                resultArray[ptr ].setNumber(r.getString("number"));
                resultArray[ptr ].setAge(r.getInt("age"));
                resultArray[ptr ].setSex((r.getString("sex")).charAt(0));
                ptr++;
            }
            this.status = "Ok";
            return resultArray;
        }
        catch(Exception e   ){
            this.status = "ERR --> "+e.toString();
            return null;
        }
        
    }

    private int getResultSetSize(String searchINFO    ){
        String sizeQuery = "SELECT COUNT(*) AS total FROM patient WHERE "+searchINFO+";";
        try{
            ResultSet r = this.statement.executeQuery(sizeQuery);
            int size = 0;
            while(r.next()){
                size = r.getInt("total");
            }
            return size;
            
        }
        catch(Exception e   ){
            return -1;
        }
    }

    void deleteData(Patient p   ){
        String deleteQuery = "DELETE FROM patient WHERE aadhaar = "+p.getAdhaarNum()+";";
        try{
            statement.executeUpdate(deleteQuery);
            this.status = "Ok";
        }
        catch(Exception e   ){
            this.status = "ERR --> Can't Delete Data\n"+e.toString();
        }
    }
    void deleteData(String searchINFO   ){
        Patient [] resultArray = this.selectData(searchINFO);
        for(Patient item: resultArray){
            deleteData(item);
        }
    }
}
