package src.com;

public class Patient {
    private String name = "Null";
    private String phoneNum = "Null";
    private String AdharNum = "Null";


    public static void main(String[] args) {
        Patient p = new Patient();
        System.out.println(p    );
        
        
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
    void setAdhaarNum(String Adhaar ){
        this.AdharNum = Adhaar;
    }

    // Method
    @Override
    public String toString(){
        return "Name: "+this.name+"\nNumber: "+this.phoneNum+"\nAdhaar: "+this.AdharNum;
    }

    
    
}
