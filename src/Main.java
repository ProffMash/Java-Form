import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

class AirlineRegistrationForm extends JFrame {

    private JTextField idField, nameField, ageField, emailField;
    private JComboBox<String> userTypeBox;
    private JButton saveButton, newButton, updateButton, cancelButton;

    public AirlineRegistrationForm() {
        setTitle("Airline Registration Form");
        setSize(500, 350); // Increased size for better spacing
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new GridBagLayout());
        getContentPane().setBackground(new Color(245, 245, 245)); // Light gray background

        // Form Fields and Labels
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.insets = new Insets(10, 10, 10, 10);
        constraints.fill = GridBagConstraints.HORIZONTAL;

        addField(constraints, "ID:", idField = new JTextField(15), 0);
        addField(constraints, "Name:", nameField = new JTextField(15), 1);
        addField(constraints, "Age:", ageField = new JTextField(15), 2);
        addField(constraints, "Email:", emailField = new JTextField(15), 3);

        JLabel userTypeLabel = createStyledLabel("User Type:");
        constraints.gridx = 0;
        constraints.gridy = 4;
        add(userTypeLabel, constraints);

        userTypeBox = new JComboBox<>(new String[]{"Student", "Patient", "Client"});
        userTypeBox.setFont(new Font("Arial", Font.PLAIN, 14));
        constraints.gridx = 1;
        add(userTypeBox, constraints);

        // Buttons Panel with Improved Styling
        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.setBackground(new Color(245, 245, 245));

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

    private void addField(GridBagConstraints constraints, String label, JTextField field, int row) {
        JLabel lbl = createStyledLabel(label);
        constraints.gridx = 0;
        constraints.gridy = row;
        add(lbl, constraints);

        field.setFont(new Font("Arial", Font.PLAIN, 14));
        constraints.gridx = 1;
        add(field, constraints);
    }

    private JLabel createStyledLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Arial", Font.BOLD, 14));
        label.setForeground(new Color(54, 54, 54));
        return label;
    }

    private JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setBackground(new Color(70, 130, 180));
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Arial", Font.BOLD, 12));
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15)); // Padding around text
        return button;
    }

    private void saveData() {
        String id = idField.getText();
        String name = nameField.getText();
        String age = ageField.getText();
        String email = emailField.getText();
        String userType = (String) userTypeBox.getSelectedItem();

        if (id.isEmpty() || name.isEmpty() || age.isEmpty() || email.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill all fields", "Error", JOptionPane.ERROR_MESSAGE);
        } else {
            try {
                int idInt = Integer.parseInt(id);  // Convert id to integer
                // Save to the database
                String sql = "INSERT INTO users (id, name, age, email, user_type) VALUES (?, ?, ?, ?, ?)";
                try (Connection conn = DatabaseConnection.getConnection();
                     PreparedStatement stmt = conn.prepareStatement(sql)) {

                    stmt.setInt(1, idInt);  // Set the id as integer
                    stmt.setString(2, name);
                    stmt.setInt(3, Integer.parseInt(age));
                    stmt.setString(4, email);
                    stmt.setString(5, userType);

                    int rowsInserted = stmt.executeUpdate();
                    if (rowsInserted > 0) {
                        JOptionPane.showMessageDialog(this, "Data Saved Successfully!");
                        clearFields();
                    }
                } catch (SQLException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(this, "Error saving data: " + ex.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "ID must be a valid integer", "Input Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void updateData() {
        String id = idField.getText();
        if (id.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Enter an ID to update", "Error", JOptionPane.ERROR_MESSAGE);
        } else {
            String sql = "UPDATE users SET name = ?, age = ?, email = ?, user_type = ? WHERE id = ?";
            try (Connection conn = DatabaseConnection.getConnection();
                 PreparedStatement stmt = conn.prepareStatement(sql)) {

                stmt.setString(1, nameField.getText());
                stmt.setInt(2, Integer.parseInt(ageField.getText()));
                stmt.setString(3, emailField.getText());
                stmt.setString(4, (String) userTypeBox.getSelectedItem());
                stmt.setInt(5, Integer.parseInt(id)); // Set the id as integer

                int rowsUpdated = stmt.executeUpdate();
                if (rowsUpdated > 0) {
                    JOptionPane.showMessageDialog(this, "Data Updated Successfully!");
                    clearFields();
                } else {
                    JOptionPane.showMessageDialog(this, "ID not found", "Update Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error updating data: " + ex.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "ID must be a valid integer", "Input Error", JOptionPane.ERROR_MESSAGE);
            }
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
        SwingUtilities.invokeLater(() -> new AirlineRegistrationForm().setVisible(true));
    }
}

class DatabaseConnection {
    private static final String URL = "";
    private static final String USERNAME = "";
    private static final String PASSWORD = "";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USERNAME, PASSWORD);
    }
}
