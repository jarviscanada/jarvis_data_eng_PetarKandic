package ca.jrvs.apps.trading.dao;

import ca.jrvs.apps.trading.model.IexQuote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


import java.util.List;
import java.util.Optional;

@Repository
public interface MarketDataDao extends JpaRepository<IexQuote, String>
{
    Optional<IexQuote> findByTicker(String ticker);
    List<IexQuote> findAllById(Iterable<String> tickers);
    boolean existsById(String s);
    long count();
    void deleteById(String s);

    void delete(IexQuote entity);

    void deleteAll(Iterable<? extends IexQuote> entities);
    IexQuote save(IexQuote entity);
}
