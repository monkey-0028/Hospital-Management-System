package src.com.Exception;

public class WrongAgeException extends Exception {
    @Override
    public String toString(){
        return "Wrong Age Formatt!!\n Age must be in range(0,150)";
    }
    @Override
    public String getMessage(){
        return toString();
    }
}
