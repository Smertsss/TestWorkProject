import java.sql.SQLException;

public interface OrderRepo {
    public void create() throws SQLException;

    public void find() throws SQLException;

    public void change(String name, int cost, int barId, double per) throws SQLException;

    public void delete() throws SQLException;
}
