package at.raphael.entity;

import jakarta.persistence.Transient;

import java.util.List;

public class UserConnector {
    public String username;
    public String token;
    public List<RepoInfo> choosenRepos;

    @Transient
    public List<RepoInfo> repositories;

    public UserConnector() {
    }

    public UserConnector(String username, String token) {
        this.username = username;
        this.token = token;
    }
}
