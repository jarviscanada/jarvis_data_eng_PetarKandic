import java.io.IOException;
import java.util.logging.*;

public class LoggerSetup {

    private static final Logger operationLogger = Logger.getLogger("OperationLogger");
    private static final Logger errorLogger = Logger.getLogger("ErrorLogger");

    static {
        setupLogger(operationLogger, "operation.log");
        setupLogger(errorLogger, "errors.log");
    }

    private static void setupLogger(Logger logger, String fileName) {
        try {
            FileHandler fileHandler = new FileHandler(fileName, true);
            SimpleFormatter formatter = new SimpleFormatter();
            fileHandler.setFormatter(formatter);
            logger.addHandler(fileHandler);
            logger.setUseParentHandlers(false);
        }
        catch (IOException e)
        {
            System.err.println("Could not setup logger: " + e.getMessage());
        }
    }

    public static Logger getOperationLogger() {
        return operationLogger;
    }

    public static Logger getErrorLogger() {
        return errorLogger;
    }
}
