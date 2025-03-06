package src.com;

public class PhoneNumException extends Exception {
    @Override
    public String toString(){
        return "Wrong Phone Number";
    }
    @Override
    public String getMessage(){
        return "Wrong Phone Number";
    }
}
