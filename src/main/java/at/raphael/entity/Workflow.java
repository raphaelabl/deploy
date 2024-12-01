package at.raphael.entity;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;

import java.util.List;

@Entity
public class Workflow extends PanacheEntity {

    @ManyToOne
    public UserConnector ghUser;

    @ManyToOne
    public RepositoryInfo ghRepository;

    @OneToMany
    public List<Job> modules;

    public String name;

    public void update(Workflow newEntity){
        this.name = newEntity.name;
        this.ghUser = newEntity.ghUser;
        this.ghRepository = newEntity.ghRepository;
        this.modules = newEntity.modules;
    }

    //region Constructor
    public Workflow() {
    }
    //endregion


}
