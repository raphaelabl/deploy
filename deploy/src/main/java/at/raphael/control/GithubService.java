package at.raphael.control;

import at.raphael.entity.RepositoryInfo;
import at.raphael.entity.UserConnector;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.jboss.logging.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@ApplicationScoped
public class GithubService {

    @Inject
    Logger LOG;

    private static final String GITHUB_API_URL = "https://api.github.com/user"; // GitHub API URL für Repositories
    private static final String CLIENT_ID = "Ov23li8HrLXJ9gME6pH5";
    private static final String CLIENT_SECRET = "be7153d0e2a48f1cc691f240d8d9d430d796a063";
    private static final String TOKEN_URL = "https://github.com/login/oauth/access_token";


    public UserConnector getUserData(String code){
        UserConnector userConnector = new UserConnector();
        try {
            userConnector.setToken(getAccessToken(code));
            userConnector.username = getUserNamePerToken(userConnector.token);
            userConnector.repositories = getRepositoriesPerToken(userConnector.token);
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
        return userConnector;
    }


    //region Userdata
    private String getUserNamePerToken(String token) throws IOException {
        URL url = new URL(GITHUB_API_URL);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setRequestProperty("Authorization", "Bearer " + token);

        connection.connect();

        BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        StringBuilder response = new StringBuilder();
        String line;

        while ((line = reader.readLine()) != null) {
            response.append(line);
        }

        // Parst die JSON-Antwort und extrahiert den Benutzernamen
        String responseBody = response.toString();

        return extractUsernameFromResponse(responseBody);
    }

    private String extractUsernameFromResponse(String responseBody) {
        // Einfache JSON-Verarbeitung - in der Praxis sollte dies robuster sein
        String[] parts = responseBody.split(",");
        for (String part : parts) {
            if (part.contains("\"login\":")) {
                return part.split(":")[1].replace("\"", "").trim();
            }
        }
        return null;
    }


    //region Repositories
    public List<RepositoryInfo> getRepositoriesPerToken(String accessToken) throws IOException {
        URL url = new URL(GITHUB_API_URL + "/repos");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setRequestProperty("Authorization", "Bearer " + accessToken);

        connection.connect();

        // Antwort der API verarbeiten
        ObjectMapper objectMapper = new ObjectMapper();
        List<Map<String, Object>> repos = objectMapper.readValue(connection.getInputStream(), List.class);

        // Repositories in eine Liste von RepoInfo umwandeln
        return repos.stream()
                .map(repo -> new RepositoryInfo(
                        repo.get("name").toString(),
                        repo.get("full_name").toString(),
                        repo.get("description") != null ? repo.get("description").toString() : "Keine Beschreibung",
                        repo.get("html_url").toString())
                )
                .collect(Collectors.toList());
    }
    //endregion

    //region Token
    private String getAccessToken(String code) throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(TOKEN_URL))
                .header("Accept", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(
                        "client_id=" + CLIENT_ID +
                                "&client_secret=" + CLIENT_SECRET +
                                "&code=" + code))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        ObjectMapper mapper = new ObjectMapper();
        String token = mapper.readTree(response.body()).get("access_token").asText();
        return token;
    }
    //endregion

    //endregion

}
