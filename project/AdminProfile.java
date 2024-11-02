package project;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class AdminProfile{

    private JFrame frame ;
    private JLabel titleLabel,fullNameLabel,phoneNumberLabel,emailLabel,addressLabel,dobLabel,nationalIdLabel;
    private JTextField userIdTf,fullNameTf,phoneNumberTf,emailTf,addressTf,dobTf,nationalIdTf;
    private JButton editButton,deleteButton,BackButton;
    private JPanel panel;
    private User user;


    public AdminProfile() {


        frame = new JFrame("User Profile");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500, 420);
        frame.setLayout(null);
        frame.setLocationRelativeTo(null);

        titleLabel = new JLabel("User Profile");
        titleLabel.setFont(new Font("Serif", Font.BOLD, 20));
        titleLabel.setBounds(200, 20, 200, 30);

        fullNameLabel = new JLabel("Full Name: " );
        fullNameLabel.setBounds(50, 110, 150, 35);

        phoneNumberLabel = new JLabel("Phone Number: " );
        phoneNumberLabel.setBounds(50, 150, 150, 35);

        emailLabel = new JLabel("Email: ");
        emailLabel.setBounds(50, 190, 150, 35);

        addressLabel = new JLabel("Address: ");
        addressLabel.setBounds(50, 230, 150, 35);


        editButton = new JButton("Edit");
        deleteButton = new JButton("Delete");
        BackButton = new JButton("Back");

        userIdTf = new JTextField();
        userIdTf.setEditable(false);
        userIdTf.setBounds(175,70,150,35);

        fullNameTf = new JTextField();
        fullNameTf.setBounds(175,110,150,35);

        phoneNumberTf = new JTextField();
        phoneNumberTf.setBounds(175,150,150,35);

        emailTf = new JTextField();
        emailTf.setBounds(175,190,150,35);

        addressTf = new JTextField();
        addressTf.setBounds(175,230,150,35);

        panel = new JPanel();
        panel.setBounds(70,280,300,30);
        panel.setLayout(new GridLayout(1,3,10,10));
        panel.add(BackButton);
        panel.add(editButton);
        panel.add(deleteButton);


        frame.add(titleLabel);
        frame.add(fullNameLabel);
        frame.add(phoneNumberLabel);
        frame.add(emailLabel);
        frame.add(addressLabel);
        frame.add(fullNameTf);
        frame.add(userIdTf);
        frame.add(phoneNumberTf);
        frame.add(emailTf);
        frame.add(addressTf);
        frame.add(panel);


        editButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                editUser();
            }
        });


        BackButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                AdminMainMenu mm = new AdminMainMenu();
                frame.dispose();
            }
        });

        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int choice = JOptionPane.showConfirmDialog(frame, "Are you sure you want to delete your account?",
                        "Delete Account", JOptionPane.YES_NO_OPTION);
                if (choice == JOptionPane.YES_OPTION) {
                    deleteUser();
                }
            }
        });

        frame.setVisible(true);
        user = LoginService.getLoggedInUser();
        userDisplay();
    }

    private void editUser() {

        int option = JOptionPane.showConfirmDialog(frame, "Edit User Information");
        if (option == JOptionPane.OK_OPTION) {
            String userId = userIdTf.getText();
            String fullName = fullNameTf.getText();
            String phoneNumber = phoneNumberTf.getText();
            String email = emailTf.getText();
            String address = addressTf.getText();


            try {
                Connection conn = DbConnection.createDBConnection();
                PreparedStatement statement = conn.prepareStatement("UPDATE users SET FullName = ?, PhoneNumber = ?, Email = ?, Address = ? WHERE userId = ?");
                statement.setString(1, fullName);
                statement.setString(2, phoneNumber);
                statement.setString(3, email);
                statement.setString(4, address);
                statement.setInt(5, Integer.parseInt(userId));
                statement.executeUpdate();
                statement.close();

                user.setFullName(fullName);
                user.setPhoneNumber(phoneNumber);
                user.setEmailAddress(email);
                user.setAddress(address);

                JOptionPane.showMessageDialog(frame, "User information updated successfully.");

                frame.dispose();
                CarRentalPage crp = new CarRentalPage();


            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(frame, "Error updating user information.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }



    private void deleteUser() {
        try {
            String userId = userIdTf.getText();
            Connection conn = DbConnection.createDBConnection();
            PreparedStatement statement = conn.prepareStatement("DELETE FROM users WHERE userId = ?");
            statement.setInt(1, Integer.parseInt(userId));
            statement.executeUpdate();
            statement.close();

            JOptionPane.showMessageDialog(frame, "Account deleted successfully.");
            frame.dispose();
            StartUpPage sup = new StartUpPage();
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(frame, "Error deleting account.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void userDisplay(){


        if (user != null) {
            String fullName = user.getFullName();
            String phoneNumber = user.getPhoneNumber();
            String email = user.getEmailAddress();
            String address = user.getAddress();
            String userId = String.valueOf(user.getUserId());

            if(userId != null) {
                userIdTf.setText(userId);
            }
            fullNameTf.setText(fullName);
            phoneNumberTf.setText(phoneNumber);
            emailTf.setText(email);
            addressTf.setText(address);

        } else {
            // User is not logged in, handle the situation accordingly (e.g., redirect to login page)
        }

    }

}
