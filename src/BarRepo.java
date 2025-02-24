import java.sql.SQLException;

public interface BarRepo {
    public void create() throws SQLException;

    public void find() throws SQLException;

    public void change(String name) throws SQLException;

    public void delete() throws SQLException;
}
