import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.awt.*;
import java.util.ArrayList;
import src.com.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import javax.swing.filechooser.FileNameExtensionFilter;


public class Main {
    private static final ArrayList<String> patients = new ArrayList<>();
    static Manager m = new Manager("jdbc:mysql://localhost:3306/GoodHospital", "GoodUser", "GoodPass@123");
    static Patient[] patientResultSet;
    static Patient selectedPatient = null;
    static Appointment[] appointmentsResultSet;
    static Appointment selectedAppointment = null;
    static String status;
    static boolean removeButtonFlag = false;
    static boolean searchButtonFlag  = false;
    static boolean showAppointmentFlag = false;
    static boolean addAppointmentFlag = false;
    static JTextArea ta = null;

    static JLabel imageLabel = new JLabel("NO image selected", JLabel.CENTER);
    static ImageIcon img_global = null;

    public static void main(String[] args) {
        // initilize the database
        // Manager m = new Manager("jdbc:mysql://localhost:3306/GoodHospital", "GoodUser", "GoodPass@123");
        m.init();
        System.out.println(m.getStatus());
        
        
        // Create JFrame
        JFrame frame = new JFrame("Welcome");
        frame.setSize(600, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(100, 30, 30, 30));

        JLabel titleLabel = new JLabel("Welcome to ABC Hospital", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        frame.add(titleLabel, BorderLayout.NORTH);

        // Create buttons
        JButton searchBtn = new JButton("Search Patient");
        JButton addBtn = new JButton("Add Patient");
        JButton removeBtn = new JButton("Remove Patient");
        // JButton checkBtn = new JButton("Search Appointment History");
        // Get the largest button size (based on "Check for Appointment")
        Dimension buttonSize = searchBtn.getPreferredSize();

        // Apply the same size constraints to all buttons
        searchBtn.setPreferredSize(buttonSize);
        searchBtn.setMinimumSize(buttonSize);
        searchBtn.setMaximumSize(buttonSize);

        addBtn.setPreferredSize(buttonSize);
        addBtn.setMinimumSize(buttonSize);
        addBtn.setMaximumSize(buttonSize);

        removeBtn.setPreferredSize(buttonSize);
        removeBtn.setMinimumSize(buttonSize);
        removeBtn.setMaximumSize(buttonSize);

        // checkBtn.setPreferredSize(buttonSize);
        // checkBtn.setMinimumSize(buttonSize);
        // checkBtn.setMaximumSize(buttonSize);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
        buttonPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Add spacing and buttons
        searchBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        addBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        removeBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        // checkBtn.setAlignmentX(Component.CENTER_ALIGNMENT);

        buttonPanel.add(searchBtn);
        buttonPanel.add(Box.createVerticalStrut(10)); // Space between buttons
        buttonPanel.add(addBtn);
        buttonPanel.add(Box.createVerticalStrut(10));
        buttonPanel.add(removeBtn);
        buttonPanel.add(Box.createVerticalStrut(10));
        // buttonPanel.add(checkBtn);

        // Add button panel to main panel
        mainPanel.add(buttonPanel, BorderLayout.CENTER);

        // Add main panel to frame
        frame.add(mainPanel);

        // Make frame visible
        frame.setVisible(true);
        searchBtn.addActionListener(e -> {searchButtonFlag = true;openSearchPage(frame);});
        removeBtn.addActionListener(e -> {removeButtonFlag = true;openSearchPage(frame);});
        // checkBtn.addActionListener(e -> openSearchPage(frame));
        addBtn.addActionListener(e -> openAddPage(frame));
    }

    private static void openSearchPage(JFrame mainFrame) {
        JFrame searchFrame = new JFrame("Search Patient");
        searchFrame.setSize(500, 300);
        searchFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        searchFrame.setLocationRelativeTo(mainFrame);

        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        panel.setBackground(new Color(220, 240, 255));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.gridx = 0;
        gbc.gridy = 0;

        // Label for "Search By"
        JLabel searchByLabel = new JLabel("Search by:");
        panel.add(searchByLabel, gbc);

        // Drop-down menu for search options
        String[] searchOptions = { "All","Name", "Age", "Sex", "Aadhar", "Phone Number"};
        JComboBox<String> searchDropdown = new JComboBox<>(searchOptions);
        gbc.gridx = 1;
        panel.add(searchDropdown, gbc);

        // Label for "Value"
        gbc.gridx = 0;
        gbc.gridy = 1;
        JLabel valueLabel = new JLabel("Value:");
        panel.add(valueLabel, gbc);

        // TextField for value input
        gbc.gridx = 1;
        JTextField valueField = new JTextField(20);
        panel.add(valueField, gbc);

        // Warning Label for errors
        JLabel warningLabel = new JLabel(" ");
        warningLabel.setForeground(Color.RED);
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        panel.add(warningLabel, gbc);

        // Buttons Panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));

        JButton searchButton = new JButton("Search");
        JButton backButton = new JButton("Back");
        backButton.setBackground(Color.RED);
        backButton.setForeground(Color.WHITE);

        buttonPanel.add(searchButton);
        buttonPanel.add(backButton);

        gbc.gridy = 3;
        panel.add(buttonPanel, gbc);

        // Search Button Action
        searchButton.addActionListener(e -> {
            String selectedCategory = (String) searchDropdown.getSelectedItem();
            String inputValue = valueField.getText().trim();
            StringBuilder errorMessage = new StringBuilder();

            // Validate input based on category
            if (selectedCategory.equals("Name")) {
                if (!inputValue.matches("[a-zA-Z\\s]+")) {
                    errorMessage.append("Invalid Name. Only letters and spaces are allowed.");
                    status = "ERR --> Wrong value entered in Name field";
                }
                else{
                    patientResultSet = m.selectData("name = \""+inputValue+"\"");
                    System.out.println(m.getStatus());
                    status = "Ok --> patientResutlSet contain item searched by --> name";
                }
            } else if (selectedCategory.equals("Age")) {
                if (!inputValue.matches("\\d+") || Integer.parseInt(inputValue) <= 0 || Integer.parseInt(inputValue) >= 150) {
                    errorMessage.append("Invalid Age. Must be a number between 1 and 150.");
                    status = "ERR --> Wrong value entered in age field";
                }
                else{
                    patientResultSet = m.selectData("age = "+inputValue);
                    System.out.println(m.getStatus());
                    status = "Ok --> patientResultSet contain item searched by --> Age";
                }
            } else if (selectedCategory.equals("Sex")) {
                if (!inputValue.equalsIgnoreCase("M") && !inputValue.equalsIgnoreCase("F")) {
                    errorMessage.append("Invalid Sex. Enter 'M' or 'F' only.");
                    status = "ERR --> Wrong value entered in sex field";
                }
                else{
                    patientResultSet = m.selectData("sex = '"+inputValue+"'");
                    System.out.println(m.getStatus());
                    status = "Ok --> patientResutlSet contain item searched by sex";
                }
            } else if (selectedCategory.equals("Aadhar")) {
                if (!inputValue.matches("\\d{12}")) {
                    errorMessage.append("Invalid Aadhar. Must be a 12-digit number.");
                    status = "ERR --> Wrong value entered in aadhar field";
                }
                else{
                    patientResultSet = m.selectData("aadhaar = \""+inputValue+"\"");
                    System.out.println(m.getStatus());
                    status = "Ok --> patientREsultSet contain item searched by aadhaar";
                }
            } else if (selectedCategory.equals("Phone Number")) {
                if (!inputValue.matches("\\+91\\d{10}")) {
                    errorMessage.append("Invalid Phone Number. Must start with +91 and be 10 digits long.");
                    status = "ERR --> Invalid phone number entered in field";
                }
                else{
                    patientResultSet = m.selectData("number = \""+inputValue+"\"");
                    System.out.println(m.getStatus());
                    status = "Ok --> patientResultSet contain item searched by number";
                }
            } else if(selectedCategory.equals("All")){
                patientResultSet = m.selectData("all")  ;
                System.out.println("all data searched");
            }

            // Display error or add patient data
            if (errorMessage.length() > 0) {
                warningLabel.setText("<html><b>Error:</b> " + errorMessage + "</html>");
            } else {
                warningLabel.setText(" ");

                // Add new patient record
                // ArrayList<String> patientData = new ArrayList<>();
                // patientData.add(selectedCategory + ": " + inputValue);

                // ArrayList<String> resultFound = new ArrayList<>();
                for(Patient item : patientResultSet){
                    patients.add(item.toString()); 
                }
                // patients.add(resultFound);
                openResultsPage(searchFrame);
                searchFrame.dispose();
                // JOptionPane.showMessageDialog(addPatientFrame, "Patient Data Added Successfully!");
                // valueField.setText("");
            }
        });
        // Back Button Action
        backButton.addActionListener(e -> {
            searchButtonFlag = false;
            removeButtonFlag = false;
            showAppointmentFlag = false;
            patientResultSet = null;
            appointmentsResultSet = null;
            selectedPatient = null;
            selectedAppointment = null;
            

            patients.clear();
            searchFrame.dispose();
        }); // back button ISSUE: clear the array.

        searchFrame.add(panel);
        searchFrame.setVisible(true);
    }

    private static void openAddPage(JFrame mainFrame) {
        JFrame searchFrame;
        if(addAppointmentFlag){
            searchFrame = new JFrame("Add Appointment");
        }
        else{
            searchFrame = new JFrame("Add Patient");
        }
        searchFrame.setSize(800, 500);
        searchFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        searchFrame.setLocationRelativeTo(mainFrame);

        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        panel.setBackground(new Color(220, 240, 255));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);

        // JLabel[] labels;
        // JTextField[] textFields;

        JLabel[] labels = { new JLabel("Name:"), new JLabel("Age:"), new JLabel("Sex (M/F):"), new JLabel("Aadhar:"),
                new JLabel("Phone Number:") , new JLabel("Appointment Date: "),new JLabel("Slot(1/2/3)"),new JLabel("Symptoms: ")};
        JTextField[] textFields = { new JTextField(20), new JTextField(20), new JTextField(20), new JTextField(20), new JTextField(20),
                new JTextField(20) , new JTextField(20), new JTextField(20)};
        


        JButton slotInfo = new JButton("?");
        slotInfo.setSize(new Dimension(5,5));

        slotInfo.addActionListener(e ->{
            JOptionPane.showMessageDialog(panel, "Slot 1 :- 9am - 12pm\nSlot 2 :- 1pm - 5pm\nSlot 3 :- 6pm - 10pm","Slot Info",JOptionPane.WARNING_MESSAGE);;
        });

        JButton dateSyntaxInfo = new JButton("?");
        dateSyntaxInfo.setSize(new Dimension(5,5));

        dateSyntaxInfo.addActionListener(e->{
            JOptionPane.showMessageDialog(panel, "Write Date in format: \nYYYY-MM-DD","Date Syntax",JOptionPane.WARNING_MESSAGE);;
        });

        int i = 0;

        for (i=0; i < labels.length; i++) {
            gbc.gridx = 0;
            gbc.gridy = i;
            panel.add(labels[i], gbc);

            gbc.gridx = 1;
            
            if(i == labels.length-1){
                
                ta = new JTextArea("");
                ta.setPreferredSize(new Dimension(300,50));
                ta.setLineWrap(true);   
                ta.setWrapStyleWord(true);
                panel.add(ta,gbc);
            }
            if(textFields[i] != null){
                panel.add(textFields[i], gbc);
            }
            if( i == labels.length -2){
                gbc.gridx = 2;
                panel.add(slotInfo,gbc);
            }
            if( i == labels.length -3){
                gbc.gridx = 2;
                panel.add(dateSyntaxInfo,gbc);
            }
        }

        imageLabel = new JLabel("No Image Selected", SwingConstants.CENTER);
        gbc.gridx = 0;
        gbc.gridy = i;
        panel.add(imageLabel, gbc);

        JButton selectImageButton = new JButton("Select Image");
        //image selector 
        selectImageButton.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setFileFilter(new FileNameExtensionFilter("Image Files", "jpg", "png", "jpeg"));

            int result = fileChooser.showOpenDialog(null);

            if (result == JFileChooser.APPROVE_OPTION) {
                File selectedFile = fileChooser.getSelectedFile();
                ImageIcon imageIcon = new ImageIcon(selectedFile.getAbsolutePath());
                img_global = imageIcon;
    
                // Resize Image
                Image image = imageIcon.getImage().getScaledInstance(200, 200, Image.SCALE_SMOOTH);
                imageLabel.setIcon(new ImageIcon(image));
                imageLabel.setText("");  // Remove text after setting image
            }



        });
        
        
        
        gbc.gridx = 1;
        gbc.gridy = i;
        panel.add(selectImageButton, gbc);

        
        

        JLabel warningLabel = new JLabel(" ");
        warningLabel.setForeground(Color.RED);
        gbc.gridx = 1;
        gbc.gridy = labels.length;
        panel.add(warningLabel, gbc);

        JButton searchButton = new JButton("Add Patient"); // search button is the name. waah!!
        gbc.gridy = labels.length + 1;
        panel.add(searchButton, gbc);

        JButton backButton = new JButton("Back");
        backButton.setBackground(Color.RED);
        backButton.setForeground(Color.WHITE);
        gbc.gridy = labels.length + 2;
        panel.add(backButton, gbc);

        searchButton.addActionListener(e -> {
            ArrayList<String> patientData = new ArrayList<>();
            StringBuilder errorMessage = new StringBuilder();
            String name = textFields[0].getText().trim();
            String age = textFields[1].getText().trim();
            String sex = textFields[2].getText().trim();
            String aadhar = textFields[3].getText().trim();
            String phone = textFields[4].getText().trim();
            String appointment_date = textFields[5].getText().trim();
            String  slot = textFields[ 6].getText().trim();
            String symp = ta.getText().trim();

            // System.out.println(appointment_date);
            // System.out.println(slot);
            // System.out.println(symp);
            
            Patient p = null;
            Appointment a =new Appointment();
            if (!aadhar.matches("\\d{12}"))
                errorMessage.append("Invalid Aadhar. ");
            else{
                p = m.createPatient(aadhar);
                a.setAadhaar(aadhar);
            }
            //name
            if (!name.matches("[a-zA-Z\\s]*"))
                errorMessage.append("Invalid Name. ");
            else{
                try{
                    p.setName(name);
                }
                catch(Exception er   ){
                    System.out.println(er);
                }
            }
            // age
            if (!age.matches("\\d+") || Integer.parseInt(age) <= 0 || Integer.parseInt(age) >= 150)
                errorMessage.append("Invalid Age. ");
            else{
                try{

                    p.setAge(Integer.parseInt(age));
                }
                catch(Exception er  ){
                    System.out.println(er   );
                }
            }
            //sex
            if (!sex.equals("M") && !sex.equals("F"))
                errorMessage.append("Invalid Sex.M or F only. ");
            else{
                try{
                    p.setSex(sex.charAt(0));
                }
                catch(Exception er   ){
                    System.out.println(er    );
                }
            }
            // image
            if(img_global != null){
                byte [] imageIconTOByte;
                Image img = img_global.getImage();
                BufferedImage bufferedImage = new BufferedImage(img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_INT_ARGB);
                Graphics2D g2d = bufferedImage.createGraphics();
                g2d.drawImage(img, 0, 0, null);
                g2d.dispose();

                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                try{
                    ImageIO.write(bufferedImage, "png", baos);
                    imageIconTOByte = baos.toByteArray();
                    p.setImage(imageIconTOByte);
                }
                catch(Exception ee){
                    System.out.println(ee);
                }

            }
            // phone
            if (!phone.matches("\\+91\\d{10}")){
                errorMessage.append("Invalid Phone Number. ");
            }
            else{
                try{
                    p.setNumber(phone);
                }
                catch(Exception err ){
                    System.out.println(err  );
                }
            }
            // create uitlMethods first
            // appointment date
            if (!UtilMethod.isValidDate(appointment_date)){
                errorMessage.append("Wrong date, check format");
            }
            else{
                a.Setappointment_date(appointment_date);
            }
            // symptoms
            if(symp == null){
                errorMessage.append("symp is empoty");
            }
            else{
                a.setSymptom(symp);
            }
            // slot : check slot is valid or not then check slot is free or not
            if(!(UtilMethod.isValidSlot(Integer.parseInt(slot)))){
                errorMessage.append("Wrong slot value");
                System.out.println(Integer.parseInt(slot));
            }
            else{
                //check if available or not
                if(UtilMethod.isValidDate(appointment_date)){
                    if(m.isFreeSlot(Integer.parseInt(slot), appointment_date)){
                        a.setSlot(Integer.parseInt(slot));
                    }
                    else{
                        JOptionPane.showMessageDialog(panel,"slot "+slot +"is full, try another one","no slot available", JOptionPane.WARNING_MESSAGE);
                    }
                }
                
            }

            if (errorMessage.length() > 0) {
                warningLabel.setText("Error: " + errorMessage.toString());
            } else {
                warningLabel.setText(" ");
                patientData.add(name);
                patientData.add(age);
                patientData.add(sex);
                patientData.add(aadhar);
                patientData.add(phone);
                // patients.add(patientData);
                if(m.checkPatientClass(p) && m.checkAppointmentClass(a)){
                    m.addData(p);
                    m.addAppointment(a);
                    System.out.println("added data");
                    status = "Ok --> Data added!!";
                    JOptionPane.showMessageDialog(panel,"Data added succesfully","Success", JOptionPane.WARNING_MESSAGE);
                    searchFrame.dispose();
                }
                else{
                    System.out.println("didn't added any data");
                }
            }
            
        });

        backButton.addActionListener(e -> {
            searchButtonFlag = false;
            removeButtonFlag = false;
            showAppointmentFlag = false;
            patientResultSet = null;
            appointmentsResultSet = null;
            selectedPatient = null;
            selectedAppointment = null;
            

            patients.clear();
            searchFrame.dispose();
        }); // back button ISSUE: clear the array.

        searchFrame.add(panel);
        searchFrame.setVisible(true);

    }

    private static void openResultsPage(JFrame parentFrame) {
        JFrame resultsFrame = new JFrame("Search Results");
        resultsFrame.setSize(500, 400);
        resultsFrame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        resultsFrame.setLocationRelativeTo(parentFrame);

        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());

        JLabel resultLabel = new JLabel("Result Found: " + patients.size() + " Patients", JLabel.CENTER);
        resultLabel.setFont(new Font("Arial", Font.BOLD, 16));
        panel.add(resultLabel, BorderLayout.NORTH);

        DefaultListModel<String> listModel = new DefaultListModel<>();
        JList<String> list = new JList<>(listModel);

        if(showAppointmentFlag){
            for(int i = 0;i<appointmentsResultSet.length ; i++){
                listModel.addElement(appointmentsResultSet[i].toString());
            }
        }
        else{
            for (int i = 0; i < patients.size(); i++) {
                listModel.addElement(patients.get(i));
            }
        }

        list.addListSelectionListener(e -> {
            if(!e.getValueIsAdjusting()){
                int selectedIndex = list.getSelectedIndex(); // selected index here!!!!!
                if(selectedIndex != -1){
                    System.out.println(selectedIndex);
                    if(showAppointmentFlag){
                        selectedAppointment = appointmentsResultSet[selectedIndex];
                    }
                    else{
                        selectedPatient = patientResultSet[selectedIndex];
                        System.out.println(selectedPatient);
                    }
                }
            }
        });

        
        panel.add(new JScrollPane(list), BorderLayout.CENTER);

        JButton backButton = new JButton("Back");
        backButton.setFont(new Font("Arial", Font.BOLD, 14));
        backButton.setBackground(new Color(220, 53, 69)); // Bootstrap danger color
        backButton.setForeground(Color.WHITE);
        backButton.setFocusPainted(false);
        backButton.setBorder(new LineBorder(new Color(200, 53, 69), 2, true));
        backButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
       

        JButton apButton = new JButton("show appointments");
        apButton.setFont(new Font("Arial", Font.BOLD, 14));
        apButton.setBackground(new Color(36, 160, 237)); // Bootstrap danger color
        apButton.setForeground(Color.WHITE);
        apButton.setFocusPainted(false);
        apButton.setBorder(new LineBorder(new Color(36, 160, 237), 2, true));
        apButton.setCursor(new Cursor(Cursor.HAND_CURSOR));

        JButton addAppBtn = new JButton("Add appointment");
        addAppBtn.setFont(new Font("Arial", Font.BOLD, 14));
        addAppBtn.setBackground(new Color(112,128,144)); // Bootstrap danger color
        addAppBtn.setForeground(Color.WHITE);
        addAppBtn.setFocusPainted(false);
        addAppBtn.setBorder(new LineBorder(new Color(112,128,144), 2, true));
        addAppBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        JButton detailApButton = new JButton("Expand appointment");
        detailApButton.setFont(new Font("Arial", Font.BOLD, 14));
        detailApButton.setBackground(new Color(36, 160, 237)); // Bootstrap danger color
        detailApButton.setForeground(Color.WHITE);
        detailApButton.setFocusPainted(false);
        detailApButton.setBorder(new LineBorder(new Color(36, 160, 237), 2, true));
        detailApButton.setCursor(new Cursor(Cursor.HAND_CURSOR));

        JButton removeButtonn = new JButton("Remove selected Item");
        removeButtonn.setFont(new Font("Arial", Font.BOLD, 14));
        removeButtonn.setBackground(new Color(170, 40, 55)); // Bootstrap danger color
        removeButtonn.setForeground(Color.WHITE);
        removeButtonn.setFocusPainted(false);
        removeButtonn.setBorder(new LineBorder(new Color(170, 40, 55), 2, true));
        removeButtonn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        JButton showProfileBtn = new JButton("Show Profile");
        showProfileBtn.setFont(new Font("Arial", Font.BOLD, 14));
        showProfileBtn.setBackground(new Color(40, 170, 100)); // Bootstrap danger color
        showProfileBtn.setForeground(Color.WHITE);
        showProfileBtn.setFocusPainted(false);
        showProfileBtn.setBorder(new LineBorder(new Color(40, 170, 100), 2, true));
        showProfileBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        JButton cancelApntBUTTON = new JButton("Cancel Appointment");
        cancelApntBUTTON.setFont(new Font("Arial", Font.BOLD, 14));
        cancelApntBUTTON.setBackground(new Color(220, 53, 69)); // Bootstrap danger color
        cancelApntBUTTON.setForeground(Color.WHITE);
        cancelApntBUTTON.setFocusPainted(false);
        cancelApntBUTTON.setBorder(new LineBorder(new Color(200, 53, 69), 2, true));
        cancelApntBUTTON.setCursor(new Cursor(Cursor.HAND_CURSOR));

        addAppBtn.addActionListener(e -> {
            if(selectedPatient != null){
                addAppointmentFlag = true;
                // System.out.println("yes");
                // openAddAppointPage(resultsFrame);
                openAddAppointmentPage(resultsFrame);
            }
        });



        
        
        JPanel buttonP = new JPanel(new GridLayout(4,1));
        buttonP.add(backButton);

        if(searchButtonFlag){
            buttonP.add(apButton);
            buttonP.add(showProfileBtn);
            buttonP.add(addAppBtn);
        }
        if(showAppointmentFlag){
            buttonP.add(detailApButton);
            buttonP.add(cancelApntBUTTON);
        }
        if(removeButtonFlag){
            buttonP.add(removeButtonn);
            buttonP.add(showProfileBtn);
        }

        resultsFrame.add(buttonP,BorderLayout.SOUTH);

        
        backButton.addActionListener(e -> {
            searchButtonFlag = false;
            removeButtonFlag = false;
            showAppointmentFlag = false;
            patientResultSet = null;
            appointmentsResultSet = null;
            selectedPatient = null;
            selectedAppointment = null;
            

            patients.clear();
            resultsFrame.dispose();
        }); // back button ISSUE: clear the array.

        //apButton listner
        apButton.addActionListener(e -> {
            if(selectedPatient != null){
                resultsFrame.dispose();
                showAppointmentFlag = true;
                // store all appointment in patientResultSet.
                appointmentsResultSet = m.selectAppointment(selectedPatient.getAdhaarNum());
                searchButtonFlag = false;
                removeButtonFlag = false;
                patientResultSet = null;
                // selectedPatient = null;
                openResultsPage(parentFrame);
                resultsFrame.dispose();
            }
            else{
                JOptionPane.showMessageDialog(panel, "First select something","No Option Selected",JOptionPane.WARNING_MESSAGE);;
            }
        });
        resultsFrame.add(panel);
        resultsFrame.setVisible(true);

        // detail button listner
        detailApButton.addActionListener(e->{
            if(selectedAppointment != null){
                openBannerPage(parentFrame,selectedAppointment,selectedPatient);
            }
            else{
                System.out.println("Select somethinng");
            }
        });
        // remove button listner
        removeButtonn.addActionListener(e->{
            if(selectedPatient != null){
                int choice = JOptionPane.showConfirmDialog(
                    null,
                    "Do you want to delete selected item?",
                    "Warning",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.WARNING_MESSAGE
                );
                if(choice == JOptionPane.YES_OPTION){
                    m.deleteData("aadhaar = \""+selectedPatient.getAdhaarNum()+"\"");
                    Patient [] refreshedArray = new Patient[patientResultSet.length-1];
                    
                    int i =0;
                    for(Patient item : patientResultSet){
                        if(item != selectedPatient){
                            refreshedArray[i] = item;
                            i++;
                        }
                    }
                    patientResultSet = refreshedArray;
                    
                    patients.clear();

                    for(Patient item : patientResultSet){
                        patients.add(item.toString()); 
                    }
                    selectedPatient = null;
                    openResultsPage(parentFrame);
                    resultsFrame.dispose();
                    
                }
                else{
                    System.out.println("NO");
                }
            }

        });
        
        // remove cancel the appointment
        cancelApntBUTTON.addActionListener(e -> {
            if(selectedAppointment != null){
                int choice = JOptionPane.showConfirmDialog(
                    null,
                    "Do you want to delete selected item?",
                    "Warning",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.WARNING_MESSAGE
                );
                if(choice == JOptionPane.YES_OPTION){
                    m.deleteApp(selectedAppointment);
                    // refresh the appointment list here
                    // Patient [] refreshedArray = new Patient[patientResultSet.length-1];
                    Appointment [] refreshedApp = new Appointment[appointmentsResultSet.length - 1];
                    
                    int i =0;
                    for(Appointment item : appointmentsResultSet){
                        if(item != selectedAppointment){
                            refreshedApp[i] = item;
                            i++;
                        }
                    }
                    appointmentsResultSet = refreshedApp;
                    
                    // patients.clear();

                    // for(Patient item : patientResultSet){
                    //     patients.add(item.toString()); 
                    // }
                    // selectedPatient = null;
                    selectedAppointment = null;
                    openResultsPage(parentFrame);
                    resultsFrame.dispose();
                    
                }

            }
            else{
                System.out.println("NO");
            }
         });

        showProfileBtn.addActionListener(e -> {
            if(selectedPatient == null){
                JOptionPane.showMessageDialog(panel, "First select something","No Option Selected",JOptionPane.WARNING_MESSAGE);;
            }
            else{
                openProfilePage(parentFrame);
            }
        });
    }
    private static void openBannerPage(JFrame parentFrame,Appointment a,Patient p) {
        JFrame resultsFrame = new JFrame("Banner Page");
        resultsFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        resultsFrame.setSize(600, 400);
        resultsFrame.setLocationRelativeTo(parentFrame); // Center it

        // String data = "";
        // data += String.format("Name: %s\nAge: %d\nSex: %c",p.getName(),p.getAge(),p.getSex());
        // data += String.format("\n\nAdhaar: %s\n\nPhone: %s",p.getAdhaarNum(),p.getPhoneNum());

        // JPanel panel = new JPanel();
        // panel.add(new JLabel(data));

        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.anchor = GridBagConstraints.CENTER;
        // gbc.gridy = GridBagConstraints.CENTER;

        gbc.insets = new Insets(5, 0, 0, 0);

        // panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS)); // Stack labels vertically


        JLabel nameLabel = new JLabel(String.format("Name: %s", p.getName()));
        gbc.gridy = 0;
        panel.add(nameLabel,gbc);

        JLabel ageLabel = new JLabel("Age: "+p.getAge());
        gbc.gridy = 1;
        panel.add(ageLabel,gbc);

        JLabel sexLabel = new JLabel("Sex: "+p.getSex());
        gbc.gridy = 2;
        panel.add(sexLabel,gbc);

        gbc.gridy = 3;
        panel.add(new Label(" "),gbc);

        JLabel aadhaarLabel = new JLabel("Aadhaar: "+p.getAdhaarNum());
        gbc.gridy = 4;
        panel.add(aadhaarLabel,gbc);
        JLabel phoneLabel = new JLabel("Phone: "+p.getPhoneNum());
        gbc.gridy = 5;
        panel.add(phoneLabel,gbc);

        gbc.gridy = 6;
        panel.add(new Label(" "),gbc);

        JLabel apDateLabel = new JLabel("Appointment Date: "+a.getAppointment_date());
        gbc.gridy = 7;
        panel.add(apDateLabel,gbc);
        JLabel slotLabel = new JLabel("Slot: "+a.getSlot());
        gbc.gridy = 8;
        panel.add(slotLabel,gbc);

        gbc.gridy = 9;
        panel.add(new Label(" "),gbc);

        JLabel sympLabel = new JLabel("--Symptoms--");
        gbc.gridy = 10;
        panel.add(sympLabel,gbc);

        // symp label
        JTextArea LastLabel = new JTextArea("| \" "+a.getSymptoms()+" \" ");
        LastLabel.setLineWrap(true);  // Enable word wrapping
        LastLabel.setWrapStyleWord(true);
        LastLabel.setEditable(false); // Make it behave like a label
        LastLabel.setOpaque(false);   // Make background match JPanel
        LastLabel.setBorder(null);    // Remove border (optional)
        LastLabel.setPreferredSize(new Dimension(200, 40)); // Set fixed width and height
        LastLabel.setCaretPosition(0);
        LastLabel.setFocusable(false);
        

        gbc.gridy = 11;
        panel.add(LastLabel, gbc);

        

        resultsFrame.add(panel);
        resultsFrame.setVisible(true);
    }
    private static void openProfilePage(JFrame parentFrame) {
        JFrame resultsFrame = new JFrame("Profile");
        resultsFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        resultsFrame.setSize(600, 400);
        resultsFrame.setLocationRelativeTo(parentFrame); // Center it
        Patient p = selectedPatient;

        // String data = "";
        // data += String.format("Name: %s\nAge: %d\nSex: %c",p.getName(),p.getAge(),p.getSex());
        // data += String.format("\n\nAdhaar: %s\n\nPhone: %s",p.getAdhaarNum(),p.getPhoneNum());

        // JPanel panel = new JPanel();
        // panel.add(new JLabel(data));

        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.anchor = GridBagConstraints.CENTER;
        // gbc.gridy = GridBagConstraints.CENTER;

        gbc.insets = new Insets(5, 0, 0, 0);

        // panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS)); // Stack labels vertically`
        
        // JLabel imagLabel = null;
        // if(p.getImage() != null){
        //     byte [] img = p.getImage();
        //     ByteArrayInputStream bais = new ByteArrayInputStream(img);
        //     try{
        //         BufferedImage OriginalImage = ImageIO.read(bais);
        //         Image resizedImage = OriginalImage.getScaledInstance(200, 200, Image.SCALE_SMOOTH);
        //         ImageIcon iconImg= new ImageIcon(resizedImage);

        //         imagLabel = new JLabel();
        //         imageLabel.setIcon(iconImg);
        //     }
        //     catch(Exception ee  ){
        //         imageLabel.setText("Error in loading image");
        //         System.out.println(ee);
        //     }

        // }
        // else{
        //     imageLabel.setText("No image Found");
        // }

        // gbc.gridy = 0;
        // panel.add(imagLabel,gbc);

        JLabel imageLabel = new JLabel("");
        ImageIcon noPP = new ImageIcon("./no.png");
        Image resizedNoPP = noPP.getImage().getScaledInstance(220, 220, Image.SCALE_SMOOTH);
        noPP = new ImageIcon(resizedNoPP);

        if(p.getImage() != null){
            byte [] imageBytes = p.getImage();
            try {
                ByteArrayInputStream bais = new ByteArrayInputStream(imageBytes);
                BufferedImage originalImage = ImageIO.read(bais);
                
                // Resize image
                // Image resizedImage = originalImage.getScaledInstance(width, height, Image.SCALE_SMOOTH);
                // return new ImageIcon(resizedImage);
                Image resizedImage = originalImage.getScaledInstance(220, 220, Image.SCALE_SMOOTH);
                ImageIcon imageIcon = new ImageIcon(resizedImage);
                imageLabel.setIcon(imageIcon);
                imageLabel.setText("");
    
            } catch (Exception e) {
                imageLabel.setIcon(noPP);
                e.printStackTrace();
                
            }


            // imageLabel.setText("Found image");
        }
        else{
            imageLabel.setIcon(noPP);
        }
        
        gbc.gridy = 0;
        panel.add(imageLabel);

        JLabel nameLabel = new JLabel(String.format("Name: %s", p.getName()));
        gbc.gridy = 1;
        panel.add(nameLabel,gbc);

        JLabel ageLabel = new JLabel("Age: "+p.getAge());
        gbc.gridy = 2;
        panel.add(ageLabel,gbc);

        JLabel sexLabel = new JLabel("Sex: "+p.getSex());
        gbc.gridy = 3;
        panel.add(sexLabel,gbc);

        gbc.gridy = 4;
        panel.add(new Label(" "),gbc);

        JLabel aadhaarLabel = new JLabel("Aadhaar: "+p.getAdhaarNum());
        gbc.gridy = 5;
        panel.add(aadhaarLabel,gbc);
        JLabel phoneLabel = new JLabel("Phone: "+p.getPhoneNum());
        gbc.gridy = 6;
        panel.add(phoneLabel,gbc);

        gbc.gridy = 7;
        panel.add(new Label(" "),gbc);


        

        resultsFrame.add(panel);
        resultsFrame.setVisible(true);
    }

    public static void openAddAppointmentPage(JFrame resultsFrame) {
        // Create new frame
        JFrame appointmentFrame = new JFrame("Add Appointment");
        appointmentFrame.setSize(400, 250);
        appointmentFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        appointmentFrame.setLocationRelativeTo(resultsFrame); // Center relative to previous frame

        // Create panel and layout
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(4, 2, 10, 10)); // 4 rows: 3 fields + 1 button

        // Labels and fields
        JLabel slotLabel = new JLabel("Slot:");
        JTextField slotField = new JTextField();

        JLabel symptomsLabel = new JLabel("Symptoms:");
        JTextField symptomsField = new JTextField();

        JLabel dateLabel = new JLabel("Date:");
        JTextField dateField = new JTextField();

        // Submit button
        JButton submitButton = new JButton("Submit");

        submitButton.addActionListener(e -> {
            String date = dateField.getText().trim();
            String symp = symptomsField.getText().trim();
            String slot = slotField.getText().trim();
            int check = 0;
            if(UtilMethod.isValidDate(date) && symp != null && UtilMethod.isValidSlot(Integer.parseInt(slot))){
                // check for empty slot
                if(m.isFreeSlot(Integer.parseInt(slot), date)){
                    // a.setSlot(Integer.parseInt(slot));
                    // add the appointment here
                    Appointment a = new Appointment();
                    a.setAadhaar(selectedPatient.getAdhaarNum());   
                    a.setSlot(Integer.parseInt(slot));
                    a.setSymptom(symp);
                    a.Setappointment_date(date);
                    m.addAppointment(a);
                    if(m.getStatus().contains("Ok")){
                        JOptionPane.showMessageDialog(panel,"Added success!!","success", JOptionPane.WARNING_MESSAGE);
                        
                        // searchButtonFlag = false;
                        // removeButtonFlag = false;
                        // showAppointmentFlag = false;
                        // patientResultSet = null;
                        // appointmentsResultSet = null;
                        // selectedPatient = null;
                        // selectedAppointment = null;
                        // addAppointmentFlag = false;

                        appointmentFrame.dispose();
                    }
                    else{
                        JOptionPane.showMessageDialog(panel,m.getStatus(),"something went wrong", JOptionPane.WARNING_MESSAGE);
                    }

                }
                else{
                    JOptionPane.showMessageDialog(panel,"slot "+slot +"is full, try another one","no slot available", JOptionPane.WARNING_MESSAGE);
                }
            }
            else{
                JOptionPane.showMessageDialog(panel,"please check the syntax, and try again","wrong details entered", JOptionPane.WARNING_MESSAGE);

            }
        });

        // Add components to panel
        panel.add(slotLabel);
        panel.add(slotField);

        panel.add(symptomsLabel);
        panel.add(symptomsField);

        panel.add(dateLabel);
        panel.add(dateField);

        // Empty label to align the button properly
        panel.add(new JLabel()); 
        panel.add(submitButton);

        // Add panel to frame
        appointmentFrame.add(panel);
        appointmentFrame.setVisible(true);
    }
    
}
