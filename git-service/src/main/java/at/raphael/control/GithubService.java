package at.raphael.control;

import at.raphael.entity.*;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.transport.URIish;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;
import org.jboss.logging.Logger;

import java.io.*;
import java.net.URISyntaxException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ApplicationScoped
public class GithubService {

    @Inject
    Logger log;

    final String repositoriesLocation = "repositories/";

    //region Module Definition
    public List<ModulePath> defineModules(UserConnector connector, RepositoryInfo info) {

        File repoDir = new File(repositoriesLocation + connector.username + "/" + info.name);
        // Clone Project

        Git git = this.cloneRepositoryWithNoCheckoutToDir(
                repoDir,
                new UsernamePasswordCredentialsProvider(connector.username, connector.token),
                info
                );

        if(git == null) return null;

        // Search for Files, Folders, ...
        List<JobTemplate> jobTemplates = listAllTemplates();
        List<ModulePath> result = new ArrayList<>();

        for(JobTemplate jobTemplate : jobTemplates) {
            for(String identificationFile : jobTemplate.identificationFiles){
                String path = searchForIdentificationFile(identificationFile, repoDir);
                if(path != null) {
                    if(jobTemplate.id != null && jobTemplate.id != 0) {
                        result.add(new ModulePath(jobTemplate.id, path));
                    }
                }
                break;
            }
        }

        // When all things are done delete the Repository
        deleteDirectory(repoDir);

        return result;
    }


    //region

    private String searchForIdentificationFile(String identificationFile, File startDir) {
        final String[] filePath = {null};

        // FileVisitor zur Rekursion erstellen
        try {
            Files.walkFileTree(startDir.toPath(), new SimpleFileVisitor<>() {
                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) {
                    if (file.toString().contains(identificationFile)) {
                        filePath[0] = file.toString();
                        return FileVisitResult.TERMINATE; // Suche abbrechen, wenn gefunden
                    }
                    return FileVisitResult.CONTINUE;
                }
            });
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        String result = null;
        if(filePath[0] != null && filePath[0].indexOf(startDir.toString()) == 0) {
            result = filePath[0].split(startDir.toString())[1];
            result = result.split(identificationFile)[0];
        }

        result = "." + result;
        return result; // Gibt den Pfad zurück oder null, wenn nicht gefunden
    }
    //endregion

    @Transactional
    public List<JobTemplate> listAllTemplates() {
        return JobTemplate.listAll();
    }

    //endregion

    //region File Pushing
    public List<String> pushToGithub(DeploymentInfo deploymentInfo){

        List<String> errors = new ArrayList<>();

        // Clone Project
        File repoDir = new File(repositoriesLocation + deploymentInfo.ghUser.username + "/" + deploymentInfo.repository.name + "/");
        Git git = this.cloneRepositoryWithNoCheckoutToDir(
                repoDir,
                new UsernamePasswordCredentialsProvider(deploymentInfo.ghUser.username, deploymentInfo.ghUser.token),
                deploymentInfo.repository
        );

        if(git == null) {
            errors.add("Could not clone repository");
            return errors;
        };

        //Insert Files
        // Workflow
        Map<File, String> files = new HashMap<>();
        if(deploymentInfo.workflow == null || deploymentInfo.workflow.modules == null){
            errors.add("Workflow or modules are null!");
            return errors;
        }

        files.put(new File(repoDir.getPath() + "/github/workflows/ci.yml"), createWorkflowContent(deploymentInfo.workflow));

        // Dockerfiles
        for(Job j : deploymentInfo.workflow.modules) {
            String dockerfileContend = createDockerfile(j);
            if(dockerfileContend != null && !dockerfileContend.isEmpty()) {
                files.put(new File(repoDir.getPath() + "/" + j.pathToRoot + "/Dockerfile"), dockerfileContend);
            }
        }

        // Docker Compose
        String dockerComposeContent = createDockerCompose(deploymentInfo.workflow);
        if(dockerComposeContent != null && !dockerComposeContent.isEmpty()) {
            files.put(new File(repoDir.getPath() + "/docker-compose.yml"), dockerComposeContent);
        }

        files.forEach((file, s) -> {
            try {
                createFile(file, s);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });



        //Push
        try {
            pushRepository(
                    git,
                    deploymentInfo.ghUser.getCredentialsProvider(),
                    repoDir.getPath(),
                    deploymentInfo.repository.htmlUrl
            );
        } catch (GitAPIException e) {
            throw new RuntimeException(e);
        }

        //Delete Repository
        deleteDirectory(repoDir);

        return errors;
    }

    private void pushRepository(Git g, UsernamePasswordCredentialsProvider user, String fullFilePath, String repoUrl) throws GitAPIException {

        File f = new File(fullFilePath);
        if(!f.exists()){
            return;
        }

        g.add().addFilepattern(".").call();
        g.commit().setMessage("Deployment Files where Automatically pushed to your GitHub Repository").call();

        try {
            g.remoteSetUrl().setRemoteName("origin").setRemoteUri(new URIish(repoUrl));
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
        g.push().setRemote("origin").setCredentialsProvider(user).call();

    }

    //region File Creation
    public String createDockerfile(Job job) {
        String jobTemplateContent = job.refTemplate.dockerFileContent;

        if(jobTemplateContent == null) {
            return null;
        }

        for (JobAttribute jobAttribute : job.dockerfileAttributes){
            if(jobAttribute == null){
                continue;
            }
            String tmpPlaceHolder = "#{" + jobAttribute.name + "}#";
            jobTemplateContent = jobTemplateContent
                    .replace(tmpPlaceHolder, jobAttribute.value!=null?jobAttribute.value:"");
        }

        log.info(jobTemplateContent);

        return jobTemplateContent;

    }

    public String createWorkflowContent(Workflow workflow){
        String workflowFileContent = "";

        //Add Generell informations . . .
        //TODO Store it out into the Database in Generall informations!!
        workflowFileContent += "## A basic GitHub Actions workflow for your Quarkus application.\n" +
                " \n" +
                "name: CI build\n" +
                " \n" +
                "on:\n" +
                "  push:\n" +
                "    branches: [ main ]\n" +
                "  pull_request:\n" +
                "    branches: [ main ]\n" +
                "env:\n" +
                "  REGISTRY: ghcr.io\n" +
                "\n" +
                "jobs:\n  ";

        for (Job job : workflow.modules){
            String jobTemplateContent = job.refTemplate.workflowFileContent;
            for (JobAttribute jobAttribute : job.workflowAttributes){
                String tmpPlaceHolder = "#{" + jobAttribute.name + "}#";
                jobTemplateContent = jobTemplateContent
                        .replace(tmpPlaceHolder, jobAttribute.value!=null?jobAttribute.value:"");
                jobTemplateContent = jobTemplateContent + "\n";
            }

            workflowFileContent += jobTemplateContent;

        }

        return workflowFileContent;
    }

    public String createDockerCompose(Workflow workflow){
            String dockerComposeFileContent = "";

            //Add Generell informations . . .
            //TODO Store it out into the Database in Generall informations!!
            dockerComposeFileContent += "version: '3.1'\n" +
                    "services:\n";

            for (Job job : workflow.modules){
                if(job.refTemplate.dockerComposeContent != null && !job.refTemplate.dockerComposeContent.isEmpty()) {
                    String jobTemplateContent = job.refTemplate.dockerComposeContent;
                    for (JobAttribute jobAttribute : job.dockerComposeAttributes) {
                        String tmpPlaceHolder = "#{" + jobAttribute.name + "}#";
                        jobTemplateContent = jobTemplateContent
                                .replace(tmpPlaceHolder, jobAttribute.value != null ? jobAttribute.value : "");
                        jobTemplateContent = jobTemplateContent + "\n";
                    }

                    dockerComposeFileContent += jobTemplateContent;
                }else{
                    return null;
                }
            }

            return dockerComposeFileContent;

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
    //endregion


    //endregion


    //region Repository Handling
    private Git cloneRepositoryWithNoCheckoutToDir(File dir, UsernamePasswordCredentialsProvider user, RepositoryInfo repository) {
        try {
            if (user != null) {
                if(dir.exists()){
                    deleteDirectory(dir);
                }

                return Git.cloneRepository()
                        .setURI(repository.htmlUrl)
                        .setDirectory(dir)
                        .setCredentialsProvider(
                                user
                        ).call();
            }
            return null;
        } catch (GitAPIException e) {
            throw new RuntimeException(e);
        }
    }

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

}
