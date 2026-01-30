package chatweb;
import javax.swing.*;
import java.awt.*;
import java.sql.*;
import static javax.swing.WindowConstants.EXIT_ON_CLOSE;
public class ChucNangChat extends JFrame {
    JTextField txtId;
    JTextField txtContent;
    JTextArea area;
    public ChucNangChat() {
        setTitle("Quản lý tin nhắn (CRUD)");
        setSize(500, 400);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        txtId = new JTextField(5);
        txtContent = new JTextField(20);
        area = new JTextArea();
        area.setEditable(false);
        JButton btnAdd = new JButton("Thêm");
        JButton btnUpdate = new JButton("Sửa");
        JButton btnDelete = new JButton("Xóa");
        JButton btnLoad = new JButton("Xem DS");
        JPanel top = new JPanel();
        top.add(new JLabel("ID:"));
        top.add(txtId);
        top.add(new JLabel("Nội dung:"));
        top.add(txtContent);
        JPanel buttons = new JPanel();
        buttons.add(btnAdd);
        buttons.add(btnUpdate);
        buttons.add(btnDelete);
        buttons.add(btnLoad);
        add(top, BorderLayout.NORTH);
        add(new JScrollPane(area), BorderLayout.CENTER);
        add(buttons, BorderLayout.SOUTH);
        btnAdd.addActionListener(e -> add());
        btnUpdate.addActionListener(e -> update());
        btnDelete.addActionListener(e -> delete());
        btnLoad.addActionListener(e -> load());
    }
    void add() {
        try (Connection c = DBConnection.getConnection()) {
            PreparedStatement ps =
                c.prepareStatement("INSERT INTO messages(content) VALUES(?)");
            ps.setString(1, txtContent.getText());
            ps.executeUpdate();
            JOptionPane.showMessageDialog(this, "Đã thêm!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void update() {
        try (Connection c = DBConnection.getConnection()) {
            PreparedStatement ps =
                c.prepareStatement("UPDATE messages SET content=? WHERE id=?");
            ps.setString(1, txtContent.getText());
            ps.setInt(2, Integer.parseInt(txtId.getText()));
            ps.executeUpdate();
            JOptionPane.showMessageDialog(this, "Đã sửa!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void delete() {
        try (Connection c = DBConnection.getConnection()) {
            PreparedStatement ps =
                c.prepareStatement("DELETE FROM messages WHERE id=?");
            ps.setInt(1, Integer.parseInt(txtId.getText()));
            ps.executeUpdate();
            JOptionPane.showMessageDialog(this, "Đã xóa!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void load() {
        area.setText("");
        try (Connection c = DBConnection.getConnection()) {
            Statement st = c.createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM messages");
            while (rs.next()) {
                area.append(
                    rs.getInt("id") + " - " +
                    rs.getString("content") + "\n"
                );
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new ChucNangChat().setVisible(true);
    }
}
