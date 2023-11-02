import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

public class CurrencyRepo
{

    private Connection c;

    public CurrencyRepo(Connection c) {
        this.c = c;
    }

    public Optional<Currency> getCurrency(String code) {
        try (PreparedStatement s = c.prepareStatement("SELECT * FROM currency WHERE symbol=?")) {
            s.setString(1, code);
            ResultSet rs = s.executeQuery();
            if (rs.next()) {
                Currency curr = new Currency();
                curr.setCode(code);
                curr.setName(rs.getString("name"));
                return Optional.of(curr);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

}
