package src.com;

public class Manager extends DataBase{
    // Constructor
    public Manager(String url,String user, String pass ){
        super(url, user, pass);
    }
    
    public Patient createPatient(String Adhaar){ // returns null if can't somehow
        try{
            return new Patient(Adhaar);
        }
        catch(Exception e   ){
            System.out.println(e    );
            return null;
        }
    }
    
    public boolean checkPatientClass(Patient p ){
        if(p.getName() == "NULL"){
            return false;
        }
        if(p.getPhoneNum() == "NULL"){
            return false;
        }
        if(p.getAge() == -1){
            return false;
        }
        if(p.getSex() == 'n'){
            return false;
        }
        return true;
    }

}
