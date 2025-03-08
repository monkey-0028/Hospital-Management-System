package src.com.Exception;

public class WrongNumberException extends Exception{
    @Override
    public String toString(){
        return "Wrong Phone Number Formatt.\nMake sure it starts with +91 and rest 10 digit.\nNo any other character is allowed other than Digits. \n Even space is now allowd";
    }
    @Override
    public String getMessage(){
        return toString();
    }
}
