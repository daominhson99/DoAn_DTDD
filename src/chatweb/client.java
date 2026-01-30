package chatweb;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.*;
import java.net.Socket;
import static javax.swing.WindowConstants.EXIT_ON_CLOSE;

public class client extends JFrame {

    private String username;

    private JTextArea txtArea;
    private JTextField txtMessage;
    private JButton btnSend;

    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;

    // Constructor có username (dùng sau login)
    public client(String username) {
        this.username = username;
        initUI();
        connectServer();
    }

    // Constructor rỗng (phòng khi test)
    public client() {
        this("Guest");
    }

    private void initUI() {
        setTitle("Chat Client - " + username);
        setSize(500, 400);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        txtArea = new JTextArea();
        txtArea.setEditable(false);
        JScrollPane scroll = new JScrollPane(txtArea);

        txtMessage = new JTextField();
        btnSend = new JButton("Gửi");

        JPanel bottom = new JPanel(new BorderLayout());
        bottom.add(txtMessage, BorderLayout.CENTER);
        bottom.add(btnSend, BorderLayout.EAST);

        add(scroll, BorderLayout.CENTER);
        add(bottom, BorderLayout.SOUTH);

        // gửi tin khi bấm nút
        btnSend.addActionListener((ActionEvent e) -> sendMessage());

        // gửi tin khi nhấn Enter
        txtMessage.addActionListener(e -> sendMessage());
    }

    private void connectServer() {
        try {
            socket = new Socket("localhost", 1234);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);

            txtArea.append("✔ Đã kết nối server\n");

            // luồng nhận tin
            new Thread(() -> {
                try {
                    String line;
                    while ((line = in.readLine()) != null) {
                        txtArea.append(line + "\n");
                    }
                } catch (IOException ex) {
                    txtArea.append("❌ Mất kết nối server\n");
                }
            }).start();

        } catch (IOException e) {
            JOptionPane.showMessageDialog(this,
                    "Không kết nối được server!",
                    "Lỗi",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void sendMessage() {
        String msg = txtMessage.getText().trim();
        if (msg.isEmpty()) return;

        out.println(username + ": " + msg);
        txtMessage.setText("");
    }

    // test độc lập
    public static void main(String[] args) {
        new client("admin_DMS").setVisible(true);
    }
}
