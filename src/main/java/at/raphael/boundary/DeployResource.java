package at.raphael.boundary;

import at.raphael.control.DeployService;
import at.raphael.entity.Workflow;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
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


    @GET
    @Path("withGitfromId/{id}")
    public Response deployToGit(@PathParam("id") Long id) throws IOException, GitAPIException {
        Workflow w = Workflow.findById(id);

        List<String> errors = new ArrayList<>();
        errors.add(deployService.createWorkflow(w));

        errors = errors.stream().filter(element -> !element.isEmpty()).collect(Collectors.toList());

        if(!errors.isEmpty()) {
            return Response.status(Response.Status.BAD_REQUEST).entity(errors).build();
        }
        return Response.ok().build();
    }

}
