package chatweb;

import javax.swing.*;
import java.awt.*;
import chatweb.client;

public class DangNhap extends JFrame {

    JTextField txtUser;
    JPasswordField txtPass;
    public DangNhap() {
        setTitle("Login");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        txtUser = new JTextField(15);
        txtPass = new JPasswordField(15);
        JButton btnLogin = new JButton("Login");

        JPanel p = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // User
        gbc.gridx = 0;
        gbc.gridy = 0;
        p.add(new JLabel("User:"), gbc);

        gbc.gridx = 1;
        p.add(txtUser, gbc);

        // Pass
        gbc.gridx = 0;
        gbc.gridy = 1;
        p.add(new JLabel("Pass:"), gbc);

        gbc.gridx = 1;
        p.add(txtPass, gbc);

        // Button Login
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        p.add(btnLogin, gbc);

        add(p);
        pack();

        // Click Login
        btnLogin.addActionListener(e -> login());

        // ENTER = Login
        getRootPane().setDefaultButton(btnLogin);
    }
    private void login() {
        String u = txtUser.getText();
        String p = new String(txtPass.getPassword());

        if (UserDAO.login(u, p)) {
            JOptionPane.showMessageDialog(this, "Login OK");
            new client(u).setVisible(true);
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Sai user hoặc pass");
        }
    }
    public static void main(String[] args) {
        new DangNhap().setVisible(true);
    }
}
