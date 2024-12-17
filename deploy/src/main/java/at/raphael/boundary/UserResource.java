package at.raphael.boundary;

import at.raphael.entity.UserConnector;
import at.raphael.entity.UserData;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.Response;

@Path("user")
public class UserResource{

    @POST
    @Path("/dataToConnector")
    @Transactional
    public Response newUser(UserConnector user){

        if(user.id != null && user.id != 0){
            UserConnector persisted = UserConnector.findById(user.id);

            if(persisted != null){
                if(user.userData.id != null && user.userData.id != 0){
                    UserData ud = UserData.findById(user.userData.id);
                    ud.updateEntity(user.userData);
                }else{
                    user.userData.id = null;
                    user.userData.persist();
                    persisted.userData = user.userData;
                }
                return Response.ok(persisted).build();
            }
            return null;
        }
        user.persist();
        return Response.ok(user).build();
    }

    @GET
    @Path("/withGithubUsername")
    public Response getViaUserName(@QueryParam("username") String userName){
        UserConnector user = UserConnector.find("username", userName).firstResult();

        if(user != null){
            user.token = "";
            user.encryptedToken = "";
            return Response.ok(user).build();
        }
        return Response.ok().build();
    }


}
