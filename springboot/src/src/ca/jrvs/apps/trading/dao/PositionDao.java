package ca.jrvs.apps.trading.dao;

import ca.jrvs.apps.trading.model.Quote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PositionDao extends JpaRepository<Quote, String>{
}