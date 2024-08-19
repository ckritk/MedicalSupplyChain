package test1;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.SpinnerNumberModel;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.Color;
import java.sql.Date;
import java.awt.Font;

public class DisposeMed extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JTable table;
    private JComboBox<String> comboMed;
    private JComboBox<String> comboDos;
    private JSpinner spinnerQty;
    private int pharmacyId; // To store PharmacyID from constructor

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    DisposeMed frame = new DisposeMed(2,"CureWell Pharmacy"); // Example PharmacyID
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * Create the frame.
     */
    public DisposeMed(int pharmacyId,String name) {
        this.pharmacyId = pharmacyId;

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 604, 406);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);
        contentPane.setBackground(new Color(225, 244, 181));

        JLabel lblNewLabel = new JLabel("Medicine");
        lblNewLabel.setFont(new Font("Tahoma", Font.PLAIN, 15));
        lblNewLabel.setBounds(22, 194, 60, 14);
        contentPane.add(lblNewLabel);

        JLabel lblNewLabel_1 = new JLabel("Dosage Form");
        lblNewLabel_1.setFont(new Font("Tahoma", Font.PLAIN, 15));
        lblNewLabel_1.setBounds(22, 227, 95, 14);
        contentPane.add(lblNewLabel_1);

        JLabel lblNewLabel_2 = new JLabel("NUMBER OF PACKAGES");
        lblNewLabel_2.setFont(new Font("Tahoma", Font.PLAIN, 15));
        lblNewLabel_2.setBounds(22, 282, 158, 14);
        contentPane.add(lblNewLabel_2);

        comboMed = new JComboBox<>();
        comboMed.setBounds(140, 192, 143, 22);
        contentPane.add(comboMed);

        comboDos = new JComboBox<>();
        comboDos.setBounds(140, 225, 143, 22);
        contentPane.add(comboDos);

        spinnerQty = new JSpinner();
        spinnerQty.setBackground(new Color(255, 255, 255));
        spinnerQty.setModel(new SpinnerNumberModel(1, 1, 100, 1));
        spinnerQty.setBounds(206, 281, 77, 20);
        contentPane.add(spinnerQty);

        JButton btnAdd = new JButton("Add");
        btnAdd.setBackground(new Color(255, 255, 255));
        btnAdd.setBounds(206, 312, 77, 38);
        contentPane.add(btnAdd);

        // Table with proper header columns
        table = new JTable();
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBounds(299, 160, 267, 139);
        contentPane.add(scrollPane);

        // Define column names for the table
        String[] columnNames = {"Medicine", "Dosage", "Quantity"};

        // Create a DefaultTableModel with the column names
        DefaultTableModel model = new DefaultTableModel(columnNames, 0);
        table.setModel(model);

        JButton btnRequest = new JButton("Confirm Request");
        btnRequest.setBackground(new Color(255, 255, 255));
        btnRequest.setBounds(423, 312, 143, 38);
        contentPane.add(btnRequest);
        
        JLabel lblNewLabel_3 = new JLabel("Service Provider");
        lblNewLabel_3.setFont(new Font("Tahoma", Font.PLAIN, 15));
        lblNewLabel_3.setBounds(22, 165, 172, 14);
        contentPane.add(lblNewLabel_3);
        
        JComboBox<String> comboProv = new JComboBox<>();
        comboProv.setBounds(140, 163, 143, 22);
        contentPane.add(comboProv);

        // Load data into comboMed
        loadMedicineData();

        // Load providers into comboProv
        loadProviders(comboProv);
        
        JLabel lblNewLabel_4 = new JLabel("Dispose Medicine");
        lblNewLabel_4.setFont(new Font("Tahoma", Font.PLAIN, 45));
        lblNewLabel_4.setBackground(new Color(240, 240, 240));
        lblNewLabel_4.setBounds(22, 79, 457, 43);
        contentPane.add(lblNewLabel_4);
        
        JButton btnBack = new JButton("Back");
        btnBack.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		dispose();
        		try {
                    PharmaHome frame = new PharmaHome(pharmacyId,name); // Pass the pharmacyId here
                    frame.setVisible(true);
                } catch (Exception eeee) {
                    eeee.printStackTrace();
                }
        	}
        });
        btnBack.setBackground(new Color(255, 255, 255));
        btnBack.setBounds(22, 11, 89, 38);
        contentPane.add(btnBack);

        // Action listener for the Add button
        btnAdd.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Get selected values from combo boxes and spinner
                String selectedMed = comboMed.getSelectedItem().toString();
                String selectedDos = comboDos.getSelectedItem().toString();
                int qty = (int) spinnerQty.getValue();

                // Add selected values to the table
                model.addRow(new Object[]{selectedMed, selectedDos, qty});
            }
        });

        // Action listener for comboMed
        comboMed.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Load dosage forms for the selected medicine
                loadDosageForms((String) comboMed.getSelectedItem());
            }
        });

        // Action listener for Confirm Request button
        btnRequest.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Save request details to the database
                saveRequestToDatabase(model, comboProv);
            }
        });
    }

    /**
     * Load MEDNAME values into comboMed from database.
     */
    private void loadMedicineData() {
        String query = "SELECT DISTINCT MEDNAME FROM dosage";
        List<String> medNames = new ArrayList<>();

        try (Connection con = DatabaseHandler.getConnection();
             PreparedStatement pst = con.prepareStatement(query);
             ResultSet rs = pst.executeQuery()) {

            while (rs.next()) {
                medNames.add(rs.getString("MEDNAME"));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        // Populate comboMed with medNames
        for (String medName : medNames) {
            comboMed.addItem(medName);
        }
    }

    /**
     * Load DOSAGEFORM values into comboDos for the selected MEDNAME from database.
     */
    private void loadDosageForms(String selectedMedName) {
        String query = "SELECT DOSAGEFORM FROM dosage WHERE MEDNAME = ?";
        comboDos.removeAllItems();

        try (Connection con = DatabaseHandler.getConnection();
             PreparedStatement pst = con.prepareStatement(query)) {

            pst.setString(1, selectedMedName);
            ResultSet rs = pst.executeQuery();

            while (rs.next()) {
                comboDos.addItem(rs.getString("DOSAGEFORM"));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Load providers into comboProv.
     */
    private void loadProviders(JComboBox<String> comboProv) {
        String query = "SELECT DISTINCT NAME FROM providers";
        List<String> providerNames = new ArrayList<>();

        try (Connection con = DatabaseHandler.getConnection();
             PreparedStatement pst = con.prepareStatement(query);
             ResultSet rs = pst.executeQuery()) {

            while (rs.next()) {
                providerNames.add(rs.getString("NAME"));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        // Populate comboProv with providerNames
        for (String name : providerNames) {
            comboProv.addItem(name);
        }
    }

    /**
     * Save request details to the database (request_details and collection_requests tables).
     */
    private void saveRequestToDatabase(DefaultTableModel model, JComboBox<String> comboProv) {
        // SQL queries
        String insertRequestDetailsQuery = "INSERT INTO request_details (REQUESTID, PHARMACYID, WHOLESALERID, MANUFACTURERID, PROVIDERID, SCHEDULED_PICKUP_DATE, STATUS) VALUES (?, ?, NULL, NULL, ?, NULL, 'N')";
        String insertCollectionRequestQuery = "INSERT INTO collection_requests (REQUESTID, WASTETYPE, QUANTITY) VALUES (?, ?, ?)";

        try (Connection con = DatabaseHandler.getConnection();
             PreparedStatement pstRequestDetails = con.prepareStatement(insertRequestDetailsQuery, Statement.RETURN_GENERATED_KEYS);
             PreparedStatement pstCollectionRequest = con.prepareStatement(insertCollectionRequestQuery)) {

            con.setAutoCommit(false); // Start transaction

            // Generate REQUESTID using sequence
            int requestId;
            try (PreparedStatement pstSeq = con.prepareStatement("SELECT RequestSeq.NEXTVAL FROM DUAL");
                 ResultSet rs = pstSeq.executeQuery()) {
                rs.next();
                requestId = rs.getInt(1);
            }

            // Get PROVIDERID from providers table based on selected name in comboProv
            String selectedProvider = comboProv.getSelectedItem().toString();
            int providerId = getProviderId(selectedProvider);

            // Insert into request_details
            pstRequestDetails.setInt(1, requestId);
            pstRequestDetails.setInt(2, pharmacyId); // PharmacyID from constructor
            pstRequestDetails.setInt(3, providerId); // ProviderID
            pstRequestDetails.executeUpdate();

            // Insert into collection_requests for each row in the JTable
            for (int i = 0; i < model.getRowCount(); i++) {
                String medicine = (String) model.getValueAt(i, 0);
                String dosage = (String) model.getValueAt(i, 1);
                int quantity = (int) model.getValueAt(i, 2);

                // Get MEDICINEID from dosage table
                int medicineId = getMedicineId(medicine, dosage);

                // Insert into collection_requests
                pstCollectionRequest.setInt(1, requestId);
                pstCollectionRequest.setInt(2, medicineId);
                pstCollectionRequest.setInt(3, quantity);
                pstCollectionRequest.executeUpdate();
            }

            con.commit(); // Commit transaction
            con.setAutoCommit(true); // Reset auto-commit mode

            // Inform user about successful request confirmation
            System.out.println("Request confirmed successfully!");

        } catch (Exception ex) {
            
            ex.printStackTrace();
            System.out.println("Failed to confirm request. Please try again.");
        }
    }

    /**
     * Get PROVIDERID from providers table based on provider name.
     */
    private int getProviderId(String providerName) {
        String query = "SELECT PROVIDERID FROM providers WHERE NAME = ?";
        int providerId = -1;

        try (Connection con = DatabaseHandler.getConnection();
             PreparedStatement pst = con.prepareStatement(query)) {

            pst.setString(1, providerName);
            ResultSet rs = pst.executeQuery();

            if (rs.next()) {
                providerId = rs.getInt("PROVIDERID");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return providerId;
    }

    /**
     * Get MEDICINEID from dosage table based on medicine name and dosage form.
     */
    private int getMedicineId(String medName, String dosageForm) {
        String query = "SELECT MEDICINEID FROM dosage WHERE MEDNAME = ? AND DOSAGEFORM = ?";
        int medicineId = -1;

        try (Connection con = DatabaseHandler.getConnection();
             PreparedStatement pst = con.prepareStatement(query)) {

            pst.setString(1, medName);
            pst.setString(2, dosageForm);
            ResultSet rs = pst.executeQuery();

            if (rs.next()) {
                medicineId = rs.getInt("MEDICINEID");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return medicineId;
    }
}
