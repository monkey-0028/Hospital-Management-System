package src.com;
public class Manager {
    public static void main(String[] args) {
        Manager m = new Manager();
        Patient p = m.createPatient("1234") ;
        System.out.println(p);
        
        return;
    }
    @SuppressWarnings("finally")
    public Patient createPatient(String Adhaar){
        try{
            return new Patient(Adhaar);
        }
        catch(Exception e   ){
            System.out.println(e    );
        }
        finally{
            return null;
        }
    }

}
