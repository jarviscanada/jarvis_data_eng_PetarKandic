import java.io.IOException;
import java.sql.Date;
import java.util.Optional;

public class RateService
{

    private DailyRateRepo repo;
    private RateHttpHelper http;

    public RateService(DailyRateRepo repo, RateHttpHelper http) {
        this.repo = repo;
        this.http = http;
    }

    public double calculateExchange(String fromSymbol, String toSymbol, Date date, double amount) throws IOException {
        Optional<Rate> rateOpt = repo.getRate(date, fromSymbol, toSymbol);

        if (!rateOpt.isPresent()) {
            rateOpt = http.fetchRate(fromSymbol, toSymbol);
            rateOpt.ifPresent(repo::save);
        }

        return rateOpt.map(rate -> amount * rate.getClose()).orElseThrow(() -> new IllegalArgumentException("Exchange rate not available"));
    }
}
