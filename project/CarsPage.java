package project;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

public class CarsPage extends JFrame {
    private DefaultTableModel tableModel;
    private Connection connection;
    private JTextField carIdTextField;
    private JTextField carMakeTextField;
    private JTextField carTypeTextField;
    private JTextField statusTextField;
    private JTextField priceTextField;
    private JTable table;

    public CarsPage() {
        setTitle("Manage Cars");
        connection = DbConnection.createDBConnection();
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        JLabel titleLabel = new JLabel("Manage Cars");
        JLabel carIdLabel = new JLabel("CarId");
        JLabel carMakeLabel = new JLabel("CarMake");
        JLabel carTypeLabel = new JLabel("CarType");
        JLabel statusLabel = new JLabel("Status");
        JLabel priceLabel = new JLabel("Price");
        carIdTextField = new JTextField(15);
        carMakeTextField = new JTextField(15);
        carTypeTextField = new JTextField(15);
        statusTextField = new JTextField(15);
        priceTextField = new JTextField(15);

        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));

        panel.add(titleLabel);

        panel.add(createHorizontalPanel(carIdLabel, carIdTextField));
        panel.add(createHorizontalPanel(carMakeLabel, carMakeTextField));
        panel.add(createHorizontalPanel(carTypeLabel, carTypeTextField));
        panel.add(createHorizontalPanel(statusLabel, statusTextField));
        panel.add(createHorizontalPanel(priceLabel, priceTextField));

        JPanel buttonPanel = createHorizontalPanel(
                createButton("Save", this::saveCar),
                createButton("Delete", this::deleteCar),
                createButton("Reset", this::resetFields),
                createButton("Edit", this::editCar)
        );

        panel.add(buttonPanel);

        String[] columnNames = {"CarId", "CarMake", "CarType", "Status", "fees"};
        tableModel = new DefaultTableModel(columnNames, 0);
        table = new JTable(tableModel);
        JScrollPane tableScrollPane = new JScrollPane(table);
        table.setFillsViewportHeight(true);
        panel.add(tableScrollPane);

        loadData();

        add(panel);

        pack();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private JPanel createHorizontalPanel(Component... components) {
        JPanel panel = new JPanel();
        for (Component component : components) {
            panel.add(component);
        }
        return panel;
    }

    private JButton createButton(String text, ActionListener actionListener) {
        JButton button = new JButton(text);
        button.addActionListener(actionListener);
        return button;
    }

    private void loadData() {
        String query = "SELECT carId, make, type, status, fees FROM cars " +
                "JOIN carMake ON cars.makeId = carMake.makeId " +
                "JOIN carType ON cars.typeId = carType.typeId";
        try (PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                String carId = resultSet.getString("carId");
                String carMake = resultSet.getString("make");
                String carType = resultSet.getString("type");
                String status = resultSet.getString("status");
                String price = resultSet.getString("fees");
                tableModel.addRow(new String[]{carId, carMake, carType, status, price});
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    private void saveCar(ActionEvent e) {
        String carMake = carMakeTextField.getText();
        String carType = carTypeTextField.getText();
        String status = statusTextField.getText();
        String price = priceTextField.getText();

        String insertQuery = "INSERT INTO cars (makeId, typeId, CarName, status, fees) " +
                "VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement insertStatement = connection.prepareStatement(insertQuery)) {
            // Assuming that makeId and typeId are already defined in the carMake and carType tables
            insertStatement.setInt(1, getMakeIdByName(carMake));
            insertStatement.setInt(2, getTypeIdByName(carType));
            insertStatement.setString(3, carMake + " " + carType);
            insertStatement.setString(4, status);
            insertStatement.setString(5, price);
            insertStatement.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error occurred while saving the car.", "Save Error",
                    JOptionPane.ERROR_MESSAGE);
        }

        tableModel.setRowCount(0);
        loadData();

        // Clear the text fields after saving
        carMakeTextField.setText("");
        carTypeTextField.setText("");
        statusTextField.setText("");
        priceTextField.setText("");
    }

    private void deleteCar(ActionEvent e) {
        int selectedRow = table.getSelectedRow();
        if (selectedRow != -1) {
            int confirmDelete = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete this car?",
                    "Confirm Deletion", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
            if (confirmDelete == JOptionPane.YES_OPTION) {
                String carId = table.getValueAt(selectedRow, 0).toString();
                String deleteQuery = "DELETE FROM cars WHERE carId = ?";
                try (PreparedStatement deleteStatement = connection.prepareStatement(deleteQuery)) {
                    deleteStatement.setString(1, carId);
                    deleteStatement.executeUpdate();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }

                // Reload the data to update the table
                tableModel.setRowCount(0);
                loadData();
            }
        } else {
            JOptionPane.showMessageDialog(this, "Please select a row to delete.", "No Row Selected",
                    JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void resetFields(ActionEvent e) {
        carMakeTextField.setText("");
        carTypeTextField.setText("");
        statusTextField.setText("");
        priceTextField.setText("");
    }

    private void editCar(ActionEvent e) {
        int selectedRow = table.getSelectedRow();
        if (selectedRow != -1) {
            carIdTextField.setText(table.getValueAt(selectedRow, 0).toString());
            carMakeTextField.setText(table.getValueAt(selectedRow, 1).toString());
            carTypeTextField.setText(table.getValueAt(selectedRow, 2).toString());
            statusTextField.setText(table.getValueAt(selectedRow, 3).toString());
            priceTextField.setText(table.getValueAt(selectedRow, 4).toString());
        } else {
            JOptionPane.showMessageDialog(this, "Please select a row to edit.", "No Row Selected",
                    JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private int getMakeIdByName(String make) throws SQLException {
        String query = "SELECT makeId FROM carMake WHERE make = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, make);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt("makeId");
                }
            }
        }
        return 0;
    }

    private int getTypeIdByName(String type) throws SQLException {
        String query = "SELECT typeId FROM carType WHERE type = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, type);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt("typeId");
                }
            }
        }
        return 0;
    }

}
