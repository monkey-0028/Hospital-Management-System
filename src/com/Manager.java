package src.com;

public class Manager extends DataBase{
    // Constructor
    Manager(String url,String user, String pass ){
        super(url, user, pass);
    }
    public static void main(String[] args) {
        Manager m = new Manager("jdbc:mysql://localhost:3306/GoodHospital", "GoodUser", "GoodPass@123");
        Patient p  = m.createPatient("122122222222"); 
        if(p!=null){
            try{
                p.setAge(5);
                p.setName("Satyaprakash");
                p.setNumber("+919060589069");
                p.setSex('M');
                if(m.checkPatientClass(p)){
                    m.addData(p);
                }
                else{
                    System.out.println("Patient data sucks");
                }
            }
            catch(Exception e   ){
                System.out.println(e);
            }
        }
        else{
            System.out.println("Something went Wrong");
        }

        return;
    }

    public Patient createPatient(String Adhaar){
        try{
            return new Patient(Adhaar);
        }
        catch(Exception e   ){
            System.out.println(e    );
            return null;
        }
    }
    boolean checkPatientClass(Patient p ){
        if(p.getName() == "NULL"){
            return false;
        }
        if(p.getPhoneNum() == "NULL"){
            return false;
        }
        if(p.getAge() == -1){
            return false;
        }
        if(p.getSex() == 'n'){
            return false;
        }
        return true;
    }

}
