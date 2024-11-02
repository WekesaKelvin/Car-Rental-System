package project;


import com.sun.tools.javac.Main;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SignUp {

    JFrame frame;
    JPanel panel;
    JTextField tfName,tfEmail,tfAddress,tfPhone,tfNatId,tfDob;
    JPasswordField pfPassword,pfConfirm;
    JLabel jlName,jlEmail,jlAddress,jlPhone,jlPassword,jlConfirm,jlDOB,jlNatId;
    JButton btnOk,btnCancel,btnClear;
    Font myFont = new Font("Arial",Font.PLAIN,16);

    SignUp(){
        frame = new JFrame("Sign Up");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(650,650);
        frame.setLayout(null);
        frame.setLocationRelativeTo(null);

        jlName = new JLabel("FullName");
        jlName.setBounds(50,50,150,25);
        jlName.setFont(myFont);

        jlPhone = new JLabel("Phone Number");
        jlPhone.setBounds(50,100,150,25);
        jlPhone.setFont(myFont);

        jlEmail = new JLabel("Email Address");
        jlEmail.setBounds(50,150,150,25);
        jlEmail.setFont(myFont);

        jlAddress = new JLabel("Address");
        jlAddress.setBounds(50,200,150,25);
        jlAddress.setFont(myFont);

        jlDOB = new JLabel("Date Of Birth");
        jlDOB.setBounds(50,250,150,25);
        jlDOB.setFont(myFont);

        jlNatId = new JLabel("National ID");
        jlNatId.setBounds(50,300,150,25);
        jlNatId.setFont(myFont);

        jlPassword = new JLabel("Password");
        jlPassword.setBounds(50,350,150,25);
        jlPassword.setFont(myFont);

        jlConfirm = new JLabel("Confirm Password");
        jlConfirm.setBounds(50,400,150,25);
        jlConfirm.setFont(myFont);

        tfName = new JTextField();
        tfName.setBounds(200,50,150,25);
        tfName.setFont(myFont);

        tfPhone = new JTextField();
        tfPhone.setBounds(200,100,150,25);
        tfPhone.setFont(myFont);

        tfEmail = new JTextField();
        tfEmail.setBounds(200,150,150,25);
        tfEmail.setFont(myFont);

        tfAddress = new JTextField();
        tfAddress.setBounds(200,200,150,25);
        tfAddress.setFont(myFont);

        tfDob = new JTextField("yyyy-mm-dd");
        tfDob.setBounds(200,250,150,25);
        tfDob.setFont(myFont);

        tfNatId = new JTextField();
        tfNatId.setBounds(200,300,150,25);
        tfNatId.setFont(myFont);

        pfPassword = new JPasswordField();
        pfPassword.setBounds(200,350,150,25);

        pfConfirm = new JPasswordField();
        pfConfirm.setBounds(200,400,150,25);

        btnOk = new JButton("Ok");
        btnOk.setFont(myFont);
        btnOk.setFocusable(false);
        btnOk.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                registerUser();
                Login login = new Login();
            }
        });

        btnCancel = new JButton("Cancel");
        btnCancel.setFont(myFont);
        btnCancel.setFocusable(false);
        btnCancel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.dispose();

            }
        });

        btnClear = new JButton("Clear");
        btnClear.setFont(myFont);
        btnClear.setFocusable(false);
        btnClear.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                tfName.setText("");
                tfEmail.setText("");
                tfAddress.setText("");
                tfPhone.setText("");
                tfDob.setText("");
                tfNatId.setText("");
                pfPassword.setText("");
                pfConfirm.setText("");
            }
        });



        panel = new JPanel();
        panel.setBounds(75,450,300,25);
        panel.setLayout(new GridLayout(1,2,10,10));
        panel.add(btnOk);
        panel.add(btnClear);
        panel.add(btnCancel);

        frame.add(jlName);
        frame.add(jlEmail);
        frame.add(jlAddress);
        frame.add(jlPhone);
        frame.add(jlDOB);
        frame.add(jlNatId);
        frame.add(jlPassword);
        frame.add(jlConfirm);
        frame.add(tfName);
        frame.add(tfAddress);
        frame.add(tfEmail);
        frame.add(tfPhone);
        frame.add(tfDob);
        frame.add(tfNatId);
        frame.add(pfPassword);
        frame.add(pfConfirm);
        frame.add(panel);
        frame.setVisible(true);
    }

    private void registerUser() {
        String fullName = tfName.getText();
        String email = tfEmail.getText();
        String address = tfAddress.getText();
        String phone = tfPhone.getText();
        String dob = getDate(tfDob.getText());
        String natId = tfNatId.getText();
        String password = String.valueOf(pfPassword.getPassword());
        String confirm = String.valueOf(pfConfirm.getPassword());

        if (fullName.isEmpty() || email.isEmpty() || address.isEmpty() || phone.isEmpty() ||dob.isEmpty() ||natId.isEmpty() || password.isEmpty() || confirm.isEmpty()){
            JOptionPane.showMessageDialog(null,
                    "Please Enter All Fields",
                    "Try Again",
                    JOptionPane.ERROR_MESSAGE);
        }
        if (!password.equals(confirm)){
            JOptionPane.showMessageDialog(null,
                    "Please Rewrite The Confirm Password",
                    "Invalid Password",
                    JOptionPane.ERROR_MESSAGE);
        }

        user = addUserToDatabase(fullName, email,address,phone,password,confirm,dob,natId);

    }

    public User user;

    private User addUserToDatabase(String fullName, String email, String address, String phone, String password, String confirm, String dob, String natId) {
        User user = null;

        try {
            Connection conn = DbConnection.createDBConnection();

            Statement stmt = conn.createStatement();
            String sql = "INSERT INTO users(FullName,PhoneNumber,Email,Address,DOB,NationalId,password)"+
                    "VALUES(?,?,?,?,?,?,?)";
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setString(1,fullName);
            preparedStatement.setString(2,phone);
            preparedStatement.setString(3,email);
            preparedStatement.setString(4,address);
            preparedStatement.setString(5, String.valueOf(dob));
            preparedStatement.setString(6,natId);
            preparedStatement.setString(7,password);


            int addedRows = preparedStatement.executeUpdate();
            if (addedRows>0){
                user = new User();
                user.fullName = fullName;
                user.emailAddress = email;
                user.address = address;
                user.phoneNumber = phone;
                user.password = password;
                user.dob = dob;
                user.natId = natId;

                JOptionPane.showMessageDialog(null,
                            "Successful Registration",
                            "Sign UP",
                            JOptionPane.PLAIN_MESSAGE);


            }


            stmt.close();
            conn.close();

        } catch(SQLException ex){
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE,null,ex);
        } catch (Exception e){
            e.printStackTrace();
        }

        return user;

    }

    public String getDate(String db){
        LocalDate date = LocalDate.parse(db, DateTimeFormatter.ISO_DATE);
        String dob = String.valueOf(date);
        return dob;
    }


}


