package src.com;


public class Appointment {
    private String aadhaar;
    private String appointment_date;
    private String symptoms;
    private int slot;


    //setters
    public void setAadhaar(String aadhaar){
        this.aadhaar = aadhaar;
    }
    public void Setappointment_date(String date   ){
        this.appointment_date = date; 
    }
    public void setSymptom(String s ){
        this.symptoms = s;
    }
    public boolean setSlot(int n   ){
        if(n <=3 && n>=1){
            this.slot = n;
            return true;
        }
        return false;
    }
    // getters
    public String getAaadhar(){
        return this.aadhaar;
    }
    public String getSymptoms(){
        return this.symptoms;
    }
    public String getAppointment_date(){
        return this.appointment_date;
    }
    public int getSlot(){
        return this.slot;
    }
    // 
    @Override
    public String toString(){
        return String.format("appointment_date: %s, symptoms: %s, slot: %d", this.appointment_date,this.symptoms,this.slot);
    }
    

}
