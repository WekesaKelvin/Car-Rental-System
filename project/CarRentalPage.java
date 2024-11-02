package project;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.text.DateFormat;

public class CarRentalPage {

    JFrame frame;
    JPanel panel, panel1;
    JLabel makeLb, typeLb, rateLb, carIdLb,nameLb,rentLB,lengthLb,rdLb,feesLb;
    JTextField carIdJt, nameJt,feesJt;
    JFormattedTextField rentJf, retDate;
    JComboBox makeJc,typeJc, rateJc, lengthJc;
    JButton searchBtn, clrBtn, rentBtn,cancelBtn;
    JTable table;
    JScrollPane scrollPane;

    CarRentalPage(){
        frame = new JFrame("Car Rental");
        frame.setSize(1100,600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(null);
        frame.setLocationRelativeTo(null);

        makeLb = new JLabel("Car Make");
        makeLb.setBounds(200,35,125,35);

        typeLb = new JLabel("Car Type");
        typeLb.setBounds(400,35,125,35);

        carIdLb = new JLabel("Car details");
        carIdLb.setBounds(750,85,125,35);

        nameLb = new JLabel("FullName");
        nameLb.setBounds(750,135,125,35);

        rentLB = new JLabel("Rent Date");
        rentLB.setBounds(750,185,125,35);

        lengthLb = new JLabel("Duration of rent");
        lengthLb.setBounds(750,285,125,35);

        rateLb = new JLabel("Rates");
        rateLb.setBounds(750,235,125,35);

        rdLb = new JLabel("Return Date");
        rdLb.setBounds(750,335,125,35);

        feesLb = new JLabel("Total Amount");
        feesLb.setBounds(750,385,125,35);


        updateMake();
        makeJc.setEnabled(true);
        makeJc.setBounds(200,80,150,35);
        makeJc.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                String make = (String) makeJc.getSelectedItem();
                updateType(make);
            }
        });

        typeJc = new JComboBox<>();
        typeJc.addItem("Choose Type");
        typeJc.setEnabled(true);
        typeJc.setBounds(400,80,150,35);


        updateRate();
        rateJc.setEnabled(true);
        rateJc.setBounds(885,235,150,35);
        rateJc.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                String rate = (String) rateJc.getSelectedItem();

                lengthJc.removeAllItems();

                if (rate.equals("Daily")) {
                    for (int i = 1; i <= 12; i++) {
                        lengthJc.addItem(String.valueOf(i));
                    }
                } else if (rate.equals("Weekly")) {
                    for (int i = 1; i <= 6; i++) {
                        lengthJc.addItem(String.valueOf(i));
                    }
                } else {
                    for (int i = 1; i <= 3; i++) {
                        lengthJc.addItem(String.valueOf(i));
                    }
                }

            }
        });


        searchBtn = new JButton("Search");
        searchBtn.setFocusable(false);
        searchBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                searchResult();
            }
        });

        clrBtn = new JButton("Clear");
        clrBtn.setFocusable(false);
        clrBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                makeJc.setSelectedItem(null);
                typeJc.setSelectedItem(null);
            }
        });

        rentBtn = new JButton("Rent");
        rentBtn.setFocusable(false);
        rentBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                carRent();
            }
        });

        cancelBtn = new JButton("Cancel");
        cancelBtn.setFocusable(false);
        cancelBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.dispose();
                MainMenu mm=new MainMenu();
            }
        });

        panel = new JPanel();
        panel.setBounds(275,150,220,35);
        panel.setLayout(new GridLayout(1,2,10,10));
        panel.add(searchBtn);
        panel.add(clrBtn);


        table = new JTable();
        table.setBounds(50,200,650,275);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        table.setFillsViewportHeight(true);
        table.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    int selectedRow = table.getSelectedRow();
                    if (selectedRow != -1) {
                        // Get the values from the selected row
                        String carId = table.getValueAt(selectedRow, 0).toString();

                        // Update the non-editable text fields
                        carIdJt.setText(carId);
                    }
                }
                display();

            }
        });


        scrollPane = new JScrollPane(table);
        scrollPane.setBounds(50,200,650,275);

        panel1 = new JPanel();
        panel1.setBounds(790,435,240,35);
        panel1.setLayout(new GridLayout(1,2,10,10));
        panel1.add(rentBtn);
        panel1.add(cancelBtn);

        carIdJt = new JTextField();
        carIdJt.setEditable(false);
        carIdJt.setBounds(885,85,150,35);

        nameJt = new JTextField();
        nameJt.setEditable(false);
        nameJt.setBounds(885,135,150,35);

        rentJf = new JFormattedTextField(DateFormat.getDateInstance(DateFormat.SHORT));
        rentJf.setValue(new Date());
        rentJf.setEditable(false);
        rentJf.setBounds(885,185,150,35);

        lengthJc = new JComboBox<>();
        lengthJc.addItem("Choose Duration");
        lengthJc.setEnabled(true);
        lengthJc.setBounds(885,285,150,35);
        lengthJc.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                dateCalc();
                priceCalc();
            }
        });

        retDate = new JFormattedTextField(DateFormat.getDateInstance(DateFormat.SHORT));
        retDate.setEditable(false);
        retDate.setBounds(885,335,150,35);

        feesJt = new JTextField();
        feesJt.setBounds(885,385,150,35);
        feesJt.setEditable(false);


        frame.add(makeLb);
        frame.add(typeLb);
        frame.add(rateLb);
        frame.add(carIdLb);
        frame.add(nameLb);
        frame.add(rentLB);
        frame.add(lengthLb);
        frame.add(rdLb);
        frame.add(feesLb);
        frame.add(makeJc);
        frame.add(typeJc);
        frame.add(rateJc);
        frame.add(carIdJt);
        frame.add(nameJt);
        frame.add(rentJf);
        frame.add(lengthJc);
        frame.add(retDate);
        frame.add(feesJt);
        frame.add(panel);
        frame.add(table);
        frame.add(panel1);
        frame.setVisible(true);

    }

    private void updateMake(){
        String sql = "Select * From carMake";
        try{
            String [] make = new String[11];
            make[0]="Choose Make";
            Connection conn = DbConnection.createDBConnection();
            PreparedStatement pst = conn.prepareStatement(sql);
            ResultSet rst = pst.executeQuery();
            int i = 1;
            while(rst.next()){
                make[i]=rst.getString("make");
                i++;
            }
            makeJc=new JComboBox<>(make);
        }catch(Exception e){
            e.printStackTrace();
        }

    }

    private void updateType(String make){
        String sql = "Select carType.type From cars Join carType On cars.typeId= carType.typeId Join carMake On cars.makeId= carMake.makeId Where carMake.make=?";
        typeJc.removeAllItems();
        try {
            String[] type = new String[11];
            Connection conn = DbConnection.createDBConnection();
            Statement stmt = conn.createStatement();
            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setString(1, make);
            ResultSet rst = pst.executeQuery();
            int i = 1;
            while (rst.next()) {
                typeJc.addItem(rst.getString("type"));
                i++;
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    private void updateRate(){
        String sql = "Select * From rates";
        try{
            String [] rate = new String[11];
            rate[0]="Choose rent plan";
            Connection conn = DbConnection.createDBConnection();
            PreparedStatement pst = conn.prepareStatement(sql);
            ResultSet rst = pst.executeQuery();
            int i = 1;
            while(rst.next()){
                rate[i]=rst.getString("rate");
                i++;
            }
            rateJc=new JComboBox<>(rate);
        }catch(Exception e){
            e.printStackTrace();
        }

    }


    public void searchResult(){

        String make = (String) makeJc.getSelectedItem();
        String type =(String) typeJc.getSelectedItem();


        try{

            String sql = "SELECT cars.carId,carMake.make, carType.type, cars.CarName " +
                    "FROM cars " +
                    "JOIN carType ON cars.typeId = carType.typeId " +
                    "JOIN carMake ON cars.makeId = carMake.makeId " +
                    "WHERE status='Available' AND make=? AND type=?";

            Connection conn = DbConnection.createDBConnection();
            Statement stmt = conn.createStatement();
            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setString(1,make);
            pst.setString(2,type);

            ResultSet resultSet = pst.executeQuery();
            ResultSetMetaData rsmd = resultSet.getMetaData();
            DefaultTableModel model = (DefaultTableModel) table.getModel();

            int cols = rsmd.getColumnCount();
            String[] colName = new String[cols];
            for (int i = 0; i < cols; i++){
                colName[i] = rsmd.getColumnName(i + 1);
            }
            model.setColumnIdentifiers(colName);

            String carId,carMake,carType,carName;
            while (resultSet.next()){
                carId = resultSet.getString("carId");
                carMake = resultSet.getString("make");
                carType = resultSet.getString("type");
                carName = resultSet.getString("carName");
                String[]row = {carId,carMake,carType,carName};
                model.addRow(row);
            }
            stmt.close();
            conn.close();



        }catch(Exception e){
            e.printStackTrace();
        }

    }
    public void display() {
        User user = LoginService.getLoggedInUser();

        if (user != null) {
            String username = user.getFullName();

            // Retrieve other user information as needed

            // Use the user information in the page
            nameJt.setText(username);
            // Perform other actions or display the user information in the desired way

        } else {
            // User is not logged in, handle the situation accordingly (e.g., redirect to login page)
        }
    }

    private void dateCalc() {
        String rate = (String) rateJc.getSelectedItem();
        String length = (String) lengthJc.getSelectedItem();
        if (length != null) {
            int duration = Integer.parseInt(length);

            Date rentDate = (Date) rentJf.getValue();
            Calendar c = Calendar.getInstance();
            c.setTime(rentDate);

            if (rate.equals("Daily")) {
                c.add(Calendar.DAY_OF_MONTH, duration);
            } else if (rate.equals("Weekly")) {
                c.add(Calendar.WEEK_OF_YEAR, duration);
            } else {
                c.add(Calendar.MONTH, duration);
            }

            retDate.setValue(c.getTime());
        }
    }


    private void priceCalc() {
        String rate = (String) rateJc.getSelectedItem();
        String length = (String) lengthJc.getSelectedItem();
        String carId = carIdJt.getText();
        if (length != null) {
            int num = Integer.parseInt(length);

            try{
                String sql ="Select fees.fees from fees JOIN rates ON fees.rateId = rates.rateId JOIN cars ON fees.typeId = cars.typeId WHERE rates.rate=? AND cars.carId=?" ;

                Connection conn = DbConnection.createDBConnection();
                Statement stmt = conn.createStatement();
                PreparedStatement pst = conn.prepareStatement(sql);
                pst.setString(1,rate);
                pst.setString(2,carId);
                ResultSet resultSet = pst.executeQuery();
                if (resultSet.next()){
                    String fees=resultSet.getString("fees");
                    if(fees !=null){
                        int price = Integer.parseInt(fees);

                        int totalCost = num * price;

                        feesJt.setText(String.valueOf(totalCost));

                    }
                }

            }catch(Exception e){
                e.printStackTrace();
            }

        }
    }

    public void carRent(){

        String carId = carIdJt.getText();
        String name = nameJt.getText();
        Date rentDate = (Date) rentJf.getValue();
        Date returnDate = (Date) retDate.getValue();
        DateFormat formatter = new SimpleDateFormat("yyyy/MM/dd");


        String fees = feesJt.getText();

        try{
            String rDate = formatter.format(rentDate);
            String rtDate = formatter.format(returnDate);
            Connection conn = DbConnection.createDBConnection();

            Statement stmt = conn.createStatement();
            String sql = "INSERT INTO rentaltable(carId,FullName,RentDate,ReturnDate,Fees)"+
                    "VALUES(?,?,?,?,?)";
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setString(1,carId);
            preparedStatement.setString(2,name);
            preparedStatement.setString(3,rDate);
            preparedStatement.setString(4,rtDate);
            preparedStatement.setString(5,fees);
            int rows = preparedStatement.executeUpdate();

            if (rows >0) {
                JOptionPane.showMessageDialog(null,
                        "Successful Car Rent",
                        "Rent successful",
                        JOptionPane.PLAIN_MESSAGE);
            }

            String stSql = "UPDATE cars SET status = 'BOOKED' WHERE carId = ?;";
            PreparedStatement pst = conn.prepareStatement(stSql);
            pst.setString(1,carId);
            pst.executeUpdate();

            stmt.close();
            conn.close();

        } catch(Exception e){
            e.printStackTrace();
        }

    }

}

