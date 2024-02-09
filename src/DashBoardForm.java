import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class DashBoardForm extends JFrame{
    private JPanel DashBoardPanel;
    private JLabel lbAdmin;
    private JButton registerButton;

    public DashBoardForm(){
        setTitle("Dashboard");
        setContentPane(DashBoardPanel);
        setMinimumSize(new Dimension(500, 429));
        setSize(1200,700);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        boolean hasRegisteredUsers = connectToDatabase();
        if (hasRegisteredUsers){
            LoginForm loginForm = new LoginForm(this);
            User user = loginForm.user;

            if (user != null){
                lbAdmin.setText("User: " + user.name);
                setLocationRelativeTo(null);
                setVisible(true);
            }
            else {
                dispose();
            }
        }
        else {
            RegFrom regFrom = new RegFrom(this);
            User user = regFrom.user;

            if (user != null){
                lbAdmin.setText("User: " + user.name);
                setLocationRelativeTo(null);
                setVisible(true);
            }
            else {
                dispose();
            }
        }

        //method is executed when the register button is clicked
        registerButton.addActionListener(new ActionListener() {

            //display registration form when the button is pressed
            @Override
            public void actionPerformed(ActionEvent e) {
                RegFrom regFrom = new RegFrom(DashBoardForm.this);
                User user = regFrom.user;

                if (user != null){
                    JOptionPane.showMessageDialog(DashBoardForm.this,
                            "New user: " + user.name,
                            "Successful Registration",
                            JOptionPane.INFORMATION_MESSAGE);
                }
            }
        });
    }

    private boolean connectToDatabase(){
        boolean hasRegisteredUsers = false;

        final String MYSQL_SERVER_URL = "jdbc:mysql://127.0.0.1:3306/";
        final String DB_URL = "jdbc:mysql://127.0.0.1:3306/user_schema";
        final String USERNAME = "root";
        final String PASSWORD = "Mysql123!";

        try {
            // connect to the mysql server and create the database if not created
            Connection connection = DriverManager.getConnection(MYSQL_SERVER_URL, USERNAME, PASSWORD);
            Statement stmnt = connection.createStatement();
            stmnt.executeUpdate("CREATE DATABASE IF NOT EXISTS MyStore");
            stmnt.close();
            connection.close();

            // CREATE THE TABLE IF IT DOES NOT EXIST
            connection = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);
            stmnt = connection.createStatement();
            String sql = "CREATE TABLE IF NOT EXISTS users ("
                    + "id INT( 10 ) NOT NULL PRIMARY KEY AUTO_INCREMENT,"
                    + "name VARCHAR(200) NOT NULL,"
                    + "email VARCHAR(200) NOT NULL UNIQUE,"
                    + "phone VARCHAR(200),"
                    + "address VARCHAR(200),"
                    + "password VARCHAR(200) NOT NULL"
                    + ")";
            stmnt.executeUpdate(sql);

            stmnt = connection.createStatement();
            ResultSet rs = stmnt.executeQuery("SELECT COUNT(*) FROM users");

            if (rs.next()){
                int numUsers = rs.getInt(1);
                if (numUsers > 0){
                    hasRegisteredUsers = true;
                }
            }
            stmnt.close();
            connection.close();
        }catch (Exception e){
            e.printStackTrace();
        }


        return hasRegisteredUsers;

    }
}
