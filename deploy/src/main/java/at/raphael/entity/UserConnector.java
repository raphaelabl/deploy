package at.raphael.entity;

import at.raphael.utils.EncryptionUtils;
import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.Transient;

import java.util.List;

@Entity
public class UserConnector extends PanacheEntity {
    public String username;

    public String encryptedToken;

    @Transient
    public String token;

    @Transient
    public List<RepositoryInfo> repositories;

    public UserConnector() {
    }

    public UserConnector(String username, String token) {
        this.username = username;
        this.token = token;
    }

    //region Token
    public void setToken(String token) {
        this.token = token;
        this.encryptedToken = EncryptionUtils.encrypt(token); // Token verschlüsseln
    }

    // Gibt das entschlüsselte Token zurück
    public String getToken() {
        return EncryptionUtils.decrypt(this.encryptedToken); // Token entschlüsseln
    }

    // Getter für das verschlüsselte Token, falls benötigt
    public String getEncryptedToken() {
        return this.encryptedToken;
    }

    // Setter für das verschlüsselte Token
    public void setEncryptedToken(String encryptedToken) {
        this.encryptedToken = encryptedToken;
    }
    //endregion
}
