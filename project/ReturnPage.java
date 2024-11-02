package project;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ReturnPage{
    private JFrame frame;
    private JComboBox rentId;
    private JTextField carNameTf;
    private JLabel rIdLb,rdLb,cnLb,tdLb;
    private JFormattedTextField todayFt,returnDateField;
    private JButton returnButton,backBtn;
    private JPanel panel;
    private String selectedRentId;

    public ReturnPage() {
        // Set up the frame
        frame = new JFrame();
        frame.setTitle("Car Return");
        frame.setSize(450, 300);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setLayout(null);


        // Add components to the main panel
        rIdLb = new JLabel("Rental ID:");
        rIdLb.setBounds(50,50,150,30);

        getInfo();
        rentId.setBounds(200,50,150,30);
        rentId.setEnabled(true);
        rentId.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    selectedRentId = (String) rentId.getSelectedItem();
                }
                updateRent(selectedRentId);
            }

        });

        cnLb = new JLabel("Car Name");
        cnLb.setBounds(50,90,150,30);

        carNameTf = new JTextField();
        carNameTf.setBounds(200,90,150,30);
        carNameTf.setEditable(false);

        tdLb = new JLabel("Today's Date");
        tdLb.setBounds(50,130,150,30);

        todayFt = new JFormattedTextField(DateFormat.getDateInstance(DateFormat.SHORT));
        todayFt.setValue(new Date());
        todayFt.setBounds(200,130,150,30);
        todayFt.setEditable(false);

        rdLb = new JLabel("Return Date:");
        rdLb.setBounds(50,170,150,30);

        returnDateField = new JFormattedTextField(DateFormat.getDateInstance(DateFormat.SHORT));
        returnDateField.setBounds(200,170,150,30);
        returnDateField.setEditable(false);

        returnButton = new JButton("Return Car");
        returnButton.setFocusable(false);
        returnButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                returnCar();

            }
        });

        backBtn = new JButton("Back");
        backBtn.setFocusable(false);
        backBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.dispose();
               MainMenu mm = new MainMenu();
            }
        });

        panel = new JPanel();
        panel.setBounds(100,220,250,30);
        panel.setLayout(new GridLayout(1,2,10,10));
        panel.add(returnButton);
        panel.add(backBtn);

        frame.add(rdLb);
        frame.add(rIdLb);
        frame.add(carNameTf);
        frame.add(cnLb);
        frame.add(returnDateField);
        frame.add(rentId);
        frame.add(todayFt);
        frame.add(tdLb);
        frame.add(panel);
        frame.setVisible(true);



    }

    private void getInfo(){
        User user = LoginService.getLoggedInUser();

        if(user !=null){
            String userId = String.valueOf(user.getUserId());

            try{
                String sql = "Select rentalTable.rentId FROM rentalTable Join cars On rentalTable.carId = cars.carId " +
                        "Join users On rentaltable.Fullname = users.FullName where userId=?";

                Connection conn = DbConnection.createDBConnection();
                Statement stmt = conn.createStatement();
                PreparedStatement pst = conn.prepareStatement(sql);
                pst.setString(1,userId);
                ResultSet resultSet = pst.executeQuery();
                String [] rate = new String[11];
                rate[0]="Choose rentId";
                int i = 1;
                while(resultSet.next()){
                    rate[i]=resultSet.getString("rentId");
                    i++;
                }
                rentId=new JComboBox<>(rate);

            }catch(Exception e){
                e.printStackTrace();
            }

        }



    }

    private void returnCar() {
        try {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

            // Remove the time component from today's date
            Date todayDate = formatter.parse(formatter.format((Date) todayFt.getValue()));

            // Remove the time component from the return date
            Date returnDate = formatter.parse(formatter.format((Date) returnDateField.getValue()));


            // Compare the return date with today's date
            if (todayDate.compareTo(returnDate) > 0) {
                long diffInMillies = Math.abs(returnDate.getTime() - todayDate.getTime());
                long diffInDays = diffInMillies / (24 * 60 * 60 * 1000);

                int fineAmount = (int) (diffInDays * 50); // Assuming a fine of $50 per day

                // Display a dialog with the fine information
                JOptionPane.showMessageDialog(frame, "Fine: $" + fineAmount);
                updateReturn( fineAmount);
            } else {
                // No fine
                int fineAmount = 0;
                JOptionPane.showMessageDialog(frame, "No fine. Thank you for returning the car on time.");
                updateReturn(fineAmount);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void updateRent( String Id){
        String sql = "Select rentalTable.returnDate, cars.carName FROM rentalTable Join cars On rentalTable.carId = cars.carId " +
                "where rentId=?";

        try{
            Connection conn = DbConnection.createDBConnection();
            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setString(1,Id);
            ResultSet rst = pst.executeQuery();
            if (rst.next()){
                String returnDateStr = rst.getString("returnDate");
                String carName = rst.getString("carName");

                carNameTf.setText(carName);

                // Adjust the date format to match the database return date format
                SimpleDateFormat dbDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                Date returnDate = dbDateFormat.parse(returnDateStr);
                returnDateField.setValue(returnDate);

            }


        }catch(Exception e){
            e.printStackTrace();
        }

    }

    private void  updateReturn(int fine){
        String rentId = selectedRentId;
        String carName = carNameTf.getText();

        Date returnDate = (Date) returnDateField.getValue();
        DateFormat formatter = new SimpleDateFormat("yyyy/MM/dd");



        try{
            String rtDate = formatter.format(returnDate);
            Connection conn = DbConnection.createDBConnection();

            Statement stmt = conn.createStatement();
            String carsSql = "Select * from cars where carName=?";
            PreparedStatement pst = conn.prepareStatement(carsSql);
            pst.setString(1,carName);
            ResultSet resultSet = pst.executeQuery();

            if (resultSet.next()) {
                String carId = resultSet.getString("carId");

                String sql = "INSERT INTO returnTable(rentId,carId,ReturnDate,Fine)" +
                        "VALUES(?,?,?,?)";
                PreparedStatement preparedStatement = conn.prepareStatement(sql);
                preparedStatement.setString(1, rentId);
                preparedStatement.setString(2, carId);
                preparedStatement.setString(3, rtDate);
                preparedStatement.setString(4, String.valueOf(fine));
                int rows = preparedStatement.executeUpdate();

                if (rows > 0) {
                    JOptionPane.showMessageDialog(null,
                            "Successful Car Rent",
                            "Rent successful",
                            JOptionPane.PLAIN_MESSAGE);
                }

                String stSql = "UPDATE cars SET status = 'Available' WHERE carId = ?;";
                PreparedStatement ps = conn.prepareStatement(stSql);
                ps.setString(1,carId);
                ps.executeUpdate();




            }
            stmt.close();
            conn.close();

        } catch(Exception e){
            e.printStackTrace();
        }
    }

}
