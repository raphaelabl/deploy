package at.raphael.entity;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.Entity;

@Entity
public class JobAttribute extends PanacheEntity {

    public String name;
    public String value;

    //region Constructor
    public JobAttribute() {
    }

    public JobAttribute(String name, String value) {
        this.name = name;
        this.value = value;
    }
    //endregion

}
