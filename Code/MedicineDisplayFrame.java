package test1;

import java.awt.EventQueue;
import java.awt.Font;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.DefaultListModel;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JTable;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.Button;
import java.awt.Color;

public class MedicineDisplayFrame {

    JFrame frame;
    private JTable apiTable;
    private JTable excipientTable;
    private JLabel PresctiptionReq;
    private JLabel Strength;
    private JLabel DosageForm;
    private JLabel MedicineNameLabel;
    private JLabel MedicineIDLabel;
    private JList<String> medicineList;
    private DefaultListModel<String> listModel;

    private static final String URL = "jdbc:oracle:thin:@//LAVANYA-ASUS:1521/orcl";
    private static final String USER = "scott";
    private static final String PASSWORD = "jaggi";

    // JDBC variables for opening and managing connection
    private static Connection connection;
    private JLabel PresctiptionReq_1;
    private JLabel Strength_1;
    private JLabel DosageForm_1;
    private JLabel DosageForm_2;
    private JLabel DosageForm_3;
    private JLabel MedicineIDLabel_1;
    private Button button;

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * Create the application.
     * @wbp.parser.entryPoint
     */
    public MedicineDisplayFrame() {
        initialize();
        populateMedicineList();
    }

    /**
     * Initialize the contents of the frame.
     */
    private void initialize() {
        frame = new JFrame();
        frame.setBounds(100, 100, 700, 450);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setLayout(null);
        frame.getContentPane().setBackground(new Color(225, 244, 181));

        apiTable = new JTable(new DefaultTableModel(new Object[]{"API"}, 0));
        apiTable.setBounds(53, 308, 141, 65);
        frame.getContentPane().add(apiTable);

        excipientTable = new JTable(new DefaultTableModel(new Object[]{"Excipient"}, 0));
        excipientTable.setBounds(231, 308, 141, 65);
        frame.getContentPane().add(excipientTable);

        MedicineNameLabel = new JLabel("Medicine Name");
        MedicineNameLabel.setFont(new Font("Tahoma", Font.PLAIN, 55));
        MedicineNameLabel.setBounds(53, 79, 581, 65);
        frame.getContentPane().add(MedicineNameLabel);

        MedicineIDLabel = new JLabel("Medicine ID");
        MedicineIDLabel.setFont(new Font("Tahoma", Font.PLAIN, 14));
        MedicineIDLabel.setBounds(132, 59, 109, 22);
        frame.getContentPane().add(MedicineIDLabel);

        PresctiptionReq = new JLabel("Prescription Requirement");
        PresctiptionReq.setFont(new Font("Tahoma", Font.PLAIN, 14));
        PresctiptionReq.setBounds(231, 181, 200, 22);
        frame.getContentPane().add(PresctiptionReq);

        Strength = new JLabel("Strength");
        Strength.setFont(new Font("Tahoma", Font.PLAIN, 14));
        Strength.setBounds(231, 204, 109, 22);
        frame.getContentPane().add(Strength);

        DosageForm = new JLabel("Dosage Form");
        DosageForm.setFont(new Font("Tahoma", Font.PLAIN, 14));
        DosageForm.setBounds(231, 232, 109, 22);
        frame.getContentPane().add(DosageForm);

        listModel = new DefaultListModel<>();
        medicineList = new JList<>(listModel);
        medicineList.setBounds(459, 181, 175, 192);
        frame.getContentPane().add(medicineList);

        JLabel Search = new JLabel("SEARCH MEDICINE");
        Search.setFont(new Font("Tahoma", Font.PLAIN, 14));
        Search.setBounds(459, 148, 143, 22);
        frame.getContentPane().add(Search);
        
        PresctiptionReq_1 = new JLabel("Prescription Requirement");
        PresctiptionReq_1.setFont(new Font("Tahoma", Font.PLAIN, 14));
        PresctiptionReq_1.setBounds(58, 181, 200, 22);
        frame.getContentPane().add(PresctiptionReq_1);
        
        Strength_1 = new JLabel("Strength");
        Strength_1.setFont(new Font("Tahoma", Font.PLAIN, 14));
        Strength_1.setBounds(57, 204, 109, 22);
        frame.getContentPane().add(Strength_1);
        
        DosageForm_1 = new JLabel("Dosage Form");
        DosageForm_1.setFont(new Font("Tahoma", Font.PLAIN, 14));
        DosageForm_1.setBounds(57, 232, 109, 22);
        frame.getContentPane().add(DosageForm_1);
        
        DosageForm_2 = new JLabel("API");
        DosageForm_2.setFont(new Font("Tahoma", Font.PLAIN, 19));
        DosageForm_2.setBounds(53, 275, 109, 22);
        frame.getContentPane().add(DosageForm_2);
        
        DosageForm_3 = new JLabel("Excipient");
        DosageForm_3.setFont(new Font("Tahoma", Font.PLAIN, 19));
        DosageForm_3.setBounds(231, 275, 109, 22);
        frame.getContentPane().add(DosageForm_3);
        
        MedicineIDLabel_1 = new JLabel("Medicine ID");
        MedicineIDLabel_1.setFont(new Font("Tahoma", Font.PLAIN, 14));
        MedicineIDLabel_1.setBounds(53, 59, 109, 22);
        frame.getContentPane().add(MedicineIDLabel_1);
        
        button = new Button("Back");
        button.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		frame.dispose();
        	}
        });
        button.setBackground(new Color(255, 255, 255));
        button.setFont(new Font("Tahoma", Font.PLAIN, 14));
        button.setBounds(10, 10, 78, 31);
        frame.getContentPane().add(button);

        medicineList.addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    String selectedMedicine = medicineList.getSelectedValue();
                    if (selectedMedicine != null) {
                        fetchMedicineDetails(selectedMedicine);
                    }
                }
            }
        });
    }

    private void populateMedicineList() {
        try {
            // Load Oracle JDBC Driver
            Class.forName("oracle.jdbc.driver.OracleDriver");

            // Establish the connection
            connection = DriverManager.getConnection(URL, USER, PASSWORD);

            if (connection != null) {
                System.out.println("Connected to the database!");

                // Create a statement
                Statement statement = connection.createStatement();

                // Define the SQL query
                String selectSql = "SELECT MedName FROM Medicine";

                // Execute the query
                ResultSet resultSet = statement.executeQuery(selectSql);

                // Populate the list model
                while (resultSet.next()) {
                    String medName = resultSet.getString("MedName");
                    listModel.addElement(medName);
                }

                // Close the result set and statement
                resultSet.close();
                statement.close();

            } else {
                System.out.println("Failed to make connection!");
            }
        } catch (ClassNotFoundException e) {
            System.out.println("Oracle JDBC Driver not found. Include the library in your classpath.");
            e.printStackTrace();
        } catch (SQLException e) {
            System.out.println("Connection failed! Check output console");
            e.printStackTrace();
        } finally {
            try {
                if (connection != null && !connection.isClosed()) {
                    connection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private void fetchMedicineDetails(String medName) {
        try {
            // Load Oracle JDBC Driver
            Class.forName("oracle.jdbc.driver.OracleDriver");

            // Establish the connection
            connection = DriverManager.getConnection(URL, USER, PASSWORD);

            if (connection != null) {
                System.out.println("Connected to the database!");

                // Create a statement
                Statement statement = connection.createStatement();

                // Define the SQL query
                String selectSql = "SELECT m.MedName, m.PrescriptionRequirement, m.Strength, d.MedicineID, d.DosageForm " +
                                   "FROM Medicine m JOIN Dosage d ON m.MedName = d.MedName WHERE m.MedName = '" + medName + "'";

                // Execute the query
                ResultSet resultSet = statement.executeQuery(selectSql);

                // Update the labels with the selected medicine details
                if (resultSet.next()) {
                    String prescriptionRequirementStr = resultSet.getString("PrescriptionRequirement");
                    char prescriptionRequirement = prescriptionRequirementStr.charAt(0);
                    String strength = resultSet.getString("Strength");
                    int medicineID = resultSet.getInt("MedicineID");
                    String dosageForm = resultSet.getString("DosageForm");

                    MedicineNameLabel.setText(medName);
                    MedicineIDLabel.setText(String.valueOf(medicineID));
                    if (String.valueOf(prescriptionRequirement) == "Y")
                    {
                    	PresctiptionReq.setText("Yes");
                    }
                    else
                    	PresctiptionReq.setText("No");
                    
                    Strength.setText(strength);
                    DosageForm.setText(dosageForm);

                    fetchAPIAndExcipientDetails(medName);
                }

                // Close the result set and statement
                resultSet.close();
                statement.close();

            } else {
                System.out.println("Failed to make connection!");
            }
        } catch (ClassNotFoundException e) {
            System.out.println("Oracle JDBC Driver not found. Include the library in your classpath.");
            e.printStackTrace();
        } catch (SQLException e) {
            System.out.println("Connection failed! Check output console");
            e.printStackTrace();
        } finally {
            try {
                if (connection != null && !connection.isClosed()) {
                    connection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private void fetchAPIAndExcipientDetails(String medName) {
        DefaultTableModel apiTableModel = (DefaultTableModel) apiTable.getModel();
        apiTableModel.setRowCount(0);  // Clear existing data

        DefaultTableModel excipientTableModel = (DefaultTableModel) excipientTable.getModel();
        excipientTableModel.setRowCount(0);  // Clear existing data

        try {
            // Load Oracle JDBC Driver
            Class.forName("oracle.jdbc.driver.OracleDriver");

            // Establish the connection
            connection = DriverManager.getConnection(URL, USER, PASSWORD);

            if (connection != null) {
                System.out.println("Connected to the database!");

                // Create a statement
                Statement statement = connection.createStatement();

                // Define the SQL query for APIs
                String selectAPISql = "SELECT API FROM API WHERE MedName = '" + medName + "'";
                ResultSet apiResultSet = statement.executeQuery(selectAPISql);

                // Populate the API table
                while (apiResultSet.next()) {
                    String api = apiResultSet.getString("API");
                    apiTableModel.addRow(new Object[]{api});
                }

                // Close the result set for APIs
                apiResultSet.close();

                // Define the SQL query for Excipients
                String selectExcipientSql = "SELECT Excipient FROM Excipient WHERE MedName = '" + medName + "'";
                ResultSet excipientResultSet = statement.executeQuery(selectExcipientSql);

                // Populate the Excipient table
                while (excipientResultSet.next()) {
                    String excipient = excipientResultSet.getString("Excipient");
                    excipientTableModel.addRow(new Object[]{excipient});
                }

                // Close the result set for Excipients
                excipientResultSet.close();

                // Close the statement
                statement.close();

            } else {
                System.out.println("Failed to make connection!");
            }
        } catch (ClassNotFoundException e) {
            System.out.println("Oracle JDBC Driver not found. Include the library in your classpath.");
            e.printStackTrace();
        } catch (SQLException e) {
            System.out.println("Connection failed! Check output console");
            e.printStackTrace();
        } finally {
            try {
                if (connection != null && !connection.isClosed()) {
                    connection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
