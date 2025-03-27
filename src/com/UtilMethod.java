package src.com;

public class UtilMethod {
    // checks Adhaar number validity
    public static boolean isAdhaar(String adhaar){
        if(adhaar.length() == 12 && adhaar.matches("\\d+")){
            return true;
        }
        return false;
    }
    public static boolean isName(String name    ){
        if(name.matches("[a-z|A-Z|\\s]*")){
            return true;
        }
        return false;
    }

    public static boolean isPhoneNumber(String phoneNum ){
        if(phoneNum.matches("\\+91\\d{10}")){
            return true;
        }
        return false;
    }
    public static boolean isSex(char c  ){
        if(c == 'M' || c == 'F'){
            return true;
        }
        else{
            return false;
        }
    }
    public static boolean isAge(int age ){
        if(age>0 && age <150){
            return true;
        }
        else{
            return false;
        }
    }
    
}
