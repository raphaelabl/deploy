package at.raphael.boundary;

import at.raphael.control.DeployService;
import at.raphael.entity.DeploymentInfo;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Response;
import org.eclipse.jgit.api.errors.GitAPIException;

import java.io.IOException;
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
        return Response.ok(this.deployService.persistDeployment(deploymentInfo)).build();
    }

    @GET
    @Path("allFromUser/{username}")
    public Response allFromUser(@PathParam("username") String username) {
        return Response.ok(deployService.fromUser(username)).build();
    }

    @GET
    @Path("withGitfromId/{id}")
    public Response deployToGit(@PathParam("id") Long id) throws IOException, GitAPIException {
        DeploymentInfo deploymentInfo = DeploymentInfo.findById(id);

        List<String> errors = new ArrayList<>();
        errors.add(deployService.deploy(deploymentInfo));

        errors = errors.stream().filter(element -> !element.isEmpty()).collect(Collectors.toList());

        if(!errors.isEmpty()) {
            return Response.status(Response.Status.BAD_REQUEST).entity(errors).build();
        }
        return Response.ok().build();
    }

}
