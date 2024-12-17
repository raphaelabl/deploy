package at.raphael.boundary;

import at.raphael.entity.License;
import at.raphael.entity.UserConnector;
import at.raphael.entity.UserOrder;
import at.raphael.entity.dto.NewLicenseDto;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Response;

import java.util.Date;

@Path("license")
public class LicenseResource {

    @Path("add")
    @POST
    @Transactional
    public Response newLicense(NewLicenseDto newLicenseDto) {

        if(newLicenseDto.ghUserName == null || newLicenseDto.ghUserName.isEmpty()) {

            return Response.status(Response.Status.BAD_REQUEST).build();
        }

        UserConnector user = UserConnector.find("username", newLicenseDto.ghUserName).firstResult();

        if(user == null){
            user = new UserConnector();
            user.username = newLicenseDto.ghUserName;
            user.persist();
        }

        for(int singleModule : newLicenseDto.module) {
            Date activeUntil = newLicenseDto.orderDate;
            activeUntil.setYear(activeUntil.getYear() + 1);
            License license = new License(activeUntil, singleModule);
            license.persist();

            user.userData.licenses.add(license);

            UserOrder userOrder = new UserOrder();

            userOrder.date = newLicenseDto.orderDate;
            userOrder.module = singleModule;
            userOrder.price = newLicenseDto.pricePayed;

            userOrder.persist();

            user.userData.orders.add(userOrder);
        }

        return Response.ok().build();
    }


}
