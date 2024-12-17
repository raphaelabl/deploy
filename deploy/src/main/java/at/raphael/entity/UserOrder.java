package at.raphael.entity;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.Entity;

import java.util.Date;

@Entity
public class UserOrder extends PanacheEntity {

    public double price;
    public Date date;
    public int module;

    public UserOrder() {
    }

    public UserOrder(double price, Date date, int module) {
        this.price = price;
        this.date = date;
        this.module = module;
    }
}
