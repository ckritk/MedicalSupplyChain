package test1;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.util.ArrayList;
import oracle.jdbc.OracleConnection;

public class ExpiredMediCollec extends JDialog {

    private JTable table;

    public ExpiredMediCollec(JFrame parent, int wholesalerID) {
        super(parent, "Provider Details", true);

        // Initialize components
        table = new JTable();
        JScrollPane scrollPane = new JScrollPane(table);
        JButton btnConfirmRequest = new JButton("Confirm Request");
        btnConfirmRequest.setBackground(new Color(255, 255, 255));

        // Set layout to null for absolute positioning
        getContentPane().setLayout(null);

        // Create a border gap
        JPanel contentPane = new JPanel();
        contentPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        setContentPane(contentPane);
        contentPane.setLayout(null);
        contentPane.setBackground(new Color(225, 244, 181));

        // Add components to the dialog
        scrollPane.setBounds(34, 111, 669, 236); // Adjust the position and size as needed
        contentPane.add(scrollPane);
        btnConfirmRequest.setBounds(527, 358, 176, 42); // Adjust the position and size as needed
        contentPane.add(btnConfirmRequest);

        JLabel lblNewLabel = new JLabel("Select Medicine Waste Collector");
        lblNewLabel.setFont(new Font("Tahoma", Font.PLAIN, 20));
        lblNewLabel.setBounds(34, 69, 345, 46);
        contentPane.add(lblNewLabel);
 

        // Fetch and display provider details
        fetchAndDisplayProviderDetails();

        // Set dialog properties
        setSize(750, 446);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(parent);

        // Add action listener to the button
        btnConfirmRequest.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int selectedRow = table.getSelectedRow();
                if (selectedRow != -1) {
                    String providerName = table.getValueAt(selectedRow, 1).toString();
                    disposeExpiredMedicines(providerName, wholesalerID);
                } else {
                    JOptionPane.showMessageDialog(ExpiredMediCollec.this, "Please select a provider from the table.");
                }
                dispose();
            }
        });
    }

    private void disposeExpiredMedicines(String providerName, int wholesalerID) {
        try (Connection conn = DatabaseHandler.getConnection()) {
            OracleConnection oracleConn = conn.unwrap(OracleConnection.class);
            String sql = "{CALL ProcessExpiredMedicinesInv(?, ?)}";
            try (CallableStatement stmt = oracleConn.prepareCall(sql)) {
                stmt.setString(1, providerName);
                stmt.setInt(2, wholesalerID);
                boolean res = stmt.execute();
                System.out.println(res);
                System.out.println("Expired medicines processed successfully.");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    private void fetchAndDisplayProviderDetails() {
        // Fetch provider details from the database
        String providerSql = "SELECT PROVIDERID, NAME, EMAIL, PHNO, COVERAGEAREA, CHARGEPERPACKAGE FROM providers";
        try (Connection conn = DatabaseHandler.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(providerSql)) {

            // Populate table model
            DefaultTableModel model = new DefaultTableModel();
            model.addColumn("ID");
            model.addColumn("Name");
            model.addColumn("Email");
            model.addColumn("Phone");
            model.addColumn("Coverage Area");
            model.addColumn("Charge Per Pkg");

            while (rs.next()) {
                model.addRow(new Object[]{
                        rs.getInt("PROVIDERID"),
                        rs.getString("NAME"),
                        rs.getString("EMAIL"),
                        rs.getString("PHNO"),
                        rs.getString("COVERAGEAREA"),
                        rs.getDouble("CHARGEPERPACKAGE")
                });
            }
            table.setModel(model);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame();
       
    }
}

