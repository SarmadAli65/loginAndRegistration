import javax.swing.*;
import java.awt.*;

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

    public RegFrom(JFrame owner) {
        super(owner);
        setTitle("Create a new account");
        setContentPane(regPanel);
        setMinimumSize(new Dimension(450,474));
        setModal(true);
        setLocationRelativeTo(owner);
        setVisible(true);
    }

    public static void main(String[] args) {
        RegFrom myForm = new RegFrom(null);
    }
}
