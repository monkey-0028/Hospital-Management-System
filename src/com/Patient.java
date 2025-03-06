package src.com;

public class Patient {
    private String name = "Null";
    private String phoneNum = "Null";
    private String AdharNum = "Null";


    public static void main(String[] args) {
        // Patient p = new Patient();
        // System.out.println(p    );
        System.out.println(isAdhaar("12311111111a"));
        
    }

    // constructor
    Patient(String Adhaar)throws AdhaarException{
        if(isAdhaar(Adhaar)){
            this.AdharNum = Adhaar;
        }
        else{
            throw new AdhaarException();
        }
        
    }

    // getters
    public String getName(){
        return this.name;
    }
    public String getPhoneNum(){
        return this.phoneNum;
    }
    public String getAdhaarNum(){
        return this.AdharNum;
    }
    // setters
    void setName(String name    ){
        this.name = name;
    }
    void setNumber(String Phonenumber    ){
        this.phoneNum  = Phonenumber;
    }

    // Method
    @Override
    public String toString(){
        return "Name: "+this.name+"\nNumber: "+this.phoneNum+"\nAdhaar: "+this.AdharNum;
    }
    private static boolean isAdhaar(String adhaar){
        if(adhaar.length() == 12 && adhaar.matches("\\d+")){
            return true;
        }
        return false;
    }
    // Phone Number checker..

    
    
}
