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
    // getters
    public String getStatus(){
        return this.status;
    }
    
    //creating required Table and all
    public boolean init(){
        // create patient table
        String queryPatientTable = "CREATE TABLE patient(aadhaar VARCHAR(12) PRIMARY KEY,name VARCHAR(60) NOT NULL, number VARCHAR(13) NOT NULL,age INT NOT NULL, sex CHAR NOT NULL);";
        String queryAppointmentTable = "CREATE TABLE appointment(aadhaar VARCHAR(12) NOT NULL, appointment_date DATE NOT NULL,symptoms VARCHAR(200),slot INT NOT NULL,ts TIMESTAMP DEFAULT CURRENT_TIMESTAMP);";
        try{
            try{
                statement.executeUpdate(queryPatientTable);
            }
            catch(Exception e   ){
                if(e.toString().contains("already exists")){
                    this.status = "Ok";
                }
                else{
                    this.status = "ERR - Error in initilizing table";
                    return false;
                }
            }
            try{
                statement.execute(queryAppointmentTable);
            }
            catch(Exception e   ){
                if(e.toString().contains("already exists")){
                    this.status = "Ok";
                
                }
                else{
                    this.status  = "ERR - problem in initizing table";
                    System.out.println(e);
                    return false;
                }
            }
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
    public boolean forcedInit(){
        String q1 = "DROP TABLE patient;";
        String q2 = "DROP TABLE appointment;";
        try{
            try{
                this.statement.executeUpdate(q1);
            }
            catch(Exception e   ){
                status = "ERR -> can't drop patient table(forecedINIT)";
            }
            try{
                this.statement.executeUpdate(q2);
            }
            catch(Exception e   ){
                status = "ERR --> can't drop appointment table (forcedINIT)";
            }
            return init();
            
        }
        catch(Exception e   ){
            System.out.println(e    );
            return false;
        }

    }
    // simply adds patient data, without checking wether the name or other variables are set or not. this will be managed by Manager.
    // handle duplicates --> DONE
    public boolean addData(Patient p){
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
    public Patient [] selectData(String searchINFO){
        String query;
        if(searchINFO == "all"){
            query = "SELECT * FROM patient;";
        }
        else{
            query = "SELECT * FROM patient WHERE "+searchINFO+";";
        }
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
        String sizeQuery;
        if(searchINFO == "all"){
            sizeQuery = "SELECT COUNT(*) AS total FROM patient;";
        }
        else{
            sizeQuery = "SELECT COUNT(*) AS total FROM patient WHERE "+searchINFO+";";
        }
        try{
            ResultSet r = this.statement.executeQuery(sizeQuery);
            int size = 0;
            while(r.next()){
                size = r.getInt("total");
            }
            // sout(size);
            return size;
            
        }
        catch(Exception e   ){
            return -1;
        }
    }

    public void deleteData(Patient p   ){
        String deleteQuery = "DELETE FROM patient WHERE aadhaar = "+p.getAdhaarNum()+";";
        try{
            statement.executeUpdate(deleteQuery);
            this.status = "Ok";
        }
        catch(Exception e   ){
            this.status = "ERR --> Can't Delete Data\n"+e.toString();
        }
    }
    public void deleteData(String searchINFO   ){
        Patient [] resultArray = this.selectData(searchINFO);
        for(Patient item: resultArray){
            deleteData(item);
        }
    }
    public boolean isFreeSlot(int slot, String date){
        String query = String.format("SELECT COUNT(*) AS tt FROM appointment WHERE slot = %d AND appointment_date = \"%s\";", slot,date);
        // query += " AND appointment_date = \"" + date + "\";";
        try{
            ResultSet r = this.statement.executeQuery(query);
            int size = 0;
            while(r.next()){
                size = r.getInt("tt");
            }
            if(size <10){
                System.out.println(size);
                return true;
            }
            else{
                return false;
            }
            
        }
        catch(Exception e   ){
            status = "ERR --> isFreeSlot error";
            System.out.println(status);
            System.out.println(e);
            return false;
    }
    }
    public void addAppointment(Appointment a){
        // String aadaar, String date, int slot, String symp
        String aadaar = a.getAaadhar();
        String date = a.getAppointment_date();
        int slot = a.getSlot();
        String symp = a.getSymptoms();

        String QueryAddAppointment = "INSERT INTO appointment(aadhaar, appointment_date,symptoms,slot)";
        QueryAddAppointment += String.format(" VALUE (\"%s\",\"%s\",\"%s\",%d)", aadaar,date,symp,slot);   ;
        try{
            this.statement.executeUpdate(QueryAddAppointment);
            status = "Ok --> appointment added!!";
        }
        catch(Exception e   ){
            System.out.println(e    );
            status = "ERR --> can't add appointment";
        }
    }
    private int ResultSizeOfAppointment(String aadhaar  ){
        String query = "SELECT COUNT(*) AS total FROM appointment WHERE aadhaar = \""+aadhaar + "\"";
        int size = -1;
        try{
            ResultSet r = this.statement.executeQuery(query);
            while(r.next()){
                size = r.getInt("total");
            }
            return size;
        }
        catch(Exception e   ){
            System.out.println(e);
            return size;
        }
    }
    public Appointment [] selectAppointment(String aadhaar){
        Appointment [] returnVal = new Appointment[ResultSizeOfAppointment(aadhaar)];
        int i = 0;
        String query = "SELECT * FROM appointment WHERE aadhaar = \"" + aadhaar+"\";";
        try{
            ResultSet r = this.statement.executeQuery(query);
            while(r.next()){
                returnVal[i] = new Appointment();
                returnVal[i].setAadhaar(aadhaar);
                returnVal[i].setSlot(r.getInt("slot"));
                returnVal[i].setSymptom(r.getString("symptoms"));
                returnVal[i].Setappointment_date(r.getDate("appointment_date").toString());
                i++;
            }
            return returnVal;

        }
        catch (Exception e  ){
            System.out.println(e    );
            return null;
        }
        
    }
    public static void main(String[] args) {
        DataBase d = new DataBase("jdbc:mysql://localhost:3306/GoodHospital", "GoodUser", "GoodPass@123");
        d.isFreeSlot(2, "2025-02-02")   ;
        for(Patient item : d.selectData("all")){
            System.out.println(item);
        }
    }
}

    

