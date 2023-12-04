package ca.jrvs.apps.trading;

import ca.jrvs.apps.trading.controller.QuoteController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@ComponentScan(basePackages = {"ca.jrvs.apps.trading.controller",
        "ca.jrvs.apps.trading.dao", "ca.jrvs.apps.trading.service"})
@EntityScan(basePackages={"ca.jrvs.apps.trading.model"})
@EnableJpaRepositories(basePackages = {"ca.jrvs.apps.trading.dao"})
public class Application implements CommandLineRunner
{
    private Logger logger = LoggerFactory.getLogger(Application.class);

    @Autowired
    QuoteController quoteController;


    public static void main(String[] args)
    {
        SpringApplication.run(Application.class, args);
    }

    @Override
    //@Transactional
    public void run(String... args) throws Exception
    {
        logger.info("Starting initialization tasks...");
        logger.info("Initialization tasks completed");
        logger.info("The application is now running");
        logger.info("Please make your requests now.");
    }
}
