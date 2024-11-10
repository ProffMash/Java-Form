import javax.swing.*;
import java.awt.*;
import java.sql.*;

class AirlineRegistrationForm extends JFrame {

    private JTextField idField, nameField, ageField, emailField;
    private JComboBox<String> userTypeBox;
    private JButton saveButton, newButton, updateButton, cancelButton;

    public AirlineRegistrationForm() {
        setTitle("Airline Registration Form");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new GridBagLayout());

        // Form Fields and Labels
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.insets = new Insets(5, 5, 5, 5);
        constraints.fill = GridBagConstraints.HORIZONTAL;

        JLabel idLabel = createStyledLabel("ID:");
        constraints.gridx = 0;
        constraints.gridy = 0;
        add(idLabel, constraints);

        idField = new JTextField(15);
        constraints.gridx = 1;
        add(idField, constraints);

        JLabel nameLabel = createStyledLabel("Name:");
        constraints.gridx = 0;
        constraints.gridy = 1;
        add(nameLabel, constraints);

        nameField = new JTextField(15);
        constraints.gridx = 1;
        add(nameField, constraints);

        JLabel ageLabel = createStyledLabel("Age:");
        constraints.gridx = 0;
        constraints.gridy = 2;
        add(ageLabel, constraints);

        ageField = new JTextField(15);
        constraints.gridx = 1;
        add(ageField, constraints);

        JLabel emailLabel = createStyledLabel("Email:");
        constraints.gridx = 0;
        constraints.gridy = 3;
        add(emailLabel, constraints);

        emailField = new JTextField(15);
        constraints.gridx = 1;
        add(emailField, constraints);

        JLabel userTypeLabel = createStyledLabel("User Type:");
        constraints.gridx = 0;
        constraints.gridy = 4;
        add(userTypeLabel, constraints);

        userTypeBox = new JComboBox<>(new String[]{"Student", "Patient", "Client"});
        constraints.gridx = 1;
        add(userTypeBox, constraints);

        // Buttons with color styling
        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.setBackground(new Color(240, 248, 255)); // Light blue background

        saveButton = createStyledButton("Save");
        newButton = createStyledButton("New");
        updateButton = createStyledButton("Update");
        cancelButton = createStyledButton("Cancel");

        buttonPanel.add(saveButton);
        buttonPanel.add(newButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(cancelButton);

        constraints.gridx = 0;
        constraints.gridy = 5;
        constraints.gridwidth = 2;
        add(buttonPanel, constraints);

        // Button Actions
        saveButton.addActionListener(e -> saveData());
        newButton.addActionListener(e -> clearFields());
        updateButton.addActionListener(e -> updateData());
        cancelButton.addActionListener(e -> dispose());
    }

    private JLabel createStyledLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Arial", Font.BOLD, 12));
        label.setForeground(new Color(25, 25, 112)); // Dark blue color
        return label;
    }

    private JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setBackground(new Color(70, 130, 180)); // Steel blue color
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Arial", Font.BOLD, 12));
        return button;
    }

    private void saveData() {
        try {
            int id = Integer.parseInt(idField.getText().trim());
            int age = Integer.parseInt(ageField.getText().trim());
            String name = nameField.getText().trim();
            String email = emailField.getText().trim();
            String userType = (String) userTypeBox.getSelectedItem();

            if (name.isEmpty() || email.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please fill all fields", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            String sql = "INSERT INTO users (id, name, age, email, user_type) VALUES (?, ?, ?, ?, ?)";
            try (Connection conn = DatabaseConnection.getConnection();
                 PreparedStatement stmt = conn.prepareStatement(sql)) {

                stmt.setInt(1, id);
                stmt.setString(2, name);
                stmt.setInt(3, age);
                stmt.setString(4, email);
                stmt.setString(5, userType);

                int rowsInserted = stmt.executeUpdate();
                if (rowsInserted > 0) {
                    JOptionPane.showMessageDialog(this, "Data Saved Successfully!");
                    clearFields();
                }
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Database error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "ID and Age must be valid integers", "Input Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updateData() {
        try {
            int id = Integer.parseInt(idField.getText().trim());
            int age = Integer.parseInt(ageField.getText().trim());
            String name = nameField.getText().trim();
            String email = emailField.getText().trim();
            String userType = (String) userTypeBox.getSelectedItem();

            if (name.isEmpty() || email.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please fill all fields", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            String sql = "UPDATE users SET name = ?, age = ?, email = ?, user_type = ? WHERE id = ?";
            try (Connection conn = DatabaseConnection.getConnection();
                 PreparedStatement stmt = conn.prepareStatement(sql)) {

                stmt.setString(1, name);
                stmt.setInt(2, age);
                stmt.setString(3, email);
                stmt.setString(4, userType);
                stmt.setInt(5, id);

                int rowsUpdated = stmt.executeUpdate();
                if (rowsUpdated > 0) {
                    JOptionPane.showMessageDialog(this, "Data Updated Successfully!");
                    clearFields();
                } else {
                    JOptionPane.showMessageDialog(this, "ID not found", "Update Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Database error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "ID and Age must be valid integers", "Input Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void clearFields() {
        idField.setText("");
        nameField.setText("");
        ageField.setText("");
        emailField.setText("");
        userTypeBox.setSelectedIndex(0);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new AirlineRegistrationForm().setVisible(true);
        });
    }
}

class DatabaseConnection {
    private static final String URL = "jdbc:postgresql://ep-hidden-butterfly-a54wv12d.us-east-2.aws.neon.tech/javaform?sslmode=require";
    private static final String USERNAME = "Book Store_owner";
    private static final String PASSWORD = "vJ2zOYt3Vabs";

    public static Connection getConnection() throws SQLException {
        try {
            return DriverManager.getConnection(URL, USERNAME, PASSWORD);
        } catch (SQLException ex) {
            System.out.println("Connection failed: " + ex.getMessage());
            throw ex;
        }
    }
}
