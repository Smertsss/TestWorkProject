import java.sql.SQLException;

public interface StudentRepo {
    public void create() throws SQLException;

    public void find() throws SQLException;

    public void change(int money, double perInBlood) throws SQLException;

    public void delete() throws SQLException;
}