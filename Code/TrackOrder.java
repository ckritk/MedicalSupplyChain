package test1;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.JLabel;
import java.awt.Font;

public class TrackOrder extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JTable table;
    private Connection connection;
    private JButton btnAll;
    private JButton btnPending;
    private int selectedRowIndex ;
    
    private int wid = 1;
    private JButton btnUpdate;
    private JButton btnView;

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
     * Create the frame.
     */
    public TrackOrder(String role, int wid, String name){
        try {
            // Establish database connection
            connection = DatabaseHandler.getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 593, 430);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        contentPane.setBackground(new Color(225, 244, 181));

        setContentPane(contentPane);
        contentPane.setLayout(null);

        btnAll = new JButton("All Orders");
        btnAll.setBackground(new Color(255,255,255));
        btnAll.setBounds(351, 73, 107, 38);
        contentPane.add(btnAll);
        btnAll.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                displayOrders("All", wid);
            }
        });

        btnPending = new JButton("Pending");
        btnPending.setBackground(new Color(255,255,255));
        btnPending.setBounds(468, 73, 90, 38);
        contentPane.add(btnPending);
        btnPending.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                displayOrders("Pending", wid);
            }
        });

        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setBounds(22, 122, 536, 213);
        contentPane.add(scrollPane);

        table = new JTable();
        scrollPane.setViewportView(table);
        
        btnUpdate = new JButton("Update Status");
        btnUpdate.setBackground(new Color(255,255,255));
        btnUpdate.setBackground(new Color(255,255,255));
        btnUpdate.setBounds(438, 346, 120, 38);
        contentPane.add(btnUpdate);
        

    	
        
        btnView = new JButton("View Details");
        btnView.setBackground(new Color(255,255,255));
        btnView.setBackground(new Color(255,255,255));
        btnView.setBounds(308, 346, 120, 38);
        contentPane.add(btnView);
        
        JLabel lblNewLabel = new JLabel("Pharmacy Orders");
        lblNewLabel.setFont(new Font("Tahoma", Font.PLAIN, 40));
        lblNewLabel.setBounds(22, 65, 336, 46);
        contentPane.add(lblNewLabel);
        
        JButton btnNewButton = new JButton("Back");
        btnNewButton.setBackground(new Color(255,255,255));
        btnNewButton.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		dispose();
        		WSHome frame = new WSHome(role,wid,name);
				frame.setVisible(true);
        	}
        });
        btnNewButton.setBounds(22, 11, 90, 38);
        contentPane.add(btnNewButton);
        
        btnUpdate.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	updateOrderStatus();
            }
        });
        
        btnView.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	selectedRowIndex = table.getSelectedRow();
                new OrderDetailsPopup(TrackOrder.this, (int)table.getValueAt(selectedRowIndex, 0), (String)table.getValueAt(selectedRowIndex, 2));
            }
        });
    }

    /**
     * Display orders based on supply status.
     * 
     * @param status Status to filter orders ('All' or 'Pending').
     */
    private void displayOrders(String status, int wholesaler) {
        //String sql = "SELECT O.OrderID, O.OrderDate, P.Name, O.SupplyStatus FROM Order_details O JOIN Pharmacy P ON P.PharmacyID = O.ORDEREDBYPHARMACY   WHERE O.OrderedFromWholesaler = ?";
    	String sql = "SELECT O.OrderID, O.OrderDate, P.Name, O.SupplyStatus FROM Order_details O JOIN Pharmacy P ON P.PHARMACYID = O.ORDEREDBYPHARMACY   WHERE O.OrderedFromWholesaler <> ?";
        if (status.equals("Pending")) {
            sql += " AND O.SupplyStatus = 'Pending'";
        }

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, wholesaler);
            try (ResultSet rs = pstmt.executeQuery()) {
                // Populate table with data
                DefaultTableModel model = new DefaultTableModel();
                model.setColumnIdentifiers(new Object[] { "OrderID", "Date", "Pharmacy", "Status" });
                while (rs.next()) {
                    model.addRow(new Object[] { rs.getInt("OrderID"), rs.getDate("OrderDate"),
                            rs.getString("Name"), rs.getString("SupplyStatus") });
                }
                table.setModel(model);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Update the status of all orders displayed in the table.
     */
    private void updateOrderStatus() {
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        int rowCount = model.getRowCount();

        String updateOrderSql = "UPDATE Order_details SET SupplyStatus = ? WHERE OrderID = ?";
        String callProcedureSql = "{ call Update_Transaction_Status(?, ?) }";

        try (Connection conn = DatabaseHandler.getConnection();
             PreparedStatement updateOrderStmt = conn.prepareStatement(updateOrderSql);
             CallableStatement callProcedureStmt = conn.prepareCall(callProcedureSql)) {

            conn.setAutoCommit(false); // Set auto-commit to false for transaction control

            for (int i = 0; i < rowCount; i++) {
                int orderId = (int) model.getValueAt(i, 0); // Assuming order ID is in the first column
                String status = (String) model.getValueAt(i, 3); // Assuming status is the 4th column

                // Update Order_details table
                updateOrderStmt.setString(1, status);
                updateOrderStmt.setInt(2, orderId);
                updateOrderStmt.executeUpdate();

                // Call stored procedure to update Transactions table
                callProcedureStmt.setInt(1, orderId); // Set order ID as parameter
                callProcedureStmt.setString(2, status); // Set status as parameter
                callProcedureStmt.executeUpdate();
            }

            conn.commit(); // Commit the transaction
            conn.setAutoCommit(true); // Reset auto-commit to true

            System.out.println("Status updated for all orders in the table.");

        } catch (SQLException e) {
        	e.printStackTrace();
        }

        // Refresh the table to show the updated statuses
        displayOrders("All", wid);
    }

}
