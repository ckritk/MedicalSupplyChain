package test1;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.Color;
import javax.swing.JLabel;
import java.awt.Font;

public class ProviderHome extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JTable table;
    private DefaultTableModel tableModel;

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    ProviderHome frame = new ProviderHome(5);
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
    public ProviderHome(int pid) {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 600, 400);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setBackground(new Color(225, 244, 181));

        // Create a table model
        tableModel = new DefaultTableModel(
            new Object[][] {},
            new String[] {
                "REQUESTID", "ENTITY_NAME", "PICKUP_DATE", "STATUS"
            }
        );
        contentPane.setLayout(null);

        // Create a table with the table model
        table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBounds(27, 150, 526, 137);
        contentPane.add(scrollPane);
        JButton btnSave = new JButton("Save Changes");
        btnSave.setBackground(new Color(255, 255, 255));
        btnSave.setBounds(428, 298, 125, 32);
        contentPane.add(btnSave);
        JButton btnPending = new JButton("Pending");
        btnPending.setBackground(new Color(255, 255, 255));
        btnPending.setBounds(297, 107, 120, 32);
        contentPane.add(btnPending);
        JButton btnCompleted = new JButton("Completed");
        btnCompleted.setBackground(new Color(255, 255, 255));
        btnCompleted.setBounds(162, 107, 125, 32);
        contentPane.add(btnCompleted);

        // Create and add buttons
        JButton btnAll = new JButton("All");
        btnAll.setBackground(new Color(255, 255, 255));
        btnAll.setBounds(27, 107, 125, 32);
        contentPane.add(btnAll);
        
        JLabel lblNewLabel = new JLabel("Waste Collection Requests");
        lblNewLabel.setFont(new Font("Tahoma", Font.PLAIN, 40));
        lblNewLabel.setBounds(27, 47, 480, 49);
        contentPane.add(lblNewLabel);

        // Add action listeners to buttons
        btnAll.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                loadData(null, pid);
            }
        });
        btnCompleted.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                loadData("Y", pid);
            }
        });
        btnPending.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                loadData("N", pid);
            }
        });
        btnSave.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                saveChanges();
            }
        });

        // Load all data initially
        loadData(null, pid);
    }

    private void loadData(String statusFilter, int pid) {
        try {
            // Database connection
            Connection connection = DatabaseHandler.getConnection();
            String query = "SELECT rd.REQUESTID, "
                         + "COALESCE(p.NAME, w.NAME, m.NAME) AS ENTITY_NAME, "
                         + "rd.SCHEDULED_PICKUP_DATE, rd.STATUS "
                         + "FROM request_details rd "
                         + "LEFT JOIN pharmacy p ON rd.PHARMACYID = p.PHARMACYID "
                         + "LEFT JOIN wholesalers w ON rd.WHOLESALERID = w.WHOLESALERID "
                         + "LEFT JOIN manufacturers m ON rd.MANUFACTURERID = m.MANUFACTURERID "
                         + "WHERE rd.ProviderID = ?";
            if (statusFilter != null) {
                query += " AND rd.STATUS = ?";
            }

            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, pid);

            if (statusFilter != null) {
                preparedStatement.setString(2, statusFilter);
            }

            ResultSet resultSet = preparedStatement.executeQuery();

            // Clear existing data
            tableModel.setRowCount(0);

            // Populate table with data
            while (resultSet.next()) {
                Object[] row = {
                    resultSet.getInt("REQUESTID"),
                    resultSet.getString("ENTITY_NAME"),
                    resultSet.getDate("SCHEDULED_PICKUP_DATE"),
                    resultSet.getString("STATUS")
                };
                tableModel.addRow(row);
            }

            // Close connections
            resultSet.close();
            preparedStatement.close();
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void saveChanges() {
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        int rowCount = model.getRowCount();

        String requestSql = "UPDATE Request_Details SET Status = ?, Scheduled_pickup_date = ? WHERE RequestID = ?";
        String transactionSql = "UPDATE Transactions SET OrderStatus = ? WHERE RequestID = ?";

        try (Connection conn = DatabaseHandler.getConnection();
             PreparedStatement requestPstmt = conn.prepareStatement(requestSql);
             PreparedStatement transactionPstmt = conn.prepareStatement(transactionSql)) {

            for (int i = 0; i < rowCount; i++) {
                int requestID = (int) model.getValueAt(i, 0); // Assuming RequestID is in column 0
                String status = (String) model.getValueAt(i, 3); // Assuming Status is in column 1
                Object pickupDateObj = model.getValueAt(i, 2); // Assuming Scheduled_pickup_date is in column 2

                if (pickupDateObj instanceof java.util.Date) {
                    java.util.Date pickupDate = (java.util.Date) pickupDateObj;
                    requestPstmt.setString(1, status);
                    requestPstmt.setDate(2, new java.sql.Date(pickupDate.getTime()));
                    requestPstmt.setInt(3, requestID);
                    requestPstmt.executeUpdate();
                } else if (pickupDateObj instanceof String) {
                    // Parse the String to Date
                    String dateString = (String) pickupDateObj;
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                    try {
                        java.util.Date pickupDate = dateFormat.parse(dateString);
                        requestPstmt.setString(1, status);
                        requestPstmt.setDate(2, new java.sql.Date(pickupDate.getTime()));
                        requestPstmt.setInt(3, requestID);
                        requestPstmt.executeUpdate();
                    } catch (ParseException e) {
                        System.out.println("Error parsing date for row: " + (i + 1));
                        e.printStackTrace();
                        // Handle the parsing error as needed
                    }
                } else {
                    System.out.println("Unexpected type for pickup date object in row: " + (i + 1));
                    // Handle the unexpected type case as needed
                }

                // Update the Transactions table based on RequestID
                String orderStatus = status.equals("Y") ? "Completed" : "Pending"; // Example status mapping
                transactionPstmt.setString(1, orderStatus);
                transactionPstmt.setInt(2, requestID);
                transactionPstmt.executeUpdate();

                // Call the stored procedure to clear collected medicines
                callClearCollectedMedicinesInv(conn, requestID);
            }

            System.out.println("Request details and transactions updated successfully.");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    private void callClearCollectedMedicinesInv(Connection connection, int requestId) {
        try {
            CallableStatement callableStatement = connection.prepareCall("{CALL ClearCollectedMedicinesInv(?)}");
            callableStatement.setInt(1, requestId);
            callableStatement.execute();
            callableStatement.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}