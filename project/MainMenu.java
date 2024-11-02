package project;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainMenu {
    JFrame frame;
    JPanel panel;
    JButton poBtn,orBtn,favBtn,traBtn;

    MainMenu(){
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
                CarRentalPage crp = new CarRentalPage();
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

        favBtn = new JButton("Profile");
        favBtn.setFocusable(false);
        favBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.dispose();
                UserProfile up = new UserProfile();
            }
        });

        traBtn = new JButton("Return");
        traBtn.setFocusable(false);
        traBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ReturnPage rp = new ReturnPage();
            }
        });

        panel = new JPanel();
        panel.setBounds(10,20,430,50);
        panel.setLayout(new FlowLayout());
        panel.add(poBtn);
        panel.add(orBtn);
        panel.add(favBtn);
        panel.add(traBtn);

        frame.add(panel);
        frame.setVisible(true);

    }
}
