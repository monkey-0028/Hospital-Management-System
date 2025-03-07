package src.com;

public class Patient {
    private String name = "Null";
    private String phoneNum = "Null";
    private String AdharNum = "Null";
    private int age;
    private char sex;
    // appointment date and time

    


    public static void main(String[] args) {
        // Patient p = new Patient();
        // System.out.println(p    );
        System.out.println(UtilMethod.isAdhaar("12311111111a"));
        
    }

    // constructor
    Patient(String Adhaar)throws WrongDataException{
        if(UtilMethod.isAdhaar(Adhaar)){
            this.AdharNum = Adhaar;
        }
        else{
            throw new WrongDataException();
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
    public int getAge(){
        return this.age;
    }
    public char getSex(){
        return this.sex;
    }

    // setters
    void setName(String name    )throws WrongDataException{
        if(UtilMethod.isName(name)){
            this.name = name;
        }
        else{
            throw new WrongDataException();
        }
    }

    void setNumber(String Phonenumber    ) throws WrongDataException{
        if(UtilMethod.isPhoneNumber(Phonenumber)){
            this.phoneNum = Phonenumber;
        }
        else{
            throw new WrongDataException();
        }
    }

    void setSex(char sex    ) throws WrongDataException{
        if(UtilMethod.isSex(sex)){
            this.sex  = sex;
        }
        else{
            throw new WrongDataException();
        }
    }

    void setAge(int age ) throws WrongDataException{
        if(UtilMethod.isAge(age)){
            this.age = age;
        }
        else{
            throw new WrongDataException();
        }
    }

    // Method
    @Override
    public String toString(){
        return "Name: "+this.name+"\nNumber: "+this.phoneNum+"\nAdhaar: "+this.AdharNum+"\nAge: "+this.age+"\nSex: "+this.sex;
    }
    
    
}
