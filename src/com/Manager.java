package src.com;

import com.mysql.cj.util.Util;

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
        if(!UtilMethod.isName(p.getName())){
            return false;
        }
        if(!UtilMethod.isPhoneNumber(p.getPhoneNum())){
            return false;
        }
        if(!UtilMethod.isAge(p.getAge())){
            return false;
        }
        if(!UtilMethod.isSex(p.getSex())){
            return false;
        }
        return true;
    }
    public boolean checkAppointmentClass( Appointment a ){
        if(a.getSlot() != -1 && a.getAaadhar() != null && a.getAppointment_date() != null && a.getSymptoms() != null){
            return true;
        }
        return false;
    }

}
