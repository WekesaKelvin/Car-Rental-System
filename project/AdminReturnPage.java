package project;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;

public class AdminReturnPage {

    JFrame frame;
    JTable avaTable, renTable;
    JLabel avaLb,rentLb;
    JScrollPane avaPane,rentPane;
    JPanel panel;
    JButton backBtn, exitBtn;

    AdminReturnPage(){
        frame = new JFrame("Returned Cars");
        frame.setSize(600,600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(null);
        frame.setLocationRelativeTo(null);

        rentLb = new JLabel("Rented Cars");
        rentLb.setBounds(250,25,150,35);

        avaLb = new JLabel("Returned Cars");
        avaLb.setBounds(250,250,150,35);

        renTable = new JTable();
        renTable.setBounds(50,75,500,175);
        renTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        renTable.setFillsViewportHeight(true);

        rentPane = new JScrollPane(renTable);
        rentPane.setBounds(50,75,500,175);

        avaTable = new JTable();
        avaTable.setBounds(50,295,500,175);
        avaTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        avaTable.setFillsViewportHeight(true);

        avaPane = new JScrollPane(avaTable);
        avaPane.setBounds(50,295,500,175);

        backBtn = new JButton("Back");
        backBtn.setFocusable(false);
        backBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.dispose();
                AdminMainMenu amm = new AdminMainMenu();
            }
        });

        exitBtn = new JButton("Exit");
        exitBtn.setFocusable(false);
        exitBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.dispose();
            }
        });

        panel = new JPanel();
        panel.setBounds(200,480,200,25);
        panel.setLayout(new GridLayout(1,2,10,10));
        panel.add(backBtn);
        panel.add(exitBtn);

        getRentRecords();
        getAvailableRecords();

        frame.add(avaLb);
        frame.add(avaPane);
        frame.add(rentLb);
        frame.add(rentPane);
        frame.add(panel);
        frame.setVisible(true);

    }

    private void getRentRecords() {
        try {
            Connection conn = DbConnection.createDBConnection();
            String sql = "SELECT * FROM cars WHERE status ='BOOKED'";
            Statement statement = conn.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);

            ResultSetMetaData metaData = resultSet.getMetaData();
            int columnCount = metaData.getColumnCount();

            // Retrieve column names
            String[] columnNames = new String[columnCount];
            for (int i = 1; i <= columnCount; i++) {
                columnNames[i - 1] = metaData.getColumnName(i);
            }

            // Retrieve query result
            DefaultTableModel model = new DefaultTableModel(0, columnCount);
            renTable.setModel(model);

            while (resultSet.next()) {
                Object[] rowData = new Object[columnCount];
                for (int i = 1; i <= columnCount; i++) {
                    rowData[i - 1] = resultSet.getString(i);
                }
                model.addRow(rowData);
            }

            // Set column names
            model.setColumnIdentifiers(columnNames);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void getAvailableRecords(){

        try {
            Connection conn = DbConnection.createDBConnection();
            String sql = "SELECT * FROM returnTable";
            Statement statement = conn.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);

            ResultSetMetaData metaData = resultSet.getMetaData();
            int columnCount = metaData.getColumnCount();

            // Retrieve column names
            String[] columnNames = new String[columnCount];
            for (int i = 1; i <= columnCount; i++) {
                columnNames[i - 1] = metaData.getColumnName(i);
            }

            // Retrieve query result
            DefaultTableModel model1 = new DefaultTableModel(0, columnCount);
            avaTable.setModel(model1);

            while (resultSet.next()) {
                Object[] rowData = new Object[columnCount];
                for (int i = 1; i <= columnCount; i++) {
                    rowData[i - 1] = resultSet.getString(i);
                }
                model1.addRow(rowData);
            }

            // Set column names
            model1.setColumnIdentifiers(columnNames);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }



}
