package test1;

import java.sql.*;
import java.util.*;

public class DatabaseHandler {
	private static final String URL = "jdbc:oracle:thin:@//LAVANYA-ASUS:1521/orcl";
    private static final String USER = "scott";
    private static final String PASSWORD = "jaggi";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
    
    public static List<Integer> getMedicineIDs(List<String> medNames, List<String> dosageForms) {
        List<Integer> medicineIDs = new ArrayList<>();
        String sql = "SELECT MedicineID FROM Dosage WHERE MedName = ? AND DosageForm = ?";
        
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            for (int i = 0; i < medNames.size(); i++) {
                String medName = medNames.get(i);
                String dosageForm = dosageForms.get(i);
                pstmt.setString(1, medName);
                pstmt.setString(2, dosageForm);
                ResultSet rs = pstmt.executeQuery();
                if (rs.next()) {
                    int medicineID = rs.getInt("MedicineID");
                    medicineIDs.add(medicineID);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return medicineIDs;
    }

    public boolean signIn(String role, String email, String password) {
        String tableName = getTableNameForRole(role);
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(
                     "SELECT * FROM " + tableName + " WHERE Email = ? AND Password = ?")) {
            pstmt.setString(1, email);
            pstmt.setString(2, password);
            ResultSet rs = pstmt.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }


    private String getTableNameForRole(String role) {
        switch (role) {
            case "Manufacturer":
                return "Manufacturers";
            case "Wholesaler":
                return "Wholesalers";
            case "Pharmacy":
                return "Pharmacy";
            case "Provider":
                return "Providers";
            default:
                throw new IllegalArgumentException("Unknown role: " + role);
        }
    }
    
    
}
