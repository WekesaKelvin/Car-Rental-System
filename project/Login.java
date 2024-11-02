package project;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Login {

    JFrame frame;
    JLabel l1, l2, l3;
    JTextField tf1;
    JButton btn1, btn2;
    JPasswordField p1;

    Login() {

        frame = new JFrame();
        frame.setSize(600, 500);
        frame.setLayout(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setTitle("Login Page");
        frame.setLocationRelativeTo(null);


        l1 = new JLabel("Login Page");
        l1.setForeground(Color.blue);
        l1.setFont(new Font("Serif", Font.BOLD, 20));
        l2 = new JLabel("Email:");
        l3 = new JLabel("Password:");
        tf1 = new JTextField();
        p1 = new JPasswordField();
        btn1 = new JButton("Login");
        btn2 = new JButton("Register");

        //deciding location for the components since we have no layout
        l1.setBounds(300, 110, 400, 30);
        l2.setBounds(80, 160, 200, 30);
        l3.setBounds(80, 210, 200, 30);
        tf1.setBounds(250, 160, 200, 30);
        p1.setBounds(250, 210, 200, 30);
        btn1.setBounds(250, 270, 80, 30);
        btn2.setBounds(350, 270, 90, 30);

        //add to container
        frame.add(l1);
        frame.add(l2);
        frame.add(l3);
        frame.add(tf1);
        frame.add(p1);
        frame.add(btn1);
        frame.add(btn2);

        //actions
        btn2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SignUp sp = new SignUp();
            }
        });
        btn1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleLogin();
            }
        });

        frame.setVisible(true);

    }

    private void handleLogin() {
        String email = tf1.getText();
        String password = String.valueOf(p1.getPassword());

        User user = LoginService.login(email, password);

        if (user != null) {
            frame.dispose();
            PageController pageController = new PageController();
            pageController.displayPage(user);
        } else {
            JOptionPane.showMessageDialog(null,
                    "Email or Password Invalid",
                    "Try Again",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

}



