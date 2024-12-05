package at.raphael.control;


import at.raphael.entity.Job;
import at.raphael.entity.JobAttribute;
import at.raphael.entity.Workflow;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;
import org.jboss.logging.Logger;

import java.io.File;
import java.io.IOException;

@ApplicationScoped
public class DeployService {

    @Inject
    Logger LOG;

    @Inject
    GithubService githubService;

    public String createWorkflow(Workflow workflow) throws IOException, GitAPIException {

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
        LOG.info(workflow.ghUser.getToken());

        UsernamePasswordCredentialsProvider credentialsProvider = new UsernamePasswordCredentialsProvider( workflow.ghUser.username,
                workflow.ghUser.getToken());


        githubService.pushFilesToRepository(
            new File("/Users/raphaelablinger/Documents/Development/Deploy/workdir/"),
            credentialsProvider,
            workflow.ghRepository,
            new File("/.github/workflows/build.yml"),
            workflowFileContent
        );

        return "";
    }



}
