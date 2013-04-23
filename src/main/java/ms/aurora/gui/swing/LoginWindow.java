package ms.aurora.gui.swing;

import ms.aurora.sdn.net.api.Authentication;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * @author rvbiljouw
 */
public class LoginWindow extends JFrame implements ActionListener {
    private final JLabel lblPleaseLogin = new JLabel("Please log in to your dashboard account");
    private final JTextField txtUsername = new JTextField();
    private final JPasswordField txtPassword = new JPasswordField();
    private final JButton btnLogin = new JButton("Log in");

    public LoginWindow() {
        setTitle("Please log in to your dashboard account");
        setSize(300, 200);
        lblPleaseLogin.setBounds(5, 10, 300, 30);
        add(lblPleaseLogin);

        JLabel lblUsername = new JLabel("Username:");
        lblUsername.setBounds(20, 40, 100, 30);
        add(lblUsername);

        JLabel lblPassword = new JLabel("Password:");
        lblPassword.setBounds(20, 80, 100, 30);
        add(lblPassword);

        txtUsername.setBounds(20 + 110, 40, 150, 30);
        add(txtUsername);

        txtPassword.setBounds(20 + 110, 80, 150, 30);
        add(txtPassword);

        btnLogin.setBounds(20, 140, 60, 20);
        btnLogin.addActionListener(this);
        btnLogin.setActionCommand("login");
        add(btnLogin);

        JButton btnCancel = new JButton("Cancel");
        btnCancel.setBounds(90, 140, 60, 20);
        btnCancel.addActionListener(this);
        btnCancel.setActionCommand("cancel");
        add(btnCancel);

        setLayout(null);
        setVisible(true);

        Dimension size = Toolkit.getDefaultToolkit().getScreenSize();
        setLocation((int) (size.getWidth() / 2 - (getWidth() / 2)), (int) (size.getHeight() / 2 - (getHeight() / 2)));
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("cancel")) {
            System.exit(0);
        } else if (e.getActionCommand().equals("login")) {
            btnLogin.setEnabled(false);
            Authentication.login(getUsername(), getPassword());
        }
    }

    public void setMessage(String retmsg) {
        lblPleaseLogin.setText(retmsg);
        btnLogin.setEnabled(true);
    }

    public String getUsername() {
        return txtUsername.getText();
    }

    public String getPassword() {
        return new String(txtPassword.getPassword());
    }
}