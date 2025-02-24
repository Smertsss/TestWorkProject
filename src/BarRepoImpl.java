import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.sql.*;
import java.sql.Connection;

@Getter
@Setter
@AllArgsConstructor
public class BarRepoImpl implements BarRepo {
    private int id;
    private String name;
    private Connection connection;

    BarRepoImpl(String name, Connection connection) {
        this.name = name;
        this.connection = connection;
    }

    @Override
    public void create() throws SQLException {
        String sqlTask = "INSERT INTO Bars (name) VALUES (?) RETURNING id";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sqlTask)) {
            preparedStatement.setString(1, name);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                this.id = resultSet.getInt("id");
            }
        }
        System.out.println("Create successful");
    }

    @Override
    public void find() throws SQLException {
        String sqlTask = "SELECT * FROM Bars WHERE id = " + id;
        try (PreparedStatement preparedStatement = connection.prepareStatement(sqlTask)) {
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                System.out.println(resultSet.getString("name"));
            }
        }
        System.out.println("Find successful");
    }

    @Override
    public void change(String name) throws SQLException {
        String sqlTask = "UPDATE Bars SET name = ? WHERE id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sqlTask)) {
            preparedStatement.setString(1, name);
            preparedStatement.setInt(2, this.id);
            preparedStatement.executeUpdate();
            this.name = name;
        }
        System.out.println("Change successful");
    }

    @Override
    public void delete() throws SQLException {
        connection.prepareStatement("DELETE FROM Bars WHERE id = " + id);
        System.out.println("Delete successful");
    }
}
