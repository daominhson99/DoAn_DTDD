package chatweb;

import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;
import java.io.*;
import java.net.*;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.util.Date;

public class client extends JFrame {

    private JTextPane chatPane;
    private JTextField input;
    private JButton btnSend, btnImage;
    private StyledDocument doc;

    private Socket socket;
    private DataInputStream in;
    private DataOutputStream out;

    private String username;

    public client(String username) {
        this.username = username;
        initUI();
        connectServer();
        listenServer();
    }

    private void initUI() {
        setTitle("Chat Client - " + username);
        setSize(420, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // HEADER
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(new Color(200, 0, 0));
        JLabel title = new JLabel(" Messenger");
        title.setForeground(Color.WHITE);
        title.setFont(new Font("Segoe UI", Font.BOLD, 16));
        header.add(title, BorderLayout.WEST);
        add(header, BorderLayout.NORTH);

        // CHAT AREA
        chatPane = new JTextPane();
        chatPane.setEditable(false);
        chatPane.setBackground(new Color(18, 18, 18));
        chatPane.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 14));
        doc = chatPane.getStyledDocument();

        JScrollPane scroll = new JScrollPane(chatPane);
        scroll.setBorder(null);
        add(scroll, BorderLayout.CENTER);

        // INPUT BAR
        JPanel inputBar = new JPanel(new BorderLayout(5, 5));
        inputBar.setBackground(new Color(30, 30, 30));
        inputBar.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        input = new JTextField();
        input.setBackground(new Color(45, 45, 45));
        input.setForeground(Color.WHITE);
        input.setCaretColor(Color.WHITE);
        input.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        btnImage = new JButton("📷");
        btnSend = new JButton("Gửi");

        inputBar.add(btnImage, BorderLayout.WEST);
        inputBar.add(input, BorderLayout.CENTER);
        inputBar.add(btnSend, BorderLayout.EAST);

        add(inputBar, BorderLayout.SOUTH);

        btnSend.addActionListener(e -> sendText());
        input.addActionListener(e -> sendText());
        btnImage.addActionListener(e -> sendImage());

        setVisible(true);
    }

    private void connectServer() {
        try {
            socket = new Socket("localhost", 1234);
            in = new DataInputStream(socket.getInputStream());
            out = new DataOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Không kết nối được server");
        }
    }

    private void sendText() {
        try {
            if (input.getText().trim().isEmpty()) return;

            out.writeUTF("TEXT");
            out.writeUTF(username + ": " + input.getText());
            out.flush();

            input.setText("");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void sendImage() {
        JFileChooser fc = new JFileChooser();
        if (fc.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            try {
                File file = fc.getSelectedFile();
                byte[] bytes = Files.readAllBytes(file.toPath());

                out.writeUTF("IMAGE");
                out.writeUTF(username);
                out.writeUTF(file.getName());
                out.writeInt(bytes.length);
                out.write(bytes);
                out.flush();

                appendImage(bytes);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void listenServer() {
        new Thread(() -> {
            try {
                while (true) {
                    String type = in.readUTF();

                    if (type.equals("TEXT")) {
                        String msg = in.readUTF();
                        appendText(msg);
                    }

                    if (type.equals("IMAGE")) {
                        String sender = in.readUTF();
                        String name = in.readUTF();
                        int len = in.readInt();
                        byte[] img = new byte[len];
                        in.readFully(img);
                        appendImage(img);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }

    private void appendText(String msg) {
    try {
        SimpleAttributeSet style = new SimpleAttributeSet();
        StyleConstants.setForeground(style, Color.WHITE); // chữ trắng
        StyleConstants.setFontFamily(style, "Segoe UI");
        StyleConstants.setFontSize(style, 14);

        doc.insertString(doc.getLength(), msg + "\n", style);
    } catch (BadLocationException e) {
        e.printStackTrace();
    }
}

    private void appendImage(byte[] imgBytes) {
        try {
            ImageIcon icon = new ImageIcon(imgBytes);
            Image img = icon.getImage().getScaledInstance(160, -1, Image.SCALE_SMOOTH);
            icon = new ImageIcon(img);

            chatPane.setCaretPosition(doc.getLength());
            chatPane.insertIcon(icon);
            doc.insertString(doc.getLength(), "\n", null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // MAIN để test nhanh
    public static void main(String[] args) {
        new client("Người Dùng");
    }
}
