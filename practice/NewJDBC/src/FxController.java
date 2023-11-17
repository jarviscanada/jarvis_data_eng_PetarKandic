import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.logging.Logger;

public class FxController
{


    private static final Logger operationLogger = LoggerSetup.getOperationLogger();
    private static final Logger errorLogger = LoggerSetup.getErrorLogger();
    private RateService rateService;
    private CurrencyService currService;

    public FxController(RateService service, CurrencyService currService) {
        this.rateService = service;
        this.currService = currService;
    }

    public void processExchange(String fromCode, String toCode, String dateString, String amountString) {
        operationLogger.info(String.format("Processing exchange: fromCode=%s, toCode=%s, date=%s, amount=%s",
                fromCode, toCode, dateString, amountString));
        if (!currService.isValidCode(fromCode) || !currService.isValidCode(toCode))
        {
            String message = "Invalid currency code(s).";
            System.out.println(message);
            errorLogger.severe(message);
            return;
        }

        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            java.util.Date utilDate = sdf.parse(dateString);
            double amount = Double.parseDouble(amountString);

            // Convert java.util.Date to java.sql.Date
            java.sql.Date sqlDate = new java.sql.Date(utilDate.getTime());

            double exchangedAmount = rateService.calculateExchange(fromCode, toCode, sqlDate, amount);
            operationLogger.info("Exchanged Amount: " + exchangedAmount);
        }
        catch (ParseException e)
        {
            errorLogger.severe("ParseException: Invalid date format. Please use yyyy-MM-dd.");
        }
        catch (NumberFormatException e)
        {
            errorLogger.severe("NumberFormatException: Invalid amount format. Please enter a valid number.");
        }
        catch (IOException e)
        {
            errorLogger.severe("IOException!");
        }

    }
}
