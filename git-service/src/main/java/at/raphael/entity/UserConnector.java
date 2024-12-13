package at.raphael.entity;

import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;

import java.util.List;

public class UserConnector {
    public String username;
    public String encryptedToken;
    public String token;
    public List<RepositoryInfo> repositories;

    public UserConnector() {
    }

    public UserConnector(String username, String token) {
        this.username = username;
        this.token = token;
    }

    public UsernamePasswordCredentialsProvider getCredentialsProvider() {
        return new UsernamePasswordCredentialsProvider(username, token);
    }

}
