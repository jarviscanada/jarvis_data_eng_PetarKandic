import java.sql.*;
import java.util.Optional;

public class DailyRateRepo {

    private Connection c;

    public DailyRateRepo(Connection c) {
        this.c = c;
    }

    public Optional<Rate> getRate(Date date, String fromCode, String toCode) {
        try (PreparedStatement s = c.prepareStatement("SELECT * FROM daily_rate WHERE day=? AND from_symbol=? AND to_symbol=?")) {
            s.setDate(1, new java.sql.Date(date.getTime()));
            s.setString(2, fromCode);
            s.setString(3, toCode);
            ResultSet rs = s.executeQuery();
            if (rs.next()) {
                Rate rate = new Rate();
                rate.setDate(date);
                rate.setFromSymbol(fromCode);
                rate.setToSymbol(toCode);
                rate.setOpen(rs.getDouble("open"));
                rate.setHigh(rs.getDouble("high"));
                rate.setLow(rs.getDouble("low"));
                rate.setClose(rs.getDouble("close"));
                return Optional.of(rate);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    public void save(Rate r) {
        try (PreparedStatement s = c.prepareStatement("INSERT INTO daily_rate (date, from_symbol, to_symbol, open, high, low, close) VALUES (?, ?, ?, ?, ?, ?, ?)")) {
            s.setDate(1, new java.sql.Date(r.getDate().getTime()));
            s.setString(2, r.getFromSymbol());
            s.setString(3, r.getToSymbol());
            s.setDouble(4, r.getOpen());
            s.setDouble(5, r.getHigh());
            s.setDouble(6, r.getLow());
            s.setDouble(7, r.getClose());
            s.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
