package ca.jrvs.apps.trading.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Account
{
    @Id
    @Column(name = "id", columnDefinition = "INTEGER")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;
    @Column(name = "trader_id", unique = true)
    int traderID;
    @Column(name = "amount")
    double amount;

    public Integer getId()
    {
        return id;
    }

    public void setId(Integer id)
    {
        this.id = id;
    }

    public int getTraderID()
    {
        return traderID;
    }

    public void setTraderID(int traderID)
    {
        this.traderID = traderID;
    }

    public double getAmount()
    {
        return amount;
    }

    public void setAmount(double amount)
    {
        this.amount = amount;
    }
}
