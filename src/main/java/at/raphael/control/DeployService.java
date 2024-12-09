package at.raphael.control;


import at.raphael.entity.DeploymentInfo;
import at.raphael.entity.Job;
import at.raphael.entity.JobAttribute;
import at.raphael.entity.UserConnector;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;
import org.jboss.logging.Logger;

import java.io.File;
import java.io.IOException;
import java.util.List;

@ApplicationScoped
public class DeployService {

    @Inject
    Logger LOG;

    @Inject
    GithubService githubService;


    @Transactional
    public DeploymentInfo persistDeployment(DeploymentInfo deploymentInfo) {
        if(deploymentInfo.id != null&& deploymentInfo.id != 0) {
            return updateDeployment(deploymentInfo);
        }

        UserConnector persisted = new UserConnector();

        if(deploymentInfo.ghUser.id != null && deploymentInfo.ghUser.id != 0){
            deploymentInfo.ghUser = UserConnector.findById(deploymentInfo.ghUser.id);
        }

        if(deploymentInfo.repository.id != null && deploymentInfo.repository.id != 0){
            deploymentInfo.repository = UserConnector.findById(deploymentInfo.repository.id);
        }

        deploymentInfo.state = "Persisted";

        deploymentInfo.persist();

        return deploymentInfo;
    }


    @Transactional
    public DeploymentInfo updateDeployment(DeploymentInfo deploymentInfo) {

        if(deploymentInfo.id == null || deploymentInfo.id == 0){
            persistDeployment(deploymentInfo);
            return deploymentInfo;
        }

        DeploymentInfo persisted = DeploymentInfo.findById(deploymentInfo.id);

        if(persisted != null){
            persisted.update(deploymentInfo);
        }

        return persisted;
    }


    public String deploy(DeploymentInfo deploymentInfo) {


        return "";
    }

    public String createWorkflowFromDeployment(DeploymentInfo deploymentInfo) throws IOException, GitAPIException {

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

        for (Job job : deploymentInfo.workflow.modules){
            String jobTemplateContent = job.refTemplate.workflowFileContent;
            for (JobAttribute jobAttribute : job.workflowAttributeList){
                String tmpPlaceHolder = "#{" + jobAttribute.name + "}#";
                jobTemplateContent = jobTemplateContent
                        .replace(tmpPlaceHolder, jobAttribute.value!=null?jobAttribute.value:"")
                        .replace("\n", "\n");
            }

            workflowFileContent += jobTemplateContent;

        }

        UsernamePasswordCredentialsProvider credentialsProvider = new UsernamePasswordCredentialsProvider(
                deploymentInfo.ghUser.username,
                deploymentInfo.ghUser.getToken()
        );


        githubService.pushFilesToRepository(
            new File("/Users/raphaelablinger/Documents/Development/Deploy/workdir/"),
            credentialsProvider,
            deploymentInfo.repository,
            new File("/generated/.github/workflows/build.yml"),
            workflowFileContent
        );

        return "";
    }

    public List<DeploymentInfo> fromUser(String username){

        List<DeploymentInfo> resultList = DeploymentInfo.find("ghUser.username", username).list();

        return resultList;

    }

}
