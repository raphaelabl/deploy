package at.raphael.entity;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.Entity;

import java.util.Date;

@Entity
public class License extends PanacheEntity {

    public int module;
    public Date activeUntil;


    public License(Date activeUnit, int module) {
        this.activeUntil = activeUnit;
        this.module = module;
    }

    public License() {
    }
}
