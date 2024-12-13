package at.raphael.boundary;

import at.raphael.control.DeployService;
import at.raphael.entity.DeploymentInfo;
import io.quarkus.logging.Log;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.core.Response;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Path("deploy")
public class DeployResource {

    @Inject
    DeployService deployService;


    @POST
    @Path("/post/deploymentInfo")
    public Response postDeploymentInfo(DeploymentInfo deploymentInfo) {
        DeploymentInfo response = this.deployService.persistDeployment(deploymentInfo);
        Log.info(deploymentInfo.deploymentName);
        this.deployService.autoImportModules(response);
        return Response.ok(response).build();
    }

    @GET
    @Path("allFromUser/{username}")
    public Response allFromUser(@PathParam("username") String username) {
        return Response.ok(deployService.fromUser(username)).build();
    }

    @POST
    @Path("uploadToGithub")
    public Response deployToGit(DeploymentInfo deploymentInfo){
        List<String> errors = new ArrayList<>();
        errors.add(deployService.deploy(deploymentInfo));

        errors = errors.stream().filter(element -> !element.isEmpty()).collect(Collectors.toList());

        if(!errors.isEmpty()) {
            return Response.status(Response.Status.BAD_REQUEST).entity(errors).build();
        }
        return Response.ok().build();
    }

}
