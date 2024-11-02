package project;

import com.sun.tools.javac.Main;

import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class LoginService { private static User loggedInUser;

    public static User login(String email, String password) {
        User user = getAuthenticatedUser(email, password);

        if (user != null) {
            loggedInUser = user;
            return loggedInUser;
        }

        return null;
    }

    private static User getAuthenticatedUser(String email, String password) {

        try {
            Connection conn = DbConnection.createDBConnection();

            Statement stmt = conn.createStatement();
            String sql = "SELECT * FROM users WHERE Email=? AND password=?";
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setString(1,email);
            preparedStatement.setString(2,password);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()){
                User user = new User();
                user.setUserId(resultSet.getInt("userId"));
                user.setFullName(resultSet.getString("FullName"));
                user.setEmailAddress(resultSet.getString("Email"));
                user.setAddress(resultSet.getString("Address"));
                user.setPhoneNumber(resultSet.getString("PhoneNumber"));
                user.setDob(resultSet.getString("DOB"));
                user.setNatId(resultSet.getString("NationalId"));
                user.setRole(UserRole.valueOf(resultSet.getString("Role")));

                return user;
            }

            stmt.close();
            conn.close();



        }catch (SQLException ex){
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE,null,ex);
        }catch (Exception e){
            e.printStackTrace();
        }

        return null;
    }


    public static User getLoggedInUser() {
        return loggedInUser;
    }
}


