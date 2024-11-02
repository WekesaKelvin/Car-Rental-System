package project;


import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AdminMainMenu {
    JFrame frame;
    JPanel panel;
    JButton poBtn,orBtn,favBtn,traBtn,pBtn;

    AdminMainMenu(){
        frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(450, 450);
        frame.setLayout(null);
        frame.setLocationRelativeTo(null);

        poBtn = new JButton("Car Rental Page");
        poBtn.setFocusable(false);
        poBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.dispose();
                AdminRentalPage arp = new AdminRentalPage();
            }
        });

        orBtn = new JButton("Cars");
        orBtn.setFocusable(false);
        orBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Code to handle order functionality goes here,anyone in the group can do it
            }
        });

        favBtn = new JButton("Customers");
        favBtn.setFocusable(false);
        favBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.dispose();
                Customers cus = new Customers();
            }
        });

        traBtn = new JButton("Return");
        traBtn.setFocusable(false);
        traBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.dispose();
                AdminReturnPage arp = new AdminReturnPage();
            }
        });
        pBtn = new JButton("profile");
        pBtn.setFocusable(false);
        pBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.dispose();
                AdminProfile pro = new AdminProfile();
            }
        });

        panel = new JPanel();
        panel.setBounds(10,20,430,70);
        panel.setLayout(new FlowLayout());
        panel.add(poBtn);
        panel.add(orBtn);
        panel.add(favBtn);
        panel.add(traBtn);
        panel.add(pBtn);

        frame.add(panel);
        frame.setVisible(true);

    }
}

