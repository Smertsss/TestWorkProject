import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import java.sql.*;

public class Main {
    static final String DATABASE_URL = "jdbc:postgresql://localhost:5432/Work Project 2";
    static final String USER = "ideal";
    static final String PASSWORD = "111";
    static boolean deleteTables = true;
    static boolean createTableSetup = true;
    static boolean createTableValueSetup = false;
    static boolean check = true;

    public static void main(String[] args) throws SQLException {
        Connection connection = null;
        Statement statement = null;
        String sqlTask = null;
        ResultSet resultSet;

        try {
            Class.forName("org.postgresql.Driver");
            connection = DriverManager.getConnection(DATABASE_URL, USER, PASSWORD);
            connection.setAutoCommit(false);
            statement = connection.createStatement();
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }

        if (deleteTables) {
            // Удаление у бота взял
            sqlTask = "DO $$ DECLARE " +
                    "    r RECORD; " +
                    "BEGIN " +
                    "    FOR r IN (SELECT tablename FROM pg_tables WHERE schemaname = 'public') LOOP " +
                    "        EXECUTE 'DROP TABLE IF EXISTS ' || quote_ident(r.tablename) || ' CASCADE'; " +
                    "    END LOOP; " +
                    "END $$;";
            statement.executeUpdate(sqlTask);

            connection.commit();
            System.out.println("Delete Table value successful");
        } else {
            System.out.println("Not delete Table");
        }

        if (createTableSetup) {
            sqlTask = "CREATE TABLE Bars " +
                    "(id SERIAL PRIMARY KEY NOT NULL," +
                    "name TEXT NOT NULL)";
            statement.executeUpdate(sqlTask);

            sqlTask = "CREATE TABLE Orders " +
                    "(id SERIAL PRIMARY KEY NOT NULL," +
                    "name TEXT NOT NULL," +
                    "cost INT NOT NULL," +
                    "per DOUBLE PRECISION," +
                    "barId INT NOT NULL," +
                    "FOREIGN KEY(barId) REFERENCES Bars(id))";
            statement.executeUpdate(sqlTask);

            sqlTask = "CREATE TABLE Students " +
                    "(id SERIAL PRIMARY KEY NOT NULL," +
                    "money INT NOT NULL," +
                    "perInBlood DOUBLE PRECISION)";
            statement.executeUpdate(sqlTask);

            sqlTask = "CREATE TABLE OrderList " +
                    "(studentId INT REFERENCES Students(id) NOT NULL," +
                    "orderId INT REFERENCES Orders(id) NOT NULL," +
                    "value INT NOT NULL," +
                    "PRIMARY KEY (studentId, orderId))";
            statement.executeUpdate(sqlTask);

            connection.commit();
            System.out.println("Create Table successful");
        } else {
            System.out.println("Not create Table");
        }

        if (createTableValueSetup) {
            sqlTask = "INSERT INTO Bars (name) VALUES" +
                    "('Without Bad')," +
                    "('You best')," +
                    "('Another')," +
                    "('Last words')";
            statement.executeUpdate(sqlTask);

            sqlTask = "INSERT INTO Orders (name, cost, per, barId) VALUES" +
                    "('beer', 150, 0.7, 3)," +
                    "('vodka', 300, 2.0, 1)," +
                    "('chicken', 50, null, 2)";
            statement.executeUpdate(sqlTask);

            sqlTask = "INSERT INTO Students (money, perInBlood) VALUES" +
                    "(3000, 0.2)," +
                    "(0, 4.2)," +
                    "(5000, 1.2)";
            statement.executeUpdate(sqlTask);

            // Парочку значений для проверки
            sqlTask = "INSERT INTO OrderList (studentId, orderId, value) VALUES" +
                    "(2, 2, 2)," +
                    "(1, 3, 5)," +
                    "(1, 1, 3)," +
                    "(2, 3, 1)";
            statement.executeUpdate(sqlTask);

            connection.commit();
            System.out.println("Create Table value successful");
        } else {
            System.out.println("Not create Table value");
        }

        // Пример использования
        BarRepoImpl bar1 = new BarRepoImpl("Without Bad", connection);
        bar1.create();
        /*bar1.find();
        bar1.change("You best");
        bar1.find();
        bar1.delete();*/
        connection.commit();

        DrinkRepoImpl drink1 = new DrinkRepoImpl("beer", 150, 1, 0.7, connection);
        drink1.create();
        /*drink1.find();
        drink1.change("vodka", 300, 1, 2.0);
        drink1.find();
        drink1.delete();*/

        SnackRepoImpl snack1 = new SnackRepoImpl("chicken", 50, 1, connection);
        snack1.create();
        /*snack1.find();
        snack1.change("lobster", 550, 1, 0);
        snack1.find();
        snack1.delete();*/

        DrinkRepoImpl drink2 = new DrinkRepoImpl("vodka", 300, 1, 2.0, connection);
        drink2.create();

        StudentRepoImpl student1 = new StudentRepoImpl(3000, 0.2, connection);
        student1.create();
        /*student1.find();
        student1.change(0, 4.2);
        student1.find();
        student1.delete();*/
        connection.commit();

        if (check) {
            System.out.println("----------\n" +
                    "Bar:");
            resultSet = statement.executeQuery("SELECT name FROM Bars ORDER BY id");
            while (resultSet.next()) {
                System.out.println(resultSet.getString("name"));
            }

            System.out.println("----------\n" +
                    "Order:");
            resultSet = statement.executeQuery("SELECT name FROM Orders ORDER BY id");
            while (resultSet.next()) {
                System.out.println(resultSet.getString("name"));
            }

            System.out.println("----------\n" +
                    "Student:");
            resultSet = statement.executeQuery("SELECT * FROM Students ORDER BY id");
            while (resultSet.next()) {
                System.out.println(resultSet.getInt("id") + " " +
                        resultSet.getString("money") + " " +
                        resultSet.getString("perInBlood"));
            }

            System.out.println("----------\n" +
                    "Order list:");
            resultSet = statement.executeQuery("SELECT Orders.name AS name, OrderList.* " +
                    "FROM OrderList " +
                    "JOIN Orders ON OrderList.orderId = Orders.id " +
                    "ORDER BY OrderList.studentId");
            while (resultSet.next()) {
                System.out.println("Student id: " + resultSet.getInt("studentId") + "; order: " +
                        resultSet.getString("name") + "; value: " +
                        resultSet.getString("value"));
            }

            System.out.println("----------");

            System.out.println("Check Table value end");
        } else {
            System.out.println("Not check Table value");
        }

        statement.close();
        connection.close();
    }
}