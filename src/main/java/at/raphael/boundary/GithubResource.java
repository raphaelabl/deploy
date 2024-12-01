package at.raphael.boundary;

import at.raphael.control.GithubService;
import at.raphael.entity.UserConnector;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("github")
public class GithubResource {
    //Callback Url
    //http://localhost:8080/github/callback


    @Inject
    GithubService githubService;


    @GET
    @Path("/callback")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response handleGitHubCallback(@QueryParam("code") String code){
        try {
            UserConnector userConnector = githubService.getUserData(code);
            
            return Response.ok(userConnector).build();
        } catch (Exception e) {
            return Response.serverError().build();
        }
    }


}
