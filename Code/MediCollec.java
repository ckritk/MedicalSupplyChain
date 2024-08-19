package test1;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.util.*;

public class MediCollec extends JDialog {

    private JTable table;

    public MediCollec(JFrame parent, ArrayList<Integer> medicineID, ArrayList<Integer> quantity, int wholesalerID) {
        super(parent, "Provider Details", true);

        // Initialize components
        table = new JTable();
        JScrollPane scrollPane = new JScrollPane(table);
        JButton btnConfirmRequest = new JButton("Confirm Request");
        btnConfirmRequest.setBackground(new Color(255,255,255));

        // Set layout to null for absolute positioning
        getContentPane().setLayout(null);

        // Create a border gap
        JPanel contentPane = new JPanel();
        contentPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        setContentPane(contentPane);
        contentPane.setLayout(null);
        contentPane.setBackground(new Color(225, 244, 181));

        // Add components to the dialog
        scrollPane.setBounds(34, 54, 669, 229); // Adjust the position and size as needed
        contentPane.add(scrollPane);
        btnConfirmRequest.setBounds(506, 308, 176, 30); // Adjust the position and size as needed
        contentPane.add(btnConfirmRequest);
        
        JLabel lblNewLabel = new JLabel("Select MediWaste Collector");
        lblNewLabel.setFont(new Font("Tahoma", Font.PLAIN, 14));
        lblNewLabel.setBounds(51, 11, 272, 46);
        contentPane.add(lblNewLabel);

        // Fetch and display provider details
        fetchAndDisplayProviderDetails();

        // Add action listener to the Confirm Request button
        btnConfirmRequest.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Add your logic for confirming the request here
            	int selectedRow = table.getSelectedRow();
                if (selectedRow == -1) {
                    return;
                }
                int providerID = (int) table.getValueAt(selectedRow, 0);

                Connection conn = null;
                PreparedStatement pstmt = null;
                ResultSet rs = null;

                try {
                    conn = DatabaseHandler.getConnection();
                    conn.setAutoCommit(false);

                    // Step 1: Get the next request ID from the sequence
                    String getRequestIDQuery = "SELECT RequestSeq.NEXTVAL FROM dual";
                    pstmt = conn.prepareStatement(getRequestIDQuery);
                    rs = pstmt.executeQuery();
                    rs.next();
                    int requestID = rs.getInt(1);
                    rs.close();
                    pstmt.close();

                    // Step 2: Insert into request_details
                    String insertRequestDetailsQuery = "INSERT INTO request_details (REQUESTID, PHARMACYID, WHOLESALERID, MANUFACTURERID, PROVIDERID, SCHEDULED_PICKUP_DATE, STATUS) " +
                                                       "VALUES (?, ?, ?, ?, ?, ?, ?)";
                    pstmt = conn.prepareStatement(insertRequestDetailsQuery);
                    pstmt.setInt(1, requestID);
                    pstmt.setNull(2, Types.INTEGER);  // Assuming PHARMACYID is NULL, you can set it if needed
                    pstmt.setInt(3, wholesalerID);
                    pstmt.setNull(4, Types.INTEGER);  // MANUFACTURERID is NULL
                    pstmt.setInt(5, providerID);
                    pstmt.setNull(6, Types.DATE);  // SCHEDULED_PICKUP_DATE is NULL
                    pstmt.setString(7, "N");  // STATUS, assuming 'N' for a new request
                    pstmt.executeUpdate();
                    pstmt.close();

                    // Step 3: Insert into collection_requests
                    String insertCollectionRequestsQuery = "INSERT INTO collection_requests (REQUESTID, WASTETYPE, QUANTITY) " +
                                                           "VALUES (?, ?, ?)";
                    pstmt = conn.prepareStatement(insertCollectionRequestsQuery);
                    for (int i = 0; i < medicineID.size(); i++) {
                        pstmt.setInt(1, requestID);
                        pstmt.setInt(2, medicineID.get(i));
                        pstmt.setInt(3, quantity.get(i));
                        pstmt.addBatch();
                    }
                    pstmt.executeBatch();

                    // Commit the transaction
                    conn.commit();

                    } catch (SQLException ex) {
                    // Handle any SQL errors
                    if (conn != null) {
                        try {
                            conn.rollback();
                        } catch (SQLException e1) {
                            e1.printStackTrace();
                        }
                    }
                    ex.printStackTrace();
                    } finally {
                    // Clean up and close connections
                    try {
                        if (rs != null) rs.close();
                        if (pstmt != null) pstmt.close();
                        if (conn != null) conn.close();
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                    }
                }
                dispose();
               }
        });

        // Set dialog properties
        setSize(750, 400);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(parent);
      
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
}
