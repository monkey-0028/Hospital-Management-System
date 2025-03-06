package src.com;

public class AdhaarException extends Exception{
    @Override
    public String toString(){
        return "Wrong Adhaar Number";
    }
    @Override
    public String getMessage(){
        return "Wrong Adhaar Number";
    }
}
