package at.raphael.entity;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;

import java.util.List;

@Entity
public class Workflow extends PanacheEntity {

    public String name;
    public String version;

    @OneToMany
    List<JobTemplate> steps;

    public Workflow() {
    }
}
