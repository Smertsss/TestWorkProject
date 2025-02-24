import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class StudentRepoImpl implements StudentRepo {
    int id;
    int money;
    double perInBlood;
    private Connection connection;

    StudentRepoImpl(int money, double perInBlood, Connection connection) {
        this.money = money;
        this.perInBlood = perInBlood;
        this.connection = connection;
    }

    @Override
    public void create() throws SQLException {
        String sqlTask = "INSERT INTO Students (money, perInBlood) VALUES (?, ?) RETURNING id";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sqlTask)) {
            preparedStatement.setInt(1, money);
            preparedStatement.setDouble(2, perInBlood);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                this.id = resultSet.getInt("id");
            }
        }
        System.out.println("Create successful");
    }

    @Override
    public void find() throws SQLException {
        String sqlTask = "SELECT * FROM Students WHERE id = " + id;
        try (PreparedStatement preparedStatement = connection.prepareStatement(sqlTask)) {
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                System.out.println(resultSet.getString("id") + "; " +
                        resultSet.getString("money") + "; " +
                        resultSet.getString("perInBlood"));
            }
        }
        System.out.println("Find successful");
    }

    @Override
    public void change(int money, double perInBlood) throws SQLException {
        String sqlTask = "UPDATE Students SET money = ?, perInBlood = ? WHERE id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sqlTask)) {
            preparedStatement.setInt(1, money);
            preparedStatement.setDouble(2, perInBlood);
            preparedStatement.setInt(3, this.id);
            preparedStatement.executeUpdate();
            this.money = money;
            this.perInBlood = perInBlood;
        }
        System.out.println("Change successful");
    }

    @Override
    public void delete() throws SQLException {
        connection.prepareStatement("DELETE FROM Students WHERE id = " + id);
        System.out.println("Delete successful");
    }
}
