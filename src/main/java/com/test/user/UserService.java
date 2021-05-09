package com.test.user;

import com.test.user.exception.NoUserFoundException;
import com.test.user.model.Address;
import com.test.user.model.User;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public User findUserById(String userId) {
        Optional<User> user = userRepository.findByEmployeeId(userId);
        if(user.isPresent()) {
            return user.get();
        }
        throw new NoUserFoundException("No user found for userId " + userId);
    }

    public User updateUser(String userId, User updatedUser) {
        Optional<User> existingUser = userRepository.findByEmployeeId(userId);
        if(existingUser.isPresent()) {
            User user = existingUser.get();
            user.setFirstName(updatedUser.getFirstName());
            user.setLastName(updatedUser.getLastName());
            user.setTitle(updatedUser.getTitle());
            user.setGender(updatedUser.getGender());

            Address address = user.getAddress();
            Address updatedAddress = updatedUser.getAddress();
            address.setStreet(updatedAddress.getStreet());
            address.setState(updatedAddress.getState());
            address.setCity(updatedAddress.getCity());
            address.setPostcode(updatedAddress.getPostcode());
            return userRepository.save(user);
        }
        throw new NoUserFoundException("No user found for userId " + userId);
    }
}
