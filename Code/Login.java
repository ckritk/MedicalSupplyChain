package test1;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

class Login extends JFrame implements ActionListener {
    private static final long serialVersionUID = 1L;
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JRadioButton manufacturerRadio;
    private JRadioButton wholesalerRadio;
    private JRadioButton providerRadio;
    private JRadioButton pharmacyRadio;  // New Pharmacy radio button
    private ButtonGroup roleButtonGroup;

    private JButton loginButton;
    private JButton signUpButton;

    private int loggedInUserID = -1;
    private String loggedInRole = "";
    private String loggedInUserName = "";

    private static final String URL = "jdbc:oracle:thin:@//LAVANYA-ASUS:1521/orcl";
    private static final String USER = "scott";
    private static final String PASSWORD = "jaggi";

    // JDBC variables for opening and managing connection
    private static Connection connection;

    public Login() {
        setTitle("Login Form");
        setSize(673, 472);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        String path = "path/to/your/image.jpg";  // Replace with your image path
        ImageIcon backgroundIcon = new ImageIcon("C:\\Users\\lavan\\Downloads\\Green Simple Aesthetic World Environment Day Instagram Post (13).png");

        // Create a JLabel with the image as an icon
        JLabel backgroundLabel = new JLabel(backgroundIcon);
        backgroundLabel.setForeground(new Color(0, 0, 0));
        backgroundLabel.setLayout(null);

        // Set the JLabel as the content pane of the JFrame
        setContentPane(backgroundLabel);

        JLabel usernameLabel = new JLabel("Email");
        usernameLabel.setFont(new Font("Tahoma", Font.BOLD, 16));
        usernameLabel.setForeground(new Color(255, 255, 255));
        usernameLabel.setBounds(95, 204, 123, 25);
        getContentPane().add(usernameLabel);

        usernameField = createRoundedTextField();
        usernameField.setBounds(228, 206, 236, 25);
        getContentPane().add(usernameField);

        JLabel passwordLabel = new JLabel("Password");
        passwordLabel.setFont(new Font("Tahoma", Font.BOLD, 16));
        passwordLabel.setForeground(new Color(255, 255, 255));
        passwordLabel.setBounds(95, 240, 80, 25);
        getContentPane().add(passwordLabel);

        passwordField = createRoundedPasswordField();
        passwordField.setBounds(228, 242, 236, 25);
        getContentPane().add(passwordField);

        JLabel loginAsLabel = new JLabel("Login as");
        loginAsLabel.setFont(new Font("Tahoma", Font.BOLD, 16));
        loginAsLabel.setForeground(new Color(255, 255, 255));
        loginAsLabel.setBounds(95, 286, 80, 25);
        getContentPane().add(loginAsLabel);

        manufacturerRadio = new JRadioButton("Manufacturer");
        customizeRadioButton(manufacturerRadio);
        manufacturerRadio.setBounds(228, 286, 150, 25); 
        getContentPane().add(manufacturerRadio);

        wholesalerRadio = new JRadioButton("Wholesaler");
        wholesalerRadio.setForeground(new Color(255, 255, 255));
        wholesalerRadio.setFont(new Font("Tahoma", Font.BOLD, 15));
        customizeRadioButton(wholesalerRadio);
        wholesalerRadio.setBounds(228, 315, 123, 25);
        getContentPane().add(wholesalerRadio);

        providerRadio = new JRadioButton("Provider");
        providerRadio.setForeground(new Color(255, 255, 255));
        providerRadio.setFont(new Font("Tahoma", Font.BOLD, 15));
        customizeRadioButton(providerRadio);
        providerRadio.setBounds(228, 344, 123, 25);
        getContentPane().add(providerRadio);

        pharmacyRadio = new JRadioButton("Pharmacy");  // New Pharmacy radio button
        pharmacyRadio.setForeground(new Color(255, 255, 255));
        pharmacyRadio.setFont(new Font("Tahoma", Font.BOLD, 15));
        customizeRadioButton(pharmacyRadio);
        pharmacyRadio.setBounds(228, 373, 123, 25);
        getContentPane().add(pharmacyRadio);

        roleButtonGroup = new ButtonGroup();
        roleButtonGroup.add(manufacturerRadio);
        roleButtonGroup.add(wholesalerRadio);
        roleButtonGroup.add(providerRadio);
        roleButtonGroup.add(pharmacyRadio);  // Add Pharmacy to the button group

        loginButton = new JButton("Login");
        loginButton.setBackground(Color.WHITE);
        loginButton.setFont(new Font("Arial Rounded MT Bold", Font.PLAIN, 14));
        loginButton.setBounds(455, 373, 98, 41); // Adjusted position  // Adjust position to accommodate new button
        loginButton.addActionListener(this);
        getContentPane().add(loginButton);

        signUpButton = new JButton("Sign Up");
        signUpButton.setBackground(new Color(226, 251, 254));
        signUpButton.setFont(new Font("Arial Rounded MT Bold", Font.PLAIN, 14));
        signUpButton.setBounds(553, 11, 98, 41);
        signUpButton.addActionListener(this);
        getContentPane().add(signUpButton);

        JLabel transparentLabel = new JLabel("Pharma Supply Chain");
        transparentLabel.setHorizontalAlignment(SwingConstants.CENTER);
        transparentLabel.setVerticalAlignment(SwingConstants.CENTER);
        transparentLabel.setFont(new Font("Tahoma", Font.BOLD, 50));
        transparentLabel.setForeground(Color.WHITE);
        transparentLabel.setOpaque(false);  // Make the label background transparent
        transparentLabel.setBounds(77, 90, 540, 54);
        getContentPane().add(transparentLabel);

        JLabel lblNewLabel = new JLabel("Deliver Health, One Supply at a Time");
        lblNewLabel.setForeground(new Color(255, 255, 255));
        lblNewLabel.setFont(new Font("Tahoma", Font.BOLD, 16));
        lblNewLabel.setBounds(87, 144, 390, 25);
        getContentPane().add(lblNewLabel);
    }

    private void customizeRadioButton(JRadioButton radioButton) {
        radioButton.setFont(new Font("Tahoma", Font.BOLD, 15));
        radioButton.setForeground(new Color(255, 255, 255));
        radioButton.setOpaque(false);
    }

    private JTextField createRoundedTextField() {
        JTextField textField = new JTextField();
        textField.setBackground(new Color(255,255,255));
        textField.setOpaque(false);
        textField.setBorder(new RoundedBorder(2));
        return textField;
    }

    private JPasswordField createRoundedPasswordField() {
        JPasswordField passwordField = new JPasswordField();
        passwordField.setFont(new Font("Tahoma", Font.PLAIN, 15));
        passwordField.setBackground(new Color(255,255,255));
        passwordField.setOpaque(false);
        passwordField.setBorder(new RoundedBorder(2));
        return passwordField;
    }

    private class RoundedBorder implements Border {
        private int radius;

        RoundedBorder(int radius) {
            this.radius = radius;
        }

        public Insets getBorderInsets(Component c) {
            return new Insets(this.radius + 1, this.radius + 1, this.radius + 2, this.radius);
        }

        public boolean isBorderOpaque() {
            return true;
        }

        public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
            g.setColor(Color.GRAY);
            g.drawRoundRect(x, y, width - 1, height - 1, radius, radius);
        }
    }

    private boolean validateLogin(String email, String password, String role) {
        boolean isValid = false;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
            connection = DriverManager.getConnection(URL, USER, PASSWORD);

            String query = null;
            String idColumnAlias = "UserID";
            switch (role) {
                case "Manufacturer":
                    query = "SELECT ManufacturerID AS " + idColumnAlias + ", Name FROM Manufacturers WHERE Email = ? AND Password = ?";
                    break;
                case "Wholesaler":
                    query = "SELECT WholesalerID AS " + idColumnAlias + ", Name FROM Wholesalers WHERE Email = ? AND Password = ?";
                    break;
                case "Provider":
                    query = "SELECT ProviderID AS " + idColumnAlias + ", Name FROM Providers WHERE Email = ? AND Password = ?";
                    break;
                case "Pharmacy":
                    query = "SELECT PharmacyID AS " + idColumnAlias + ", Name FROM Pharmacies WHERE Email = ? AND Password = ?";  // New query for Pharmacy
                    break;
                default:
                    return false;
            }

            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, email);
            preparedStatement.setString(2, password);

            resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                isValid = true;
                loggedInUserID = resultSet.getInt(idColumnAlias);
                loggedInUserName = resultSet.getString("Name");
            }
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (resultSet != null) resultSet.close();
                if (preparedStatement != null) preparedStatement.close();
                if (connection != null) connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return isValid;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String email = usernameField.getText();
        String password = new String(passwordField.getPassword());
        String selectedRole = "";

        if (manufacturerRadio.isSelected()) {
            selectedRole = "Manufacturer";
        } else if (wholesalerRadio.isSelected()) {
            selectedRole = "Wholesaler";
        } else if (providerRadio.isSelected()) {
            selectedRole = "Provider";
        } else if (pharmacyRadio.isSelected()) {
            selectedRole = "Pharmacy";
        }

        if (e.getSource() == loginButton) {
            if (validateLogin(email, password, selectedRole)) {
                JOptionPane.showMessageDialog(this, "Successfully logged in!");
                dispose();
                switch (selectedRole) {
                    case "Manufacturer":
                        //ManufacturerHome manufacturerHome = new ManufacturerHome(loggedInUserID);
                        //manufacturerHome.setVisible(true);
                        break;
                    case "Wholesaler":
                        WSHome wholesalerHome = new WSHome(selectedRole, loggedInUserID, loggedInUserName);
                        wholesalerHome.setVisible(true);
                        break;
                    case "Provider":
                        ProviderHome providerHome = new ProviderHome(loggedInUserID);
                        providerHome.setVisible(true);
                        break;
                    case "Pharmacy":
                        PharmaHome pharmacyHome = new PharmaHome(loggedInUserID, loggedInUserName);  // Open Pharmacy home page
                        pharmacyHome.setVisible(true);
                        break;
                }
            } else {
                JOptionPane.showMessageDialog(this, "Invalid credentials or role. Login failed.");
            }
        } else if (e.getSource() == signUpButton) {
            openSignUpPage();
        }
    }

    private void openSignUpPage() {
        // Implement the sign-up page opening logic here
    }

    public static void main(String[] args) {
        Login login = new Login();
        login.setVisible(true);
    }
}
