package src.com;
import src.com.Exception.*;


public class Patient {
    private String name = "Null";
    private String phoneNum = "Null";
    private String AdharNum = "Null";
    private int age=-1;
    private char sex='n';
    // appointment date and time

    // constructor
    Patient(String Adhaar)throws WrongAdhaarException{
        if(UtilMethod.isAdhaar(Adhaar)){
            this.AdharNum = Adhaar;
        }
        else{
            throw new WrongAdhaarException();
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
    void setName(String name    )throws WrongNameExcaption{
        if(UtilMethod.isName(name)){
            this.name = name;
        }
        else{
            throw new WrongNameExcaption();
        }
    }

    void setNumber(String Phonenumber    ) throws WrongNumberException{
        if(UtilMethod.isPhoneNumber(Phonenumber)){
            this.phoneNum = Phonenumber;
        }
        else{
            throw new WrongNumberException();
        }
    }

    void setSex(char sex    ) throws WrongSexException{
        if(UtilMethod.isSex(sex)){
            this.sex  = sex;
        }
        else{
            throw new WrongSexException();
        }
    }

    void setAge(int age ) throws WrongAgeException{
        if(UtilMethod.isAge(age)){
            this.age = age;
        }
        else{
            throw new WrongAgeException();
        }
    }

    // Method
    @Override
    public String toString(){
        return "Name: "+this.name+"\nNumber: "+this.phoneNum+"\nAdhaar: "+this.AdharNum+"\nAge: "+this.age+"\nSex: "+this.sex;
    }
    
    
}
