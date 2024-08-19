package test1;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.time.LocalDate;
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
import java.awt.Font;

public class OrderPharma extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JTable table;
    private JComboBox<String> comboMed;
    private JComboBox<String> comboDos;
    private JSpinner spinnerQty;
    private int pharmacyId; // To store PharmacyID from constructor
    private List<Integer> medicineIds = new ArrayList<>();
    private List<Integer> quantities = new ArrayList<>();

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    OrderPharma frame = new OrderPharma(2,"CureWell Pharmacy"); // Example PharmacyID
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
    public OrderPharma(int pharmacyId,String name) {
        this.pharmacyId = pharmacyId;

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 605, 402);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);
        contentPane.setBackground(new Color(225, 244, 181));

        JLabel lblNewLabel = new JLabel("Medicine");
        lblNewLabel.setFont(new Font("Tahoma", Font.PLAIN, 15));
        lblNewLabel.setBounds(31, 189, 95, 22);
        contentPane.add(lblNewLabel);

        JLabel lblNewLabel_1 = new JLabel("Dosage Form");
        lblNewLabel_1.setFont(new Font("Tahoma", Font.PLAIN, 15));
        lblNewLabel_1.setBounds(31, 220, 95, 22);
        contentPane.add(lblNewLabel_1);

        JLabel lblNewLabel_2 = new JLabel("NUMBER OF PACKAGES");
        lblNewLabel_2.setFont(new Font("Tahoma", Font.PLAIN, 15));
        lblNewLabel_2.setBounds(31, 280, 171, 14);
        contentPane.add(lblNewLabel_2);

        comboMed = new JComboBox<>();
        comboMed.setBounds(132, 191, 131, 22);
        contentPane.add(comboMed);

        comboDos = new JComboBox<>();
        comboDos.setBounds(132, 222, 131, 22);
        contentPane.add(comboDos);

        spinnerQty = new JSpinner();
        spinnerQty.setBackground(new Color(255, 255, 255));
        spinnerQty.setModel(new SpinnerNumberModel(1, 1, 100, 1));
        spinnerQty.setBounds(197, 274, 66, 20);
        contentPane.add(spinnerQty);

        JButton btnAdd = new JButton("Add");
        btnAdd.setBackground(new Color(255, 255, 255));
        btnAdd.setBounds(174, 308, 89, 34);
        contentPane.add(btnAdd);

        // Table with proper header columns
        table = new JTable();
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBounds(313, 159, 256, 138);
        contentPane.add(scrollPane);

        // Define column names for the table
        String[] columnNames = {"Medicine", "Dosage", "Quantity"};

        // Create a DefaultTableModel with the column names
        DefaultTableModel model = new DefaultTableModel(columnNames, 0);
        table.setModel(model);

        JButton btnRequest = new JButton("Place Order");
        btnRequest.setBackground(new Color(255, 255, 255));
        btnRequest.setBounds(438, 308, 131, 34);
        contentPane.add(btnRequest);

        JLabel lblNewLabel_3 = new JLabel("Wholesaler");
        lblNewLabel_3.setFont(new Font("Tahoma", Font.PLAIN, 15));
        lblNewLabel_3.setBounds(31, 164, 172, 14);
        contentPane.add(lblNewLabel_3);

        JComboBox<String> comboProv = new JComboBox<>();
        comboProv.setBounds(132, 162, 131, 22);
        contentPane.add(comboProv);

        // Load data into comboMed
        loadMedicineData();

        // Load wholesalers into comboProv
        loadWholesalers(comboProv);

        JLabel lblNewLabel_4 = new JLabel("Order Medicine");
        lblNewLabel_4.setFont(new Font("Tahoma", Font.PLAIN, 45));
        lblNewLabel_4.setBackground(new Color(240, 240, 240));
        lblNewLabel_4.setBounds(20, 77, 323, 59);
        contentPane.add(lblNewLabel_4);
        
        JButton btnBack = new JButton("Back");
        btnBack.setBackground(new Color(255, 255, 255));
        btnBack.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		dispose();
        		try {
                    PharmaHome frame = new PharmaHome(pharmacyId,name); // Pass the pharmacyId here
                    frame.setVisible(true);
                } catch (Exception eee) {
                    eee.printStackTrace();
                }
        	}
        });
        btnBack.setBounds(20, 11, 95, 41);
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

        // Action listener for Confirm Order button
        btnRequest.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Collect medicine IDs and quantities
                collectMedicineIdsAndQuantities(model);
                // Optionally, you can use medicineIds and quantities lists for further processing
                System.out.println("Medicine IDs: " + medicineIds);
                System.out.println("Quantities: " + quantities);
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
     * Load wholesalers into comboProv.
     */
    private void loadWholesalers(JComboBox<String> comboProv) {
        String query = "SELECT DISTINCT NAME FROM wholesalers";
        List<String> wholesalerNames = new ArrayList<>();

        try (Connection con = DatabaseHandler.getConnection();
             PreparedStatement pst = con.prepareStatement(query);
             ResultSet rs = pst.executeQuery()) {

            while (rs.next()) {
                wholesalerNames.add(rs.getString("NAME"));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        // Populate comboProv with wholesalerNames
        for (String name : wholesalerNames) {
            comboProv.addItem(name);
        }
    }

    /**
     * Collects medicine IDs and quantities from the JTable.
     */
    private void collectMedicineIdsAndQuantities(DefaultTableModel model) {
        medicineIds.clear();
        quantities.clear();

        for (int i = 0; i < model.getRowCount(); i++) {
            String medName = model.getValueAt(i, 0).toString();
            String dosage = model.getValueAt(i, 1).toString();
            int qty = Integer.parseInt(model.getValueAt(i, 2).toString());

            // Retrieve MEDICINEID for medName and dosage from dosage table
            int medicineId = getMedicineId(medName, dosage);

            // Add to lists
            medicineIds.add(medicineId);
            quantities.add(qty);
        }
    }

    /**
     * Helper method to retrieve MEDICINEID based on medicine name and dosage form.
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
