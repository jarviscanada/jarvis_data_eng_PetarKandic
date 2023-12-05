package ca.jrvs.apps.trading;

import ca.jrvs.apps.trading.controller.OrderController;
import ca.jrvs.apps.trading.controller.QuoteController;
import ca.jrvs.apps.trading.dao.*;
import ca.jrvs.apps.trading.service.OrderService;
import ca.jrvs.apps.trading.service.TraderAccountService;
import org.springframework.context.annotation.PropertySource;
import ca.jrvs.apps.trading.service.QuoteService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import javax.sql.DataSource;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.util.HashMap;

@Configuration
@EnableTransactionManagement
@PropertySource("classpath:/application.properties")
public class AppConfig implements EnvironmentAware {
    private Logger logger = LoggerFactory.getLogger(AppConfig.class);

    private MarketDataDao marketDataDao;
    private QuoteDao quoteDao;

    private TraderDao targetDao;

    private AccountDao accountDao;

    @Bean
    public MarketDataDao marketDataConfig(MarketDataDao marketDataDao) {
        this.marketDataDao = marketDataDao;
        return this.marketDataDao;
    }

    private Environment env;

    public String getProperty(String key) {
        return env.getProperty(key);
    }

    @Override
    public void setEnvironment(final Environment environment) {
        this.env = environment;
    }


    @Bean
    public QuoteService quoteService(MarketDataDao marketDataDao, QuoteDao quoteDao) {
        return new QuoteService(marketDataDao, quoteDao);
    }

    @Bean
    public TraderAccountService quoteService(TraderDao traderDao, AccountDao accountDao) {
        return new TraderAccountService(traderDao, accountDao);
    }

    @Bean
    public QuoteController quoteController() {
        return new QuoteController(quoteService(marketDataDao, quoteDao));
    }


    @Bean
    public DataSource dataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("org.postgresql.Driver");
        dataSource.setUrl("jdbc:postgresql://localhost:5432/jrvstrading");
        dataSource.setUsername("postgres");
        dataSource.setPassword("password");
        return dataSource;
    }

    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(DataSource dataSource) throws InterruptedException { // Inject DataSource
        LocalContainerEntityManagerFactoryBean emf = new LocalContainerEntityManagerFactoryBean();
        emf.setDataSource(dataSource);
        emf.setPackagesToScan("ca.jrvs.apps.trading.model");
        emf.setPersistenceUnitName("default");

        HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        emf.setJpaVendorAdapter(vendorAdapter);

        HashMap<String, Object> properties = new HashMap<>();
        properties.put("hibernate.hbm2ddl.auto", "update");
        properties.put("hibernate.dialect", "org.hibernate.dialect.PostgreSQLDialect");
        emf.setJpaPropertyMap(properties);

        return emf;
    }

}