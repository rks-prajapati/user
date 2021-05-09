package com.test.user;

import com.test.user.exception.NoUserFoundException;
import com.test.user.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static com.test.user.UserObjectMother.createDefaultUser;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    private UserService userService;

    @Mock
    private UserRepository userRepository;
    private final String userId = "1111";

    @BeforeEach
    public void setUp() {
        userService = new UserService(userRepository);
    }

    @Test
    public void findByUserIdShouldReturnUser() {
        User user = createDefaultUser();
        when(userRepository.findByEmployeeId(userId)).thenReturn(Optional.of(user));

        User actualUser = userService.findUserById(userId);

        assertThat(actualUser).isEqualTo(user);
    }

    @Test
    public void findUserShouldThrowExceptionIfNoUserFound() {
        when(userRepository.findByEmployeeId(userId)).thenReturn(Optional.empty());

        NoUserFoundException exception = assertThrows(NoUserFoundException.class,
                () -> userService.findUserById(userId));
        assertThat(exception.getMessage()).isEqualTo("No user found for userId " + userId);
    }

    @Test
    public void updateUserShouldReturnUpdateUser() {
        User originalUser = createDefaultUser();
        when(userRepository.findByEmployeeId(userId)).thenReturn(Optional.of(originalUser));
        User updatedUser = createDefaultUser();
        updatedUser.setFirstName("NewFirstName");
        updatedUser.setLastName("NewLastName");

        ArgumentCaptor<User> userArgumentCaptor = ArgumentCaptor.forClass(User.class);
        when(userRepository.save(updatedUser)).thenReturn(updatedUser);

        User user = userService.updateUser(userId, updatedUser);

        assertThat(user).isEqualTo(updatedUser);
        verify(userRepository).save(userArgumentCaptor.capture());
        User updatedUserToCompare = userArgumentCaptor.getValue();
        assertThat(updatedUserToCompare.getFirstName()).isEqualTo(updatedUser.getFirstName());
        assertThat(updatedUserToCompare.getLastName()).isEqualTo(updatedUser.getLastName());
    }
}