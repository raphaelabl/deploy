package at.raphael.entity;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.transaction.Transactional;

import java.util.List;

public class Workflow{

    public List<Job> modules;

    public String name;

    //region Constructor
    public Workflow() {
    }
    //endregion


}
