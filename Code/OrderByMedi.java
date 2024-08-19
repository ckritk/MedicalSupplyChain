package test1;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.JTable;
import javax.swing.JSpinner;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Vector;

import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

import oracle.jdbc.OracleConnection;
import oracle.sql.ARRAY;
import oracle.sql.ArrayDescriptor;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.awt.Color;
import java.awt.Font;
import javax.swing.JCheckBox;


public class OrderByMedi extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTable table;
	private JTextField textField;
	private JTable table_1;

	/**
	 * Launch the application.
	
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					OrderByMedi frame = new OrderByMedi();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
      */
	/**
	 * Create the frame.
	 */
	public OrderByMedi(String role, int id, String name) {
		setBackground(new Color(255, 255, 255));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 668, 477);
        contentPane = new JPanel();
        contentPane.setBackground(new Color(240,240,240));
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

        setContentPane(contentPane);
        contentPane.setLayout(null);

        JLabel lblNewLabel = new JLabel("By Medicine");
        lblNewLabel.setFont(new Font("Tahoma", Font.PLAIN, 14));
        lblNewLabel.setBounds(21, 166, 116, 32);
        contentPane.add(lblNewLabel);

        textField = new JTextField();
        textField.setBounds(131, 168, 160, 32);
        contentPane.add(textField);
        textField.setColumns(10);

        JButton btnOK = new JButton("Search");
        btnOK.setBackground(new Color(255,255,255));
        btnOK.setBounds(294, 168, 79, 32);
        contentPane.add(btnOK);

        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setBounds(21, 209, 353, 172);
        contentPane.add(scrollPane);
        contentPane.setBackground(new Color(225, 244, 181));

        table = new JTable();
        scrollPane.setViewportView(table);

        JLabel lblNewLabel_1 = new JLabel("NUMBER OF PACKAGES");
        lblNewLabel_1.setFont(new Font("Tahoma", Font.PLAIN, 14));
        lblNewLabel_1.setBounds(21, 392, 175, 22);
        contentPane.add(lblNewLabel_1);

        JSpinner spinner = new JSpinner();
        spinner.setBounds(181, 390, 66, 29);
        contentPane.add(spinner);

        JButton btnOrder = new JButton("Place Order");
        btnOrder.setBackground(new Color(255,255,255));
        btnOrder.setBounds(523, 390, 108, 30);
        contentPane.add(btnOrder);
        
        JButton btnAdd = new JButton("Add to Order");
        btnAdd.setBackground(new Color(255,255,255));
        btnAdd.setBounds(257, 388, 116, 32);
        contentPane.add(btnAdd);
        
        JComboBox comboBoxManu = new JComboBox();
        comboBoxManu.setBounds(131, 135, 160, 32);
        contentPane.add(comboBoxManu);
        
     // Execute the query to retrieve manufacturer names
        String query = "SELECT Name FROM Manufacturers";
        try (Connection conn = DatabaseHandler.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            
            // Clear existing items in the combo box
            comboBoxManu.removeAllItems();
            
            // Add items from the result set to the combo box
            while (rs.next()) {
                String manufacturerName = rs.getString("Name");
                comboBoxManu.addItem(manufacturerName);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        
        JButton btnManu = new JButton("Search");
        btnManu.setBackground(new Color(255,255,255));
        btnManu.setBounds(294, 135, 79, 32);
        contentPane.add(btnManu);
        
        String[] columnNames = {"MedName", "Dosage", "Packages", "Manufacturer"};

        // Define the model for table_1
        DefaultTableModel table1Model = new DefaultTableModel(new Object[][] {}, columnNames);

        // Add column names as the first row in the model
        table1Model.addRow(columnNames);

        // Create table_1 with the defined model
        table_1 = new JTable(table1Model);
        table_1.setBounds(384, 210, 247, 172);
        contentPane.add(table_1);
        
        JLabel lblNewLabel_2 = new JLabel("Order Medicines");
        lblNewLabel_2.setFont(new Font("Tahoma", Font.PLAIN, 50));
        lblNewLabel_2.setBounds(153, 47, 370, 51);
        contentPane.add(lblNewLabel_2);
        
        JButton btnNewButton = new JButton("Back");
        btnNewButton.setBackground(new Color(255,255,255));
        btnNewButton.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		dispose();
        		WSHome wshomePage = new WSHome(role,id,name);
                wshomePage.setVisible(true);
        	}
        });
        btnNewButton.setBounds(10, 11, 93, 32);
        contentPane.add(btnNewButton);
        
        JLabel lblSearchManufacturer = new JLabel("By Manufacturer");
        lblSearchManufacturer.setFont(new Font("Tahoma", Font.PLAIN, 14));
        lblSearchManufacturer.setBounds(21, 133, 116, 32);
        contentPane.add(lblSearchManufacturer);
        
        JLabel lblNewLabel_3 = new JLabel("SEARCH");
        lblNewLabel_3.setFont(new Font("Tahoma", Font.PLAIN, 19));
        lblNewLabel_3.setBounds(21, 102, 116, 32);
        contentPane.add(lblNewLabel_3);
        
        JLabel lblNewLabel_3_1 = new JLabel("YOUR ORDER");
        lblNewLabel_3_1.setFont(new Font("Tahoma", Font.PLAIN, 19));
        lblNewLabel_3_1.setBounds(396, 168, 127, 32);
        contentPane.add(lblNewLabel_3_1);

        btnOK.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String medName = textField.getText();
                fillTableWithMedicineData(medName);
            }
        });
        
        btnManu.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String manu = (String) comboBoxManu.getSelectedItem();
                fillTableWithManufacturerData(manu);
            }
        });
        
        btnAdd.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Get the selected row index
                int selectedRow = table.getSelectedRow();
                
                // Check if a row is selected
                if (selectedRow != -1) {
                    // Get the values from the selected row
                    Object valueMedName = table.getValueAt(selectedRow, 1); // Assuming the 2nd column contains MedName
                    Object valueDosage = table.getValueAt(selectedRow, 4); // Assuming the 5th column contains Dosage
                    Object valueManu = table.getValueAt(selectedRow, 0);
                    
                    // Get the value from the spinner
                    int spinnerValue = (int) spinner.getValue();
                    
                    // Add the values to table_1
                    DefaultTableModel model = (DefaultTableModel) table_1.getModel();
                    model.addRow(new Object[] {valueMedName, valueDosage, spinnerValue, valueManu});
                } 
            }
        });
        
        btnOrder.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Date currentDate = new Date();

                List<String> names = getColumnData(table_1, 0);
                List<String> dosages = getColumnData(table_1, 1);
                List<String> packages = getColumnData(table_1, 2);
                List<String> manufacturers = getColumnData(table_1, 3);

                if (!names.isEmpty()) names.remove(0);
                if (!dosages.isEmpty()) dosages.remove(0);
                if (!packages.isEmpty()) packages.remove(0);
                if (!manufacturers.isEmpty()) manufacturers.remove(0);

                List<Integer> medicineIDs = DatabaseHandler.getMedicineIDs(names, dosages);
                List<Integer> packagesOrdered = new ArrayList<>();
                for (String pkg : packages) {
                    packagesOrdered.add(Integer.parseInt(pkg));
                }

                Integer[] medicineIDArray = medicineIDs.toArray(new Integer[0]);
                Integer[] packagesOrderedArray = packagesOrdered.toArray(new Integer[0]);

                String who = role;
                String whoName = name;
                String from = "Manufacturer";
                String fromName = manufacturers.isEmpty() ? "" : manufacturers.get(0);
                
                try (Connection conn = DatabaseHandler.getConnection()) {
                    // Disable auto-commit mode
                    conn.setAutoCommit(false);
                    
                    OracleConnection oracleConn = conn.unwrap(OracleConnection.class);
                    String sql = "{CALL PlaceOrder(?, ?, ?, ?, ?, ?, ?)}";
                    try (CallableStatement stmt = oracleConn.prepareCall(sql)) {
                        stmt.setDate(1, new java.sql.Date(currentDate.getTime()));
                        stmt.setString(2, who);
                        stmt.setString(3, whoName);
                        stmt.setString(4, from);
                        stmt.setString(5, fromName);
                        
                        // Create ARRAY directly using oracleConn
                        ARRAY medicineArray = oracleConn.createARRAY("SYS.ODCINUMBERLIST", medicineIDArray);
                        ARRAY packagesArray = oracleConn.createARRAY("SYS.ODCINUMBERLIST", packagesOrderedArray);
                        
                        stmt.setArray(6, medicineArray);
                        stmt.setArray(7, packagesArray);

                        stmt.execute();
                        System.out.println("Order placed successfully.");
                        
                        // Commit the transaction
                        conn.commit();
                    } catch (SQLException ex) {
                        // Roll back the transaction in case of an error
                        conn.rollback();
                        ex.printStackTrace();
                    } finally {
                        // Reset auto-commit mode to true
                        conn.setAutoCommit(true);
                    }
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        });}

	
	private void fillTableWithMedicineData(String medName) {
        String sql = "SELECT DISTINCT ma.Name as Manufacturer, m.MedName, b.PackagesManufactured AS Stock, b.QuantityPerPackage, d.DosageForm, mp.PricePerPackage "
                   + "FROM Batch b "
                   + "JOIN Dosage d ON b.MedicineID = d.MedicineID "
                   + "JOIN Inventory i ON b.BatchID = i.BatchID "
                   + "JOIN Medicine m ON d.MedName = m.MedName "
                   + "JOIN Manufacturers ma ON ma.ManufacturerID = b.ManufacturerID "
                   + "JOIN ManufacturePrice mp ON b.MedicineID = mp.MedicineID AND mp.ManufacturerID = b.ManufacturerID "
                   + "WHERE m.MedName = ?";

        Vector<String> columnNames = new Vector<>();
        columnNames.add("ManufacturerID");
        columnNames.add("MedName");
        columnNames.add("Stock");
        columnNames.add("QtyPerPackage");
        columnNames.add("DosageForm");
        columnNames.add("Price");

        Vector<Vector<Object>> data = new Vector<>();

        try (Connection conn = DatabaseHandler.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, medName);
            ResultSet rs = stmt.executeQuery();
            ResultSetMetaData metaData = rs.getMetaData();

            int columnCount = metaData.getColumnCount();
            while (rs.next()) {
                Vector<Object> vector = new Vector<>();
                for (int i = 1; i <= columnCount; i++) {
                    vector.add(rs.getObject(i));
                }
                data.add(vector);
            }

            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        DefaultTableModel model = new DefaultTableModel(data, columnNames);
        table.setModel(model);
    }

    private void fillTableWithManufacturerData(String manufacturer) {
        String sql = "SELECT DISTINCT ma.Name as Manufacturer, m.MedName, b.PackagesManufactured AS Stock, b.QuantityPerPackage, d.DosageForm, mp.PricePerPackage "
                   + "FROM Batch b "
                   + "JOIN Dosage d ON b.MedicineID = d.MedicineID "
                   + "JOIN Inventory i ON b.BatchID = i.BatchID "
                   + "JOIN Medicine m ON d.MedName = m.MedName "
                   + "JOIN Manufacturers ma ON ma.ManufacturerID = b.ManufacturerID "
                   + "JOIN ManufacturePrice mp ON b.MedicineID = mp.MedicineID AND mp.ManufacturerID = b.ManufacturerID "
                   + "WHERE ma.Name = ?";

        Vector<String> columnNames = new Vector<>();
        columnNames.add("ManufacturerID");
        columnNames.add("MedName");
        columnNames.add("Stock");
        columnNames.add("QtyPerPackage");
        columnNames.add("DosageForm");
        columnNames.add("Price");

        Vector<Vector<Object>> data = new Vector<>();

        try (Connection conn = DatabaseHandler.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, manufacturer);
            ResultSet rs = stmt.executeQuery();
            ResultSetMetaData metaData = rs.getMetaData();

            int columnCount = metaData.getColumnCount();
            while (rs.next()) {
                Vector<Object> vector = new Vector<>();
                for (int i = 1; i <= columnCount; i++) {
                    vector.add(rs.getObject(i));
                }
                data.add(vector);
            }

            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        DefaultTableModel model = new DefaultTableModel(data, columnNames);
        table.setModel(model);
    }
	private static class __Tmp {
		private static void __tmp() {
			  javax.swing.JPanel __wbp_panel = new javax.swing.JPanel();
		}
	}
	
	private static List<String> getColumnData(JTable table, int columnIndex) {
        List<String> columnData = new ArrayList<>();
        for (int i = 0; i < table.getRowCount(); i++) {
            Object value = table.getValueAt(i, columnIndex);
            if (value != null) {
                columnData.add(value.toString());
            }
        }
        return columnData;
    }
}


