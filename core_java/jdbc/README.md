# Introduction
The Stock Quote App simulates a simple stock portfolio. It may be used on its own (to demonstrate the functions of typical portfolio apps), or as a base to build more advanced stock portfolio applications. JDBC is used to connect to, and operate on, a PostgreSQL databasem. These databases store information on user commands and stocks.

# Implementaiton
## ER Diagram
![ER diagram: ](C:\Users\petar\Documents\Diagram.png)

## Design Patterns
We use Data Access Objects (DAOs) to comminicate with the database. DAOs are types of classes which are used to provide a layer of abstraction. Instead of directly communicating with a database, we create a DAO which has certain methods and attributes. These are used from another class to communicate with a database. For both our Position ("owned" stocks) and Quote (available stocks) databases, we have implemented a DAO. These DAOs are accessed from their respective "service" classes. Besides comminicating with the DAO, they provide certain functionalities (such as parsing of inputs). The QuoteHTTPHelper class is used to scrape data from the Alpha Vantage endpoint. The StockQuoteController class includes the main method, and is used to provide a layer of control, as it accepts user inputs.
# Test
Two different types of testing programs were used. jUnit was used to test individual commands, including "buy", and "save". Mockito was used to test the DAOs. In particular, we tested how the service classes interacted with the DAOs, by passing several different inputs and ensuring that the proper methods were executed.
