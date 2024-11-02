package project;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.time.LocalDate;


public class Customers extends JFrame {
    private final JTable table;
    private final JButton deleteButton;
    private final JButton editButton;
    private final JButton BackButton;
    Connection conn = DbConnection.createDBConnection();

    public Customers() {
        setTitle("Admin Users");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 400);

        table = new JTable();
        JScrollPane scrollPane = new JScrollPane(table);

        deleteButton = new JButton("Delete");
        deleteButton.setEnabled(false);

        editButton = new JButton("Edit");
        editButton.setEnabled(false);

        BackButton = new JButton("Back");
        BackButton.setEnabled(true);

        JButton addButton = new JButton("Add");

        deleteButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int selectedRow = table.getSelectedRow();
                if (selectedRow != -1) {
                    int userId = (int) table.getValueAt(selectedRow, 0);
                    deleteRecord(userId);
                }
            }
        });

        editButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int selectedRow = table.getSelectedRow();
                if (selectedRow != -1) {
                    int userId = (int) table.getValueAt(selectedRow, 0);
                    editRecord(userId);
                }
            }
        });

        addButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                addRecord();
            }
        });
        BackButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                AdminMainMenu mm = new AdminMainMenu();
                dispose();
            }
        });

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(deleteButton);
        buttonPanel.add(editButton);
        buttonPanel.add(addButton);
        buttonPanel.add(BackButton);

        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(scrollPane, BorderLayout.CENTER);
        getContentPane().add(buttonPanel, BorderLayout.SOUTH);

        setVisible(true);
        loadRecords();
    }

    private void loadRecords() {
        try  {

            PreparedStatement statement = conn.prepareStatement("SELECT * FROM users");
            ResultSet resultSet = statement.executeQuery();

            table.setModel(buildTableModel(resultSet));
            deleteButton.setEnabled(true);
            editButton.setEnabled(true);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void deleteRecord(int userId) {
        int confirmation = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete this user?",
                "Confirm Delete", JOptionPane.YES_NO_OPTION);
        if (confirmation == JOptionPane.YES_OPTION) {
            try {

                PreparedStatement statement = conn.prepareStatement("DELETE FROM users WHERE userId = ?");
                statement.setInt(1, userId);
                statement.executeUpdate();

                loadRecords();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private void editRecord(int userId) {
        // Retrieve user information from the database based on the userId
        try  {
            PreparedStatement statement = conn.prepareStatement("SELECT * FROM users WHERE userId = ?");
            statement.setInt(1, userId);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                String fullName = resultSet.getString("FullName");
                String phoneNumber = resultSet.getString("PhoneNumber");
                String email = resultSet.getString("Email");
                String address = resultSet.getString("Address");
                Date dob = resultSet.getDate("DOB");
                String nationalId = resultSet.getString("NationalId");

                // Prompt the admin to edit the user information
                JTextField fullNameField = new JTextField(fullName);
                JTextField phoneNumberField = new JTextField(phoneNumber);
                JTextField emailField = new JTextField(email);
                JTextField addressField = new JTextField(address);
                JFormattedTextField dobField = new JFormattedTextField(dob);
                JTextField nationalIdField = new JTextField(nationalId);

                JPanel panel = new JPanel(new GridLayout(7, 2));
                panel.add(new JLabel("Full Name:"));
                panel.add(fullNameField);
                panel.add(new JLabel("Phone Number:"));
                panel.add(phoneNumberField);
                panel.add(new JLabel("Email:"));
                panel.add(emailField);
                panel.add(new JLabel("Address:"));
                panel.add(addressField);
                panel.add(new JLabel("Date of Birth (YYYY-MM-DD):"));
                panel.add(dobField);
                panel.add(new JLabel("National ID:"));
                panel.add(nationalIdField);

                int option = JOptionPane.showConfirmDialog(this, panel, "Edit User", JOptionPane.OK_CANCEL_OPTION);

                if (option == JOptionPane.OK_OPTION) {
                    String newFullName = fullNameField.getText();
                    String newPhoneNumber = phoneNumberField.getText();
                    String newEmail = emailField.getText();
                    String newAddress = addressField.getText();
                    Date newDob = Date.valueOf(dobField.getText());
                    String newNationalId = nationalIdField.getText();

                    PreparedStatement updateStatement = conn.prepareStatement(
                            "UPDATE users SET FullName = ?, PhoneNumber = ?, Email = ?, Address = ?, DOB = ?, NationalId = ? WHERE userId = ?");
                    updateStatement.setString(1, newFullName);
                    updateStatement.setString(2, newPhoneNumber);
                    updateStatement.setString(3, newEmail);
                    updateStatement.setString(4, newAddress);
                    updateStatement.setDate(5, newDob);
                    updateStatement.setString(6, newNationalId);
                    updateStatement.setInt(7, userId);
                    updateStatement.executeUpdate();

                    loadRecords();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void addRecord() {
        JTextField fullNameField = new JTextField();
        JTextField phoneNumberField = new JTextField();
        JTextField emailField = new JTextField();
        JTextField addressField = new JTextField();
        JFormattedTextField dobField = new JFormattedTextField(LocalDate.now());
        JTextField nationalIdField = new JTextField();

        JPanel panel = new JPanel(new GridLayout(7, 2));
        panel.add(new JLabel("Full Name:"));
        panel.add(fullNameField);
        panel.add(new JLabel("Phone Number:"));
        panel.add(phoneNumberField);
        panel.add(new JLabel("Email:"));
        panel.add(emailField);
        panel.add(new JLabel("Address:"));
        panel.add(addressField);
        panel.add(new JLabel("Date of Birth (YYYY-MM-DD):"));
        panel.add(dobField);
        panel.add(new JLabel("National ID:"));
        panel.add(nationalIdField);

        int option = JOptionPane.showConfirmDialog(this, panel, "Add User", JOptionPane.OK_CANCEL_OPTION);

        if (option == JOptionPane.OK_OPTION) {
            String fullName = fullNameField.getText();
            String phoneNumber = phoneNumberField.getText();
            String email = emailField.getText();
            String address = addressField.getText();
            Date dob = Date.valueOf(dobField.getText());
            String nationalId = nationalIdField.getText();

            try {
                PreparedStatement statement = conn.prepareStatement(
                        "INSERT INTO users (FullName, PhoneNumber, Email, Address, DOB, NationalId) VALUES (?, ?, ?, ?, ?, ?)");
                statement.setString(1, fullName);
                statement.setString(2, phoneNumber);
                statement.setString(3, email);
                statement.setString(4, address);
                statement.setDate(5, dob);
                statement.setString(6, nationalId);
                statement.executeUpdate();

                loadRecords();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }




    private static DefaultTableModel buildTableModel(ResultSet resultSet) throws SQLException {
        ResultSetMetaData metaData = resultSet.getMetaData();
        int columnCount = metaData.getColumnCount();

        // Create column names
        String[] columnNames = new String[columnCount];
        for (int column = 0; column < columnCount; column++) {
            columnNames[column] = metaData.getColumnName(column + 1);
        }

        // Create data array
        Object[][] data = new Object[100][columnCount];
        int row = 0;
        while (resultSet.next()) {
            for (int column = 0; column < columnCount; column++) {
                if (metaData.getColumnType(column + 1) == Types.INTEGER) {
                    data[row][column] = resultSet.getInt(column + 1);
                } else {
                    data[row][column] = resultSet.getObject(column + 1);
                }
            }
            row++;
        }

        return new DefaultTableModel(data, columnNames);
    }

  /*  public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                Customers page = new Customers();
                page.setVisible(true);
            }
        });
    }*/
}

