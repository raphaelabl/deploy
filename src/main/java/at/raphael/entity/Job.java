package at.raphael.entity;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;

import java.util.List;

@Entity
public class Job extends PanacheEntity {

    public String name;

    @OneToMany
    public List<JobAttribute> atributeList;

    public Job() {
    }
}
