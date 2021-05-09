package com.test.user;

import com.test.user.model.Address;
import com.test.user.model.User;

public class UserObjectMother {
    public static User createDefaultUser() {
        User user = new User();
        user.setEmployeeId("1111");
        user.setTitle("Mr");
        user.setFirstName("FirstName");
        user.setLastName("LastName");
        user.setGender("Male");

        Address address = new Address();
        address.setId(1);
        address.setStreet("Street Name");
        address.setCity("City");
        address.setState("State");
        address.setPostcode("2765");

        user.setAddress(address);
        return user;
    }
}
