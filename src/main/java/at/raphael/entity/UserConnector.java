package at.raphael.entity;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.Transient;

import java.util.List;

@Entity
public class UserConnector extends PanacheEntity {
    public String username;
    public String token;

    @Transient
    public List<RepositoryInfo> repositories;

    public UserConnector() {
    }

    public UserConnector(String username, String token) {
        this.username = username;
        this.token = token;
    }
}
