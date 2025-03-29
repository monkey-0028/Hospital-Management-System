package src.com;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

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
    public static boolean isValidDate(String dateStr) {
        try {
            // Define the date format
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

            // Parse the input date
            LocalDate inputDate = LocalDate.parse(dateStr, formatter);

            // Get the current date
            LocalDate currentDate = LocalDate.now();

            // Return true if inputDate is today or in the future
            return !inputDate.isBefore(currentDate);
        } catch (DateTimeParseException e) {
            return false; // Return false if the date format is incorrect
        }
    }
    public static boolean isValidSlot(int n){
        if(n == 1 || n == 2 || n==3)  {
            return true;
        }
        return false;
    }

    // isSlotAvailabe is present in DataBase.java
    public static void main(String[] args) {
        System.out.println(isValidDate("2025-05-02"));
        System.out.println(isValidSlot(2));
    }
    
}
