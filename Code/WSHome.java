package test1;

import java.awt.EventQueue;
import java.sql.*;
import java.sql.Date;

import javax.swing.border.EmptyBorder;

import java.util.Vector;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import java.awt.*;

import java.text.SimpleDateFormat;
import java.text.ParseException;

import java.util.*;


public class WSHome extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
    private JTable table;
    private JScrollPane scrollPane;
    private int Wid;

	/**
	 * Launch the application.
	 
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					WSHome frame = new WSHome();
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
    public WSHome(String role, int wid, String name) {
		setBackground(new Color(225, 244, 181));
    	Wid = wid;
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 750, 496); // Set the size of the main window to 800x600
        contentPane = new JPanel();
        contentPane.setBackground(new Color(225, 244, 181));
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

        setContentPane(contentPane);
        contentPane.setLayout(null); // Use null layout to specify coordinates

        JButton btnExpiredMeds = new JButton("Expired Stock");
        btnExpiredMeds.setBackground(new Color(255, 255, 255));
        btnExpiredMeds.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                displayExpiredMeds(wid); // Pass the wholesalerID as needed
            }
        });
        btnExpiredMeds.setBounds(301, 73, 130, 43);
        contentPane.add(btnExpiredMeds);

        // Button to display records with stock less than 50
        JButton btnLowStock = new JButton("Low Stock");
        btnLowStock.setBackground(new Color(255, 255, 255));
        btnLowStock.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                displayLowStockMeds(wid); // Pass the wholesalerID as needed
            }
        });
        btnLowStock.setBounds(441, 73, 123, 43);
        contentPane.add(btnLowStock);

        // Button to display all records
        JButton btnAllRecords = new JButton("Complete stock");
        btnAllRecords.setBackground(new Color(255, 255, 255));
        btnAllRecords.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                displayAllRecords(wid);
            }
        });
        btnAllRecords.setBounds(574, 73, 130, 43);
        contentPane.add(btnAllRecords);

        JButton BtnPrice = new JButton("Update Price");
        BtnPrice.setBackground(new Color(255, 255, 255));
        BtnPrice.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int rowCount = table.getRowCount();
                for (int i = 0; i < rowCount; i++) {
                    String medicineID = table.getValueAt(i, 0).toString();
                    // Assuming the date format is "yyyy-MM-dd"
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                    Date manufacturedDate = null;
                    try {
                        manufacturedDate = new Date(dateFormat.parse(table.getValueAt(i, 4).toString()).getTime());
                    } catch (ParseException ex) {
                        ex.printStackTrace();
                    }
                    // Assuming the date is in a proper format
                    double newPrice = Double.parseDouble(table.getValueAt(i, 3).toString());
                    int wholesalerID = wid; // As per your requirement

                    updatePriceInInventory(medicineID, manufacturedDate, wholesalerID, newPrice);
                }
            }
        });
        BtnPrice.setBounds(558, 346, 147, 43);
        contentPane.add(BtnPrice);

        scrollPane = new JScrollPane(table);
        contentPane.add(scrollPane);
        scrollPane.setBounds(33, 135, 672, 199);

        table = new JTable(); // Initialize table without data
        scrollPane.setViewportView(table); // Add the table to the scroll pane

        JButton btnOrder = new JButton("Order Medicine");
        btnOrder.setBackground(new Color(255, 255, 255));
        btnOrder.setBounds(33, 346, 163, 43);
        contentPane.add(btnOrder);
        btnOrder.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose(); // Close the current dialog
                OrderByMedi frame = new OrderByMedi(role, wid, name);
                frame.setVisible(true);
            }
        });

        JButton btnSupply = new JButton("Pharmacy Orders");
        btnSupply.setBackground(new Color(255, 255, 255));
        btnSupply.setBounds(206, 346, 156, 43);
        contentPane.add(btnSupply);
        btnSupply.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose(); // Close the current dialog
                System.out.println("lllll");
                TrackOrder frame = new TrackOrder(role, wid, name);
                frame.setVisible(true);
            }
        });

        JButton btnCollec = new JButton("Dispose Medicine");
        btnCollec.setBackground(new Color(255, 255, 255));
        btnCollec.setBounds(33, 400, 163, 43);
        contentPane.add(btnCollec);
        btnCollec.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                ArrayList<Integer> medicineIDList = new ArrayList<>();
                ArrayList<Integer> quantityList = new ArrayList<>();

                int rowCount = table.getRowCount();
                for (int i = 0; i < rowCount; i++) {
                    int medicineID = Integer.parseInt(table.getValueAt(i, 0).toString());
                    int quantity = Integer.parseInt(table.getValueAt(i, 2).toString());
                    medicineIDList.add(medicineID);
                    quantityList.add(quantity);
                }
                if (table.getSelectedRow() == -1) {
                    JOptionPane.showMessageDialog(WSHome.this, "Please select medicines for disposal.");
                }

                MediCollec frame = new MediCollec(WSHome.this, medicineIDList, quantityList, wid);
                frame.setVisible(true);
            }
        });

        JButton btnTrans = new JButton("Transactions");
        btnTrans.setBackground(new Color(255, 255, 255));
        btnTrans.setBounds(206, 400, 156, 43);
        contentPane.add(btnTrans);

        JLabel lblNewLabel = new JLabel("Inventory");
        lblNewLabel.setBackground(new Color(255, 255, 255));
        lblNewLabel.setFont(new Font("Tahoma", Font.PLAIN, 55));
        lblNewLabel.setBounds(33, 48, 336, 83);
        contentPane.add(lblNewLabel);

        JButton btnNewButton = new JButton("SignOut");
        btnNewButton.setBackground(new Color(226, 251, 254));
        btnNewButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose();
                Login login = new Login();
                login.setVisible(true);
            }
        });
        btnNewButton.setBounds(574, 11, 130, 36);
        contentPane.add(btnNewButton);

        JButton btnDisposeExpiredMedicines = new JButton("Dispose Expired Medicines");
        btnDisposeExpiredMedicines.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                ExpiredMediCollec dialog = new ExpiredMediCollec(WSHome.this, wid);
                dialog.setVisible(true);
            }
        });
        btnDisposeExpiredMedicines.setFont(new Font("Tahoma", Font.PLAIN, 11));
        btnDisposeExpiredMedicines.setBackground(Color.WHITE);
        btnDisposeExpiredMedicines.setBounds(372, 400, 176, 43);
        contentPane.add(btnDisposeExpiredMedicines);

        JLabel lblNewLabel_1 = new JLabel(name);
        lblNewLabel_1.setFont(new Font("Tahoma", Font.PLAIN, 14));
        lblNewLabel_1.setBounds(33, 33, 232, 28);
        contentPane.add(lblNewLabel_1);
        
        JButton btnSupply_1 = new JButton("Medicine Info");
        btnSupply_1.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		MedicineDisplayFrame window = new MedicineDisplayFrame();
                window.frame.setVisible(true);
        	}
        });
        btnSupply_1.setBackground(Color.WHITE);
        btnSupply_1.setBounds(372, 346, 176, 43);
        contentPane.add(btnSupply_1);
        btnTrans.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Add action to perform for Transactions button if needed
                dispose();
                History frame = new History(role, wid,name);
                frame.setVisible(true);
            }
        });
    }

	
	public void displayAllRecords(int wholesalerID) {
        DefaultTableModel model = getAllRecordsTableModel(wholesalerID);
        table.setModel(model);
    }

    public void displayExpiredMeds(int wholesalerID) {
        DefaultTableModel model = getExpiredMedsTableModel(wholesalerID);
        table.setModel(model);
    }

    public void displayLowStockMeds(int wholesalerID) {
        DefaultTableModel model = getLowStockTableModel(wholesalerID);
        table.setModel(model);
    }

    public DefaultTableModel getAllRecordsTableModel(int wholesalerID) {
        Vector<String> columnNames = new Vector<>();
        Vector<Vector<Object>> data = new Vector<>();

        // Your SQL query to fetch all records
        String sql = "SELECT d.MEDICINEID, m.MEDNAME, i.QUANTITYINSTOCK, i.PRICEPERPACKAGE, b.MANUFACTUREDDATE, " +
                "ADD_MONTHS(b.MANUFACTUREDDATE, m.EXPIRYFROMMANUFACTURE * 30) AS ExpDate, " +
                "b.QUANTITYPERPACKAGE " +
                "FROM inventory i " +
                "JOIN batch b ON i.BATCHID = b.BATCHID " +
                "JOIN dosage d ON d.MEDICINEID = b.MEDICINEID " +
                "JOIN medicine m ON d.MEDNAME = m.MEDNAME " +
                "WHERE i.WHOLESALERID = ?";

        try (Connection conn = DatabaseHandler.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, wholesalerID);
            ResultSet rs = stmt.executeQuery();
            ResultSetMetaData metaData = rs.getMetaData();

            int columnCount = metaData.getColumnCount();
            for (int i = 1; i <= columnCount; i++) {
                columnNames.add(metaData.getColumnName(i));
            }

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
        return new DefaultTableModel(data, columnNames);
    }

    @Override
    public void setVisible(boolean visible) {
        super.setVisible(visible);
        if (visible) {
            displayAllRecords(Wid); // Refresh the table data
        }
    }

    public DefaultTableModel getExpiredMedsTableModel(int wholesalerID) {
        Vector<String> columnNames = new Vector<>();
        Vector<Vector<Object>> data = new Vector<>();

        // Your SQL query to fetch expired medication records
        String sql = "SELECT d.MEDICINEID, m.MEDNAME, i.QUANTITYINSTOCK, i.PRICEPERPACKAGE, b.MANUFACTUREDDATE, " +
                "ADD_MONTHS(b.MANUFACTUREDDATE, m.EXPIRYFROMMANUFACTURE) AS ExpDate, " +
                "b.QUANTITYPERPACKAGE " +
                "FROM inventory i " +
                "JOIN batch b ON i.BATCHID = b.BATCHID " +
                "JOIN dosage d ON d.MEDICINEID = b.MEDICINEID " +
                "JOIN medicine m ON d.MEDNAME = m.MEDNAME " +
                "WHERE i.WHOLESALERID = ? " +
                "AND ADD_MONTHS(b.MANUFACTUREDDATE, m.EXPIRYFROMMANUFACTURE) < SYSDATE";

        try (Connection conn = DatabaseHandler.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, wholesalerID);
            ResultSet rs = stmt.executeQuery();
            ResultSetMetaData metaData = rs.getMetaData();

            int columnCount = metaData.getColumnCount();
            for (int i = 1; i <= columnCount; i++) {
                columnNames.add(metaData.getColumnName(i));
            }

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

        return new DefaultTableModel(data, columnNames);
    }

    public DefaultTableModel getLowStockTableModel(int wholesalerID) {
        Vector<String> columnNames = new Vector<>();
        Vector<Vector<Object>> data = new Vector<>();

        // Your SQL query to fetch medication records with stock less than 50
        String sql = "SELECT d.MEDICINEID, m.MEDNAME, i.QUANTITYINSTOCK, i.PRICEPERPACKAGE, b.MANUFACTUREDDATE, " +
                "ADD_MONTHS(b.MANUFACTUREDDATE, m.EXPIRYFROMMANUFACTURE) AS ExpDate, " +
                "b.QUANTITYPERPACKAGE " +
                "FROM inventory i " +
                "JOIN batch b ON i.BATCHID = b.BATCHID " +
                "JOIN dosage d ON d.MEDICINEID = b.MEDICINEID " +
                "JOIN medicine m ON d.MEDNAME = m.MEDNAME " +
                "WHERE i.WHOLESALERID = ? " +
                "AND i.QUANTITYINSTOCK < 50";

        try (Connection conn = DatabaseHandler.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, wholesalerID);
            ResultSet rs = stmt.executeQuery();
            ResultSetMetaData metaData = rs.getMetaData();

            int columnCount = metaData.getColumnCount();
            for (int i = 1; i <= columnCount; i++) {
                columnNames.add(metaData.getColumnName(i));
            }

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

        return new DefaultTableModel(data, columnNames);
    }
    
    public void updatePriceInInventory(String medicineID, Date manufacturedDate, int wholesalerID, double newPrice) {
        String sql = "UPDATE Inventory "
                   + "SET PricePerPackage = ? "
                   + "WHERE BatchID = ("
                   +     "SELECT b.BatchID "
                   +     "FROM Batch b "
                   +     "JOIN Dosage m ON b.MedicineID = m.MedicineID "
                   +     "WHERE m.MedicineID = ? "
                   +     "AND b.ManufacturedDate = ? "
                   + ")"
                   + "AND WholesalerID = ?";
        
     
        
        try (Connection conn = DatabaseHandler.getConnection()) {
            conn.setAutoCommit(true); // Disable auto-commit
            
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setDouble(1, newPrice);
                stmt.setString(2, medicineID);
                stmt.setDate(3, manufacturedDate);
                stmt.setInt(4, wholesalerID);
                
                int rowsAffected = stmt.executeUpdate();
                System.out.println(rowsAffected + " rows updated in Inventory table.");
            } catch (SQLException e) {
                conn.rollback(); // Roll back the transaction if an exception occurs
                e.printStackTrace();
                return; // Exit the method
            }
            //conn.commit(); // Commit the transaction
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
}
