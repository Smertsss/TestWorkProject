import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SnackRepoImpl implements OrderRepo {
    private int id;
    private String name;
    private int cost;
    private int barId;
    private Connection connection;

    SnackRepoImpl(String name, int cost, int barId, Connection connection) {
        this.name = name;
        this.cost = cost;
        this.barId = barId;
        this.connection = connection;
    }

    @Override
    public void create() throws SQLException {
        String sqlTask = "INSERT INTO Orders (name, cost, barId) VALUES (?, ?, ?) RETURNING id";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sqlTask)) {
            preparedStatement.setString(1, this.name);
            preparedStatement.setInt(2, this.cost);
            preparedStatement.setInt(3, this.barId);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                this.id = resultSet.getInt("id");
            }
        }
        System.out.println("Create successful");
    }

    @Override
    public void find() throws SQLException {
        String sqlTask = "SELECT * FROM Orders WHERE id = " + id;
        try (PreparedStatement preparedStatement = connection.prepareStatement(sqlTask)) {
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                System.out.println(resultSet.getString("name"));
            }
        }
        System.out.println("Find successful");
    }

    @Override
    public void change(String name, int cost, int barId, double per) throws SQLException {
        String sqlTask = "UPDATE Orders SET name = ?, cost = ?, barId = ? WHERE id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sqlTask)) {
            preparedStatement.setString(1, name);
            preparedStatement.setInt(2, cost);
            preparedStatement.setInt(3, barId);
            preparedStatement.setInt(4, this.id);
            preparedStatement.executeUpdate();
            this.name = name;
            this.cost = cost;
            this.barId = barId;
        }
        System.out.println("Change successful");
    }

    @Override
    public void delete() throws SQLException {
        connection.prepareStatement("DELETE FROM Orders WHERE id = " + id);
        System.out.println("Delete successful");
    }
}
