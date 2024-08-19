package test1;

import java.awt.Color;
import java.awt.EventQueue;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.JLabel;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;

public class History extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JTable orderTable;
    private JTable requestTable;
    private Connection connection;
    private int wid = 1;

    /**
     * Launch the application.
     
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    History frame = new History();
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
    public History(String role, int wid, String name) {
        try {
            // Establish database connection
            connection = DatabaseHandler.getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 540, 477);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        contentPane.setBackground(new Color(225, 244, 181));

        setContentPane(contentPane);
        contentPane.setLayout(null);

        // Initialize scroll panes for tables
        JScrollPane orderScrollPane = new JScrollPane();
        orderScrollPane.setBounds(24, 98, 481, 138);
        contentPane.add(orderScrollPane);

        JScrollPane requestScrollPane = new JScrollPane();
        requestScrollPane.setBounds(24, 274, 481, 146);
        contentPane.add(requestScrollPane);

        // Initialize order table
        orderTable = new JTable();
        orderScrollPane.setViewportView(orderTable);

        // Initialize request table
        requestTable = new JTable();
        requestScrollPane.setViewportView(requestTable);
        
        JLabel lblNewLabel = new JLabel("Purchase History");
        lblNewLabel.setFont(new Font("Tahoma", Font.PLAIN, 16));
        lblNewLabel.setBounds(24, 69, 127, 28);
        contentPane.add(lblNewLabel);
        
        JLabel lblNewLabel_1 = new JLabel("Waste Collection Requests History");
        lblNewLabel_1.setFont(new Font("Tahoma", Font.PLAIN, 16));
        lblNewLabel_1.setBounds(24, 247, 261, 22);
        contentPane.add(lblNewLabel_1);
        
        JButton btnBack = new JButton("Back");
        btnBack.setBackground(new Color(255,255,255));
        btnBack.setBounds(24, 11, 100, 36);
        contentPane.add(btnBack);
        btnBack.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose();
                WSHome frame = new WSHome(role,wid,name);
				frame.setVisible(true);
            }
        });

        // Display orders and requests
        displayOrders();
        displayRequests();
    }

    /**
     * Display all orders in the table.
     */
    private void displayOrders() {
        String sqlOrders = "SELECT O.OrderID, O.OrderDate, M.Name AS Manufacturer, O.SupplyStatus "
                         + "FROM Order_details O JOIN Manufacturers M ON M.ManufacturerID = O.ORDEREDFROMMANUFACTURER "
                         + "WHERE O.OrderedByWholesaler = ?";

        try (PreparedStatement pstmt = connection.prepareStatement(sqlOrders)) {
            pstmt.setInt(1, wid);
            try (ResultSet rs = pstmt.executeQuery()) {
                // Populate order table with data
                DefaultTableModel orderModel = new DefaultTableModel();
                orderModel.setColumnIdentifiers(new Object[] { "OrderID", "Date", "Manufacturer", "Status" });
                while (rs.next()) {
                    orderModel.addRow(new Object[] { rs.getInt("OrderID"), rs.getDate("OrderDate"),
                            rs.getString("Manufacturer"), rs.getString("SupplyStatus") });
                }
                orderTable.setModel(orderModel);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Display all requests in the table.
     */
    private void displayRequests() {
        String sqlRequests = "SELECT REQUESTID, p.name, SCHEDULED_PICKUP_DATE, STATUS "
                           + "FROM request_details NATURAL JOIN providers p "
                           + "WHERE WHOLESALERID = ?";

        try (PreparedStatement pstmt = connection.prepareStatement(sqlRequests)) {
            pstmt.setInt(1, wid);
            try (ResultSet rs = pstmt.executeQuery()) {
                // Populate request table with data
                DefaultTableModel requestModel = new DefaultTableModel();
                requestModel.setColumnIdentifiers(new Object[] { "RequestID", "Provider", "Scheduled Pickup", "Status" });
                while (rs.next()) {
                    requestModel.addRow(new Object[] { rs.getInt("REQUESTID"), rs.getString("name"),
                            rs.getDate("SCHEDULED_PICKUP_DATE"), rs.getString("STATUS") });
                }
                requestTable.setModel(requestModel);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}