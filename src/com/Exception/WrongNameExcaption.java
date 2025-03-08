package src.com.Exception;

public class WrongNameExcaption extends Exception {
    @Override
    public String toString(){
        return "Wrong Name Format! Make sure Name doesn't contain any Numberical Value";
    }
    @Override
    public String getMessage(){
        return toString();
    }
}
