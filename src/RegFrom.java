import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.Statement;

public class RegFrom extends JDialog{
    private JTextField tfName;
    private JTextField tfEmail;
    private JTextField tfPhone;
    private JTextField tfAddress;
    private JPasswordField pfPassword;
    private JPasswordField pfConfirmPassword;
    private JButton btnRegister;
    private JButton btnCancel;
    private JPanel regPanel;
    public User user;

    public RegFrom(JFrame owner) {
        super(owner);
        setTitle("Create a new account");
        setContentPane(regPanel);
        setMinimumSize(new Dimension(450,474));
        setModal(true);
        setLocationRelativeTo(owner);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        btnRegister.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                registerUser();
            }
        });
        btnCancel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();

            }
        });

        setVisible(true);
    }

    private void registerUser() {
        /*
        take the entered information and assing it to the variables
         */
        String name = tfName.getText();
        String email = tfEmail.getText();
        String phone = tfPhone.getText();
        String address = tfAddress.getText();
        String password = String.valueOf(pfPassword.getPassword());
        String confirmPass = String.valueOf(pfConfirmPassword.getPassword());

        if (name.isEmpty() || email.isEmpty() || phone.isEmpty() || address.isEmpty() || password.isEmpty()){
            JOptionPane.showMessageDialog(this, "please enter all fields"
                    , "try again",JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (!password.equals(confirmPass)){
            JOptionPane.showMessageDialog(this,
                    "Confirm Password does not match",
                    "Try again",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        user = addUserToDataBase(name,email,phone,address,password);
        if (user != null){
            dispose();
        }else {
            JOptionPane.showMessageDialog(this,
                    "Failed to register new user",
                    "Try again",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private User addUserToDataBase(String name, String email, String phone, String address, String password){
        User user = null;
        final String DB_URL = "jdbc:mysql://127.0.0.1:3306/user_schema";
        final String USERNAME = "root";
        final String PASSWORD = "Mysql123!";

        /*
        import the classes to connect to the database
         */
        try{
            Connection connect = DriverManager.getConnection(DB_URL, USERNAME , PASSWORD);

            Statement statement = connect.createStatement();
            String sql = "INSERT INTO users (name, email, phone, address, password) " +
                    "VALUES (?, ?, ?, ?, ?)";
            PreparedStatement ps = connect.prepareStatement(sql);
            ps.setString(1,name);
            ps.setString(2,email);
            ps.setString(3,phone);
            ps.setString(4,address);
            ps.setString(5,password);

            int addedRows = ps.executeUpdate();
            /*
            if everything is inputted correctly then we need to add the values to the database
             */
            if (addedRows > 0){
                user = new User();
                user.name = name;
                user.email = email;
                user.phone = phone;
                user.address = address;
                user.password = password;
            }

            statement.close();
            connect.close();
        }catch (Exception e){
            e.printStackTrace();
        }
        return user;

        /*
        if the method above fails to add a user to the database then it will
        return null, else it returns the user
         */
    }

    public static void main(String[] args) {
        RegFrom myForm = new RegFrom(null);
        User user = myForm.user;
        if (user != null){
            System.out.println("Successful registration of: " + user.name);
        }
        else {
            System.out.println("Registration cancelled");
        }
    }
}
