package at.raphael.control;


import at.raphael.entity.Job;
import at.raphael.entity.JobAttribute;
import at.raphael.entity.Workflow;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.jboss.logging.Logger;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

@ApplicationScoped
public class DeployService {

    @Inject
    Logger LOG;

    @Inject
    GithubService githubService;

    public String createWorkflow(Workflow workflow) {

        String workflowFileContent = "";

        //Add Generell informations . . .
        //TODO Store it out into the Database in Generall informations!!
        workflowFileContent += "## A basic GitHub Actions workflow for your Quarkus application.\n" +
                "\n" +
                "name: CI build\n" +
                "\n" +
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
            String jobTemplateContent = job.refTemplate.filePart;
            for (JobAttribute jobAttribute : job.attributeList){
                String tmpPlaceHolder = "#{" + jobAttribute.name + "}#";
                jobTemplateContent = jobTemplateContent
                        .replace(tmpPlaceHolder, jobAttribute.value!=null?jobAttribute.value:"")
                        .replace("\n", "\n");
            }

            workflowFileContent += jobTemplateContent;

        }

        //TODO Save generation space path in env variable
        File file = new File("generationSpace/build.yml");
        String errors = this.writeStringToFile(workflowFileContent, file);

        githubService.pushFileToGitHub(
                "generationSpace/build.yml",
                workflow.ghRepository.htmlUrl,
                "main",
                ".github/workflows",
                workflow.ghUser.username,
                workflow.ghUser.getToken()
                );

        return errors;
    }

    private String writeStringToFile(String content, File file) {
        try {

            if(file.exists()) {
                file.delete();
            }

            file.getParentFile().mkdirs();
            try (PrintWriter out = new PrintWriter(file)) {
                out.print(content);
            }

            return "File written successfully: " + file.getAbsolutePath();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return "Error writing file: " + e.getMessage();
        }
    }



}
