package at.raphael.entity;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;

import java.util.List;

@Entity
public class UserData extends PanacheEntity {

    public String firstName;
    public String lastName;
    public String email;
    public String phoneNumber;

    //Address
    public String place;
    public String houseNumber;
    public String addressAddition;
    public String zipCode;
    public String cityName;
    public String countryName;

    @OneToMany
    public List<License> licenses;

    @OneToMany
    public List<UserOrder> orders;


    public UserData updateEntity(UserData newEntity){
        this.firstName = newEntity.firstName;
        this.lastName = newEntity.lastName;
        this.email = newEntity.email;
        this.phoneNumber = newEntity.phoneNumber;
        this.place = newEntity.place;
        this.houseNumber = newEntity.houseNumber;
        this.addressAddition = newEntity.addressAddition;
        this.zipCode = newEntity.zipCode;
        this.cityName = newEntity.cityName;
        this.countryName = newEntity.countryName;
        return this;
    }

    public UserData() {
    }
}
