package at.raphael.control;

import at.raphael.entity.RepositoryInfo;
import at.raphael.entity.UserConnector;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;
import org.jboss.logging.Logger;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.Map;
import java.util.Objects;
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
            userConnector.token = getAccessToken(code);
            userConnector.username = getUserNamePerToken(userConnector.token);
            userConnector.repositories = getRepositoriesPerToken(userConnector.token);
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
        return userConnector;
    }

    //region Files Commit
    public void pushFilesToRepository(
            File dir,
            UsernamePasswordCredentialsProvider user,
            RepositoryInfo repository,
            File dirForFileInRepo,
            String fileContent
            ) throws IOException, GitAPIException {
        deleteDirectory(dir);

        Git git = cloneRepositoryWithNoCheckoutToDir(dir, user, repository);
        pushRepository(git, user, dirForFileInRepo.getPath());

        if(git != null){
            createFile(new File(dir.getPath() + dirForFileInRepo.getPath()), fileContent);
        }
    }

    private void createFile(File file, String content) throws IOException {
        try {

            file.getParentFile().mkdirs();
            try (PrintWriter out = new PrintWriter(file)) {
                out.print(content);
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private Git cloneRepositoryWithNoCheckoutToDir(File dir, UsernamePasswordCredentialsProvider user, RepositoryInfo repository) {
        try {
            if (user != null) {
                return Git.cloneRepository()
                        .setURI(repository.htmlUrl)
                        .setDirectory(dir)
                        .setNoCheckout(true)
                        .setCredentialsProvider(
                                user
                        ).call();
            }
            LOG.info("TOKEN IS NULL");
            return null;
        } catch (GitAPIException e) {
            throw new RuntimeException(e);
        }
    }

    private void pushRepository(Git g, UsernamePasswordCredentialsProvider user, String addPath) throws GitAPIException {
        g.add().addFilepattern(Objects.requireNonNull(addPath)).call();

        g.commit().setMessage("Deployment Files where Automatically pushed to your GitHub Repository").call();

        g.push().setCredentialsProvider(user).setRemote("origin").call();

    }

    // Rekursive Methode zum Löschen eines Verzeichnisses
    private void deleteDirectory(File directory) {
        if (directory.exists()) {
            File[] files = directory.listFiles();
            if (files != null) {
                for (File file : files) {
                    if (file.isDirectory()) {
                        deleteDirectory(file);
                    } else {
                        file.delete();
                    }
                }
            }
            directory.delete();
        }
    }

    //endregion


    //region Username
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
    //endregion

    //region Repositories
    private List<RepositoryInfo> getRepositoriesPerToken(String accessToken) throws IOException {
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
        LOG.info("Got access token: " + token);
        return token;
    }
    //endregion

}
