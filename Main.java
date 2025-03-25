import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.util.ArrayList;


public class Main {
    private static final ArrayList<ArrayList<String>> patients = new ArrayList<>();

    public static void main(String[] args) {
        // Create JFrame
        JFrame frame = new JFrame("Welcome");
        frame.setSize(300, 300);
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
        JButton checkBtn = new JButton("Check for Appointment");
        // Get the largest button size (based on "Check for Appointment")
        Dimension buttonSize = checkBtn.getPreferredSize();

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

        checkBtn.setPreferredSize(buttonSize);
        checkBtn.setMinimumSize(buttonSize);
        checkBtn.setMaximumSize(buttonSize);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
        buttonPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Add spacing and buttons
        searchBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        addBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        removeBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        checkBtn.setAlignmentX(Component.CENTER_ALIGNMENT);

        buttonPanel.add(searchBtn);
        buttonPanel.add(Box.createVerticalStrut(10)); // Space between buttons
        buttonPanel.add(addBtn);
        buttonPanel.add(Box.createVerticalStrut(10));
        buttonPanel.add(removeBtn);
        buttonPanel.add(Box.createVerticalStrut(10));
        buttonPanel.add(checkBtn);

        // Add button panel to main panel
        mainPanel.add(buttonPanel, BorderLayout.CENTER);

        // Add main panel to frame
        frame.add(mainPanel);

        // Make frame visible
        frame.setVisible(true);
        searchBtn.addActionListener(e -> openSearchPage(frame));
        removeBtn.addActionListener(e -> openSearchPage(frame));
        checkBtn.addActionListener(e -> openSearchPage(frame));
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
        String[] searchOptions = { "Name", "Age", "Sex", "Aadhar", "Phone Number" };
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
                }
            } else if (selectedCategory.equals("Age")) {
                if (!inputValue.matches("\\d+") || Integer.parseInt(inputValue) <= 0 || Integer.parseInt(inputValue) >= 150) {
                    errorMessage.append("Invalid Age. Must be a number between 1 and 150.");
                }
            } else if (selectedCategory.equals("Sex")) {
                if (!inputValue.equalsIgnoreCase("M") && !inputValue.equalsIgnoreCase("F")) {
                    errorMessage.append("Invalid Sex. Enter 'M' or 'F' only.");
                }
            } else if (selectedCategory.equals("Aadhar")) {
                if (!inputValue.matches("\\d{12}")) {
                    errorMessage.append("Invalid Aadhar. Must be a 12-digit number.");
                }
            } else if (selectedCategory.equals("Phone Number")) {
                if (!inputValue.matches("\\+91\\d{10}")) {
                    errorMessage.append("Invalid Phone Number. Must start with +91 and be 10 digits long.");
                }
            }

            // Display error or add patient data
            if (errorMessage.length() > 0) {
                warningLabel.setText("<html><b>Error:</b> " + errorMessage + "</html>");
            } else {
                warningLabel.setText(" ");

                // Add new patient record
                ArrayList<String> patientData = new ArrayList<>();
                patientData.add(selectedCategory + ": " + inputValue);
                patients.add(patientData);
                openResultsPage(searchFrame);
                // JOptionPane.showMessageDialog(addPatientFrame, "Patient Data Added Successfully!");
                // valueField.setText("");
            }
        });
        // Back Button Action
        backButton.addActionListener(e -> searchFrame.dispose());

        searchFrame.add(panel);
        searchFrame.setVisible(true);
    }

    private static void openAddPage(JFrame mainFrame) {
        JFrame searchFrame = new JFrame("Add Patient");
        searchFrame.setSize(500, 500);
        searchFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        searchFrame.setLocationRelativeTo(mainFrame);

        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        panel.setBackground(new Color(220, 240, 255));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);

        JLabel[] labels = { new JLabel("Name:"), new JLabel("Age:"), new JLabel("Sex (M/F):"), new JLabel("Aadhar:"),
                new JLabel("Phone Number:") };
        JTextField[] textFields = { new JTextField(20), new JTextField(20), new JTextField(20), new JTextField(20),
                new JTextField(20) };

        for (int i = 0; i < labels.length; i++) {
            gbc.gridx = 0;
            gbc.gridy = i;
            panel.add(labels[i], gbc);

            gbc.gridx = 1;
            panel.add(textFields[i], gbc);
        }

        JLabel warningLabel = new JLabel(" ");
        warningLabel.setForeground(Color.RED);
        gbc.gridx = 1;
        gbc.gridy = labels.length;
        panel.add(warningLabel, gbc);

        JButton searchButton = new JButton("Add Patient");
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
            boolean isValid = true;

            String name = textFields[0].getText().trim();
            String age = textFields[1].getText().trim();
            String sex = textFields[2].getText().trim();
            String aadhar = textFields[3].getText().trim();
            String phone = textFields[4].getText().trim();

            if (!name.matches("[a-zA-Z\\s]*"))
                errorMessage.append("Invalid Name. ");
            if (!age.matches("\\d+") || Integer.parseInt(age) <= 0 || Integer.parseInt(age) >= 150)
                errorMessage.append("Invalid Age. ");
            if (!sex.equalsIgnoreCase("M") && !sex.equalsIgnoreCase("F"))
                errorMessage.append("Invalid Sex. ");
            if (!aadhar.matches("\\d{12}"))
                errorMessage.append("Invalid Aadhar. ");
            if (!phone.matches("\\+91\\d{10}"))
                errorMessage.append("Invalid Phone Number. ");

            if (errorMessage.length() > 0) {
                warningLabel.setText("Error: " + errorMessage.toString());
            } else {
                warningLabel.setText(" ");
                patientData.add(name);
                patientData.add(age);
                patientData.add(sex);
                patientData.add(aadhar);
                patientData.add(phone);
                patients.add(patientData);
                patientAddPage(searchFrame);
            }
        });

        backButton.addActionListener(e -> searchFrame.dispose());

        searchFrame.add(panel);
        searchFrame.setVisible(true);

    }

    private static void patientAddPage(JFrame parentFrame) {
        JFrame resultsFrame = new JFrame("Patient Results");
        resultsFrame.setSize(500, 400);
        resultsFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        resultsFrame.setLocationRelativeTo(parentFrame);

        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());

        JLabel resultLabel = new JLabel("Patient added successfully", JLabel.CENTER);
        resultLabel.setFont(new Font("Arial", Font.BOLD, 16));
        panel.add(resultLabel, BorderLayout.CENTER);  

        JButton backButton = new JButton("Back");
        backButton.setFont(new Font("Arial", Font.BOLD, 14));
        backButton.setBackground(new Color(220, 53, 69)); // Bootstrap danger color
        backButton.setForeground(Color.WHITE);
        backButton.setFocusPainted(false);
        backButton.setBorder(new LineBorder(new Color(200, 40, 60), 2, true));
        backButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        panel.add(backButton, BorderLayout.SOUTH);

        backButton.addActionListener(e -> resultsFrame.dispose());

        resultsFrame.add(panel);
        resultsFrame.setVisible(true);
    }

    private static void openResultsPage(JFrame parentFrame) {
        JFrame resultsFrame = new JFrame("Search Results");
        resultsFrame.setSize(500, 400);
        resultsFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        resultsFrame.setLocationRelativeTo(parentFrame);

        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());

        JLabel resultLabel = new JLabel("Result Found: " + patients.size() + " Patients", JLabel.CENTER);
        resultLabel.setFont(new Font("Arial", Font.BOLD, 16));
        panel.add(resultLabel, BorderLayout.NORTH);

        DefaultListModel<String> listModel = new DefaultListModel<>();
        JList<String> list = new JList<>(listModel);
        for (int i = 0; i < patients.size(); i++) {
            final int index = i;
            listModel.addElement(String.join(", ", patients.get(i)));
            list.addListSelectionListener(e -> {
            });
        }
        panel.add(new JScrollPane(list), BorderLayout.CENTER);

        JButton backButton = new JButton("Back");
        backButton.setFont(new Font("Arial", Font.BOLD, 14));
        backButton.setBackground(new Color(220, 53, 69)); // Bootstrap danger color
        backButton.setForeground(Color.WHITE);
        backButton.setFocusPainted(false);
        backButton.setBorder(new LineBorder(new Color(200, 40, 60), 2, true));
        backButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        panel.add(backButton, BorderLayout.SOUTH);

        backButton.addActionListener(e -> resultsFrame.dispose());

        resultsFrame.add(panel);
        resultsFrame.setVisible(true);
    }
}
