package chatweb;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MessageDAO {

    // ➕ THÊM
    public static void insert(String sender, String content) throws SQLException {
        String sql = "INSERT INTO messages(sender, content) VALUES (?, ?)";
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, sender);
            ps.setString(2, content);
            ps.executeUpdate();
        }
    }

    // 📋 XEM
    public static List<Message> getAll() throws SQLException {
        List<Message> list = new ArrayList<>();
        String sql = "SELECT * FROM messages ORDER BY id";
        try (Connection c = DBConnection.getConnection();
             Statement st = c.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                list.add(new Message(
                        rs.getInt("id"),
                        rs.getString("sender"),
                        rs.getString("content")
                ));
            }
        }
        return list;
    }

    // ✏ SỬA
    public static void update(int id, String content) throws SQLException {
        String sql = "UPDATE messages SET content=? WHERE id=?";
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, content);
            ps.setInt(2, id);
            ps.executeUpdate();
        }
    }

    // ❌ XÓA
    public static void delete(int id) throws SQLException {
        String sql = "DELETE FROM messages WHERE id=?";
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }
}
