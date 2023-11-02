import okhttp3.OkHttpClient;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.logging.Logger;

public class Main
{

    private static final Logger operationLogger = LoggerSetup.getOperationLogger();
    private static final Logger errorLogger = LoggerSetup.getErrorLogger();

    public static void main(String[] args)
    {
        if (args.length != 4)
        {
            String message = "Application requires exactly 4 arguments: from_code, to_code, date, amount";
            System.err.println(message);
            errorLogger.severe(message);
            return;
        }

        try
        {
            Class.forName("org.postgresql.Driver");
        }
        catch (ClassNotFoundException e) {
            errorLogger.severe("ClassNotFoundException!");
        }
        OkHttpClient client = new OkHttpClient();
        String url = "jdbc:postgresql://localhost:5432/forex";
        try (Connection c = DriverManager.getConnection(url, "postgres", "password")) {
            DailyRateRepo rateRepo = new DailyRateRepo(c);
            CurrencyRepo currRepo = new CurrencyRepo(c);
            RateHttpHelper http = new RateHttpHelper("4ddc16d886msh00cf8e5579be4d4p1ad40ajsn66b9a5f1c046", "FX_DAILY", client);
            RateService rateService = new RateService(rateRepo, http);
            CurrencyService currService = new CurrencyService(currRepo);
            FxController controller = new FxController(rateService, currService);
            controller.processExchange(args[0], args[1], args[2], args[3]);
            operationLogger.info("Application started with arguments: " + Arrays.toString(args));
        } catch (SQLException e)
        {
            errorLogger.severe("SQLException");
        }
    }

}
