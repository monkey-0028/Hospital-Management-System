package src.com;

public class Test {
    public static void main(String[] args) {
        Manager aiimsHospital = new Manager("jdbc:mysql://localhost:3306/GoodHospital", "GoodUser", "GoodPass@123");
        System.out.println(aiimsHospital.getStatus());
        aiimsHospital.init();
        System.out.println(aiimsHospital.getStatus());
        Patient p1 = aiimsHospital.createPatient("123123123123");
        if(p1 == null){
            System.out.println("error");
        }
        Patient p2 = aiimsHospital.createPatient("987096456789");
        Patient p3 = aiimsHospital.createPatient("456123456789");
        if(p2 == null || p3 == null ){
            System.out.println("erro");
        }
        try{
            p1.setName("SatyaprakashRoy");
            p2.setName("Ritesh Bharti");    
            p3.setName("Ronita Patra");

            p1.setAge(21);
            p2.setAge(20);
            p3.setAge(20);

            p1.setNumber("+919060589045");
            p2.setNumber("+914567234567");
            p3.setNumber("+919570345678");

            p1.setSex('M');
            p2.setSex('M'); 
            p3.setSex('F'); 

        }
        catch(Exception e   ){
            System.out.println(e);
        }

        // check patient before adding
        if(aiimsHospital.checkPatientClass(p1)){
            aiimsHospital.addData(p1);
        }
        if(aiimsHospital.checkPatientClass(p2)){
            aiimsHospital.addData(p2);
        }
        if(aiimsHospital.checkPatientClass(p3)){
            aiimsHospital.addData(p3);
        }
        System.out.println(aiimsHospital.getStatus()); // ERR --> duplicate entry found!!

        // Searching
        Patient [] p;
        p = aiimsHospital.selectData("name = \"SatyaprakashRoy\""); // take notes here!!!
        System.out.println(aiimsHospital.getStatus());
        for(Patient item : p    ){
            System.out.println(item);
        }
        System.out.println(aiimsHospital.getStatus());
        System.out.println(aiimsHospital.isFreeSlot(1,"2025-02-02"));
        // aiimsHospital.addAppointment("32", "sd", 5, "dsaf");
        

        // adding appointments.
        Appointment a1 = new Appointment();
        a1.setAadhaar("123123123123");
        a1.setSlot(2);
        a1.setSymptom("Headache");
        a1.Setappointment_date("2025-04-02");
        aiimsHospital.addAppointment(a1);
        System.out.println(aiimsHospital.getStatus());

        Appointment a2 = new Appointment();
        a2.setAadhaar("456123456789");
        a2.setSlot(1);
        a2.setSymptom("Fever");
        a2.Setappointment_date("2025-04-03");
        aiimsHospital.addAppointment(a2);
        System.out.println(aiimsHospital.getStatus());

        Appointment a3 = new Appointment();
        a3.setAadhaar("987096456789");
        a3.setSlot(3);
        a3.setSymptom("Blood From anus, maybe piles(bawaseer)");
        a3.Setappointment_date("2025-05-03");
        aiimsHospital.addAppointment(a3);
        System.out.println(aiimsHospital.getStatus());

        Appointment [] a = aiimsHospital.selectAppointment("987096456789");
        System.out.println(a.length);
        System.out.println(a[0].getSymptoms());

    }
}
