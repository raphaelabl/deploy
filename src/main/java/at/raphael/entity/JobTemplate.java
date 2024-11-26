package at.raphael.entity;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;

@Entity
public class JobTemplate extends PanacheEntity {

    public String name;
    public String runsOn;

    @Column(columnDefinition = "TEXT")
    public String filePart;

    public JobTemplate() {
    }
}
