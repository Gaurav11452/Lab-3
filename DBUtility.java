import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DBCookieUtility {
    private static final String URL = "";
    private static final String USER = "";
    private static final String PASSWORD = "";
    private static Connection connection;
    private static PreparedStatement preparedStatement;
    private static ResultSet resultSet;
    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
    public static void connect() throws SQLException {
        try {
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("Connected to the database");
        } catch (SQLException e) {
            System.err.println("Failed to connect to the database: " + e.getMessage());
            throw e;
        }
    }
    public static void close() {
        try {
            if (resultSet != null) {
                resultSet.close();
            }
            if (preparedStatement != null) {
                preparedStatement.close();
            }
            if (connection != null) {
                connection.close();
                System.out.println("Connection closed");
            }
        } catch (SQLException e) {
            System.err.println("Failed to close connection: " + e.getMessage());
        }
    }
    public static List<CookieSale> getCookieSalesData() throws SQLException {
        List<CookieSale> cookieSales = new ArrayList<>();
        String query = "SELECT * FROM cookies_sales";
        try {
            preparedStatement = connection.prepareStatement(query);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                CookieSale sale = new CookieSale();
                sale.setId(resultSet.getInt("id"));
                sale.setCookieName(resultSet.getString("cookie_name"));
                sale.setManufacturer(resultSet.getString("manufacturer"));
                sale.setSales(resultSet.getInt("sales"));
                cookieSales.add(sale);
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving cookie sales data: " + e.getMessage());
            throw e;
        }
        return cookieSales;
    }
    public static void insertCookieSale(CookieSale sale) throws SQLException {
        String query = "INSERT INTO cookies_sales (cookie_name, manufacturer, sales) VALUES (?, ?, ?)";
        try {
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, sale.getCookieName());
            preparedStatement.setString(2, sale.getManufacturer());
            preparedStatement.setInt(3, sale.getSales());
            preparedStatement.executeUpdate();
            System.out.println("Sale data inserted successfully");
        } catch (SQLException e) {
            System.err.println("Error inserting cookie sale data: " + e.getMessage());
            throw e;
        }
    }
}