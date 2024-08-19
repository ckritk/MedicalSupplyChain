package test1;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.JButton;
import java.awt.Font;
import java.awt.Color;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JLabel;

public class PharmaHome extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JTable table;
    private int pharmacyId;

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    PharmaHome frame = new PharmaHome(2,"CureWell Pharmacy"); // Pass the pharmacyId here
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
    public PharmaHome(int pharmacyId, String name) {
        this.pharmacyId = pharmacyId;
        
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 596, 421); // Adjust the size if necessary
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);
        contentPane.setBackground(new Color(225, 244, 181));
        
        // Create table with DefaultTableModel
        table = new JTable();
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBounds(22, 138, 543, 185);
        contentPane.add(scrollPane);
        
        JButton btnOrder = new JButton("Order Medicine");
        btnOrder.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		dispose();
        		try {
                    OrderPharma frame = new OrderPharma(pharmacyId,name); // Example PharmacyID
                    frame.setVisible(true);
                } catch (Exception ee) {
                    ee.printStackTrace();
                }
        	}
        });
        btnOrder.setBackground(new Color(255, 255, 255));
        btnOrder.setFont(new Font("Tahoma", Font.BOLD, 11));
        btnOrder.setBounds(22, 334, 138, 29);
        contentPane.add(btnOrder);
        
        JButton btnDispose = new JButton("Dispose Medicine");
        btnDispose.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		dispose();
        		try {
                    DisposeMed frame = new DisposeMed(pharmacyId,name); // Example PharmacyID
                    frame.setVisible(true);
                } catch (Exception eee) {
                    eee.printStackTrace();
                }
        	}
        });
        btnDispose.setFont(new Font("Tahoma", Font.BOLD, 11));
        btnDispose.setBackground(new Color(255, 255, 255));
        btnDispose.setBounds(170, 334, 138, 29);
        contentPane.add(btnDispose);
        
        JButton btnOrderDet = new JButton("View Order");
        btnOrderDet.setFont(new Font("Tahoma", Font.BOLD, 11));
        btnOrderDet.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Get the selected row index
                int selectedRowIndex = table.getSelectedRow();
                if (selectedRowIndex != -1) { // If a row is selected
                    // Get the order ID from the first column of the selected row
                    int orderId = (int) table.getValueAt(selectedRowIndex, 0);
                    String name = (String) table.getValueAt(selectedRowIndex, 2);

                    // Open the OrderDetailsPopup for the selected order ID
                    OrderDetails2 popup = new OrderDetails2(PharmaHome.this, orderId, name);
                }
            }
        });
        btnOrderDet.setBackground(new Color(255, 255, 255));
        btnOrderDet.setBounds(318, 334, 113, 29);
        contentPane.add(btnOrderDet);
        
        JLabel lblNewLabel = new JLabel("Orders");
        lblNewLabel.setFont(new Font("Tahoma", Font.PLAIN, 50));
        lblNewLabel.setBackground(new Color(255, 255, 255));
        lblNewLabel.setBounds(22, 77, 311, 61);
        contentPane.add(lblNewLabel);
        
        JLabel lblNewLabel_1 = new JLabel(name);
        lblNewLabel_1.setFont(new Font("Tahoma", Font.PLAIN, 15));
        lblNewLabel_1.setBounds(22, 63, 209, 14);
        contentPane.add(lblNewLabel_1);
        
        JButton btnNewButton = new JButton("SignOut");
        btnNewButton.setBackground(new Color(226, 251, 254));
        btnNewButton.setBounds(476, 11, 89, 38);
        contentPane.add(btnNewButton);
        
        // Load data from database
        loadData();
    }

    private void loadData() {
        String query = "SELECT ORDERID, ORDERDATE, w.Name, SUPPLYSTATUS "
                     + "FROM order_details o JOIN wholesalers w ON o.orderedfromwholesaler = w.wholesalerid"
                     + " WHERE ORDEREDBYPHARMACY = ?";
        
        try (Connection con = DatabaseHandler.getConnection();
             PreparedStatement pst = con.prepareStatement(query)) {
             
            pst.setInt(1, pharmacyId);
            ResultSet rs = pst.executeQuery();
            
            // Create table model and set it to the JTable
            DefaultTableModel model = new DefaultTableModel(new Object[]{"Order ID", "Order Date", "Wholesaler", "Supply Status"}, 0);
            while (rs.next()) {
                int orderId = rs.getInt("ORDERID");
                java.sql.Date orderDate = rs.getDate("ORDERDATE");
                String orderedFromWholesaler = rs.getString("name");
                String supplyStatus = rs.getString("SUPPLYSTATUS");
                model.addRow(new Object[]{orderId, orderDate, orderedFromWholesaler, supplyStatus});
            }
            table.setModel(model);
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
