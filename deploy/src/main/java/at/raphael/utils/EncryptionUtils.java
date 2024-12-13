package at.raphael.utils;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

public class EncryptionUtils {


    static String encryptionKey = "iYqHLnZqTCK0ynoZqyEwcw=="; // Der geheime Schlüssel aus der Konfiguration

    private static final String ALGORITHM = "AES";

    private static final SecretKey SECRET_KEY = loadSecretKey();


    private static SecretKey loadSecretKey() {
        if (encryptionKey == null || encryptionKey.isEmpty()) {
            throw new RuntimeException("Encryption key not found in application.properties");
        }
        return new SecretKeySpec(Base64.getDecoder().decode(encryptionKey), ALGORITHM);  // Schlüssel dekodieren
    }



    // Verschlüsselt das gegebene Daten
    public static String encrypt(String data) {
        try {
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, SECRET_KEY);
            byte[] encryptedBytes = cipher.doFinal(data.getBytes());
            return Base64.getEncoder().encodeToString(encryptedBytes);
        } catch (Exception e) {
            throw new RuntimeException("Encryption error", e);
        }
    }

    // Entschlüsselt das verschlüsselte Daten
    public static String decrypt(String encryptedData) {
        if(encryptedData == null || encryptedData.isEmpty()) {
            return "";
        }
        try {
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, SECRET_KEY);
            byte[] decryptedBytes = cipher.doFinal(Base64.getDecoder().decode(encryptedData));
            return new String(decryptedBytes);
        } catch (Exception e) {
            throw new RuntimeException("Decryption error", e);
        }
    }

    //    später mit docker compose file
    /*private static SecretKey loadSecretKey() {
        String key = System.getenv("ENCRYPTION_KEY");  // Umgebungsvariable lesen
        if (key == null || key.isEmpty()) {
            throw new RuntimeException("Encryption key not found in environment variables");
        }
        return new SecretKeySpec(Base64.getDecoder().decode(key), ALGORITHM);  // Schlüssel dekodieren
    }*/
}
