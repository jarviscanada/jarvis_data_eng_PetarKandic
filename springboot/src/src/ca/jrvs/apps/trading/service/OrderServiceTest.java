package ca.jrvs.apps.trading.service;

import ca.jrvs.apps.trading.model.MarketOrderDto;
import ca.jrvs.apps.trading.model.SecurityOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertEquals;


@RunWith(SpringRunner.class)
@SpringBootTest
public class OrderServiceTest
{

    @Autowired
    private OrderService orderService;

    @Test
    public void aTest()
    {
        MarketOrderDto orderData = new MarketOrderDto("AAPL", "buy", 5, 3);

        // Creating a mock account to be returned
        SecurityOrder securityOrder = orderService.executeMarketOrder(orderData);
        assertEquals(securityOrder.getNotes(), "grngrnjgrjknfgenk");

    }
}
