package src.com.Exception;

public class WrongSexException extends Exception {
    @Override
    public String toString(){
        return "Wrong Sex Formatt!!\nOnly M and F are allowed!";
    }
    @Override
    public String getMessage(){
        return toString();
    }
}
