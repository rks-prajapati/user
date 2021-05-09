package com.test.user;

import com.test.user.exception.NoUserFoundException;
import com.test.user.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static com.test.user.UserObjectMother.createDefaultUser;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserControllerTest {

    @Mock
    private UserService userService;

    private UserController userController;

    @BeforeEach
    public void setUp() {
        userController = new UserController(userService);
    }

    @Test
    public void findUserShouldReturnUserResponseIfUserExists() {
        String userId = "1111";
        User testUser = createDefaultUser();
        when(userService.findUserById(userId)).thenReturn(testUser);
        ResponseEntity<User> userResponse = userController.retrieveCustomer(userId);

        assertThat(userResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(userResponse.getBody()).isEqualTo(testUser);
    }

    @Test
    public void findUserShouldThrowExceptionIfNoUserFound() {
        String userId = "1111";
        when(userService.findUserById(userId)).thenThrow(new NoUserFoundException("No user found"));

        NoUserFoundException exception = assertThrows(NoUserFoundException.class,
                () -> userController.retrieveCustomer(userId));
        assertThat(exception.getMessage()).isEqualTo("No user found");
    }

    @Test
    public void updateUserShouldReturnUpdatedUser() {
        String userId = "1111";
        User user = createDefaultUser();
        when(userService.updateUser(userId, user)).thenReturn(user);

        ResponseEntity<User> userResponse = userController.updateUser(userId, user);

        assertThat(userResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(userResponse.getBody()).isEqualTo(user);
    }

    @Test
    public void updateUserShouldThrowExceptionIfNoUserFound() {
        String userId = "1111";
        User user = createDefaultUser();

        when(userService.updateUser(userId, user)).thenThrow(new NoUserFoundException("No user found"));

        NoUserFoundException exception = assertThrows(NoUserFoundException.class,
                () -> userController.updateUser(userId, user));
        assertThat(exception.getMessage()).isEqualTo("No user found");
    }
}