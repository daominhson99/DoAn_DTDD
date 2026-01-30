package chatweb;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {

    // 🔹 ĐÚNG TÊN SERVER CỦA BẠN
    private static final String URL =
            "jdbc:sqlserver://DaoMinhSon\\SQLEXPRESS01;"
          + "databaseName=doanchatonl;"
          + "encrypt=true;"
          + "trustServerCertificate=true;";

    // 🔹 USER & PASSWORD SAU KHI ĐẶT LẠI
    private static final String USER = "sa";
    private static final String PASSWORD = "123456"; // đổi nếu bạn đặt khác

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}
