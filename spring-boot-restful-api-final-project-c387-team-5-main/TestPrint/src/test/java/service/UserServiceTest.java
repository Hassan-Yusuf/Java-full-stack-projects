package service;

import com.recipro.TestApplicationConfiguration;
import com.recipro.dao.UserDao;
import com.recipro.model.User;
import com.recipro.service.UserServiceImpl;
import com.recipro.dao.UserVerificationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = TestApplicationConfiguration.class)
@ActiveProfiles("test")
public class UserServiceTest {

    @Autowired
    private UserDao userDao;

    @Autowired
    private UserServiceImpl userService;

    @BeforeEach
    public void setUp() {
        // Clear the database before each test
        List<String> users = userDao.getAllUsers();
        for (String userId : users) {
            userDao.deleteUser(userId);
        }
    }

    @Test
    public void testVerifyUser() {
        // Create a user with the correct user ID and password
        User user = new User();
        user.setUserId("123");
        user.setPassword("password");

        // Add the user to the database
        try {
            userService.addUser(user);
        } catch (UserVerificationException e) {
            fail("Unexpected exception: " + e.getMessage());
        }

        // Now verify the user
        try {
            String result = userService.verifyUser(user);
            assertEquals("123", result);
        } catch (UserVerificationException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testAddUser() throws UserVerificationException {
        User user = new User();
        user.setUserId("124");
        user.setPassword("password");

        String userId = userService.addUser(user);
        assertNotNull(userId);
        assertEquals("124", userId);

        User createdUser = userDao.getUserById("124");
        assertTrue(new BCryptPasswordEncoder().matches("password", createdUser.getPassword()));
    }

    @Test
    public void testDeleteUser() {
        User user = new User();
        user.setUserId("125");
        user.setPassword("password");

        try {
            userService.addUser(user);
        } catch (UserVerificationException e) {
            fail("Unexpected exception: " + e.getMessage());
        }

        userService.deleteUser(user);

        assertNull(userDao.getUserById("125"));
    }

    @Test
    public void testAddUserWithNullFields() {
        User user = new User();
        user.setUserId(null);
        user.setPassword("password");

        UserVerificationException exception = assertThrows(UserVerificationException.class, () -> {
            userService.addUser(user);
        });

        assertEquals("User ID or password cannot be null", exception.getMessage());
    }

    @Test
    public void testAddUserWithNullPassword() {
        User user = new User();
        user.setUserId("126");
        user.setPassword(null);

        UserVerificationException exception = assertThrows(UserVerificationException.class, () -> {
            userService.addUser(user);
        });

        assertEquals("User ID or password cannot be null", exception.getMessage());
    }

    @Test
    public void testAddUserThatAlreadyExists() {
        User user1 = new User();
        user1.setUserId("existingUser");
        user1.setPassword("password");

        try {
            userService.addUser(user1);
        } catch (UserVerificationException e) {
            fail("Unexpected exception: " + e.getMessage());
        }

        User user2 = new User();
        user2.setUserId("existingUser");
        user2.setPassword("newPassword");

        UserVerificationException exception = assertThrows(UserVerificationException.class, () -> {
            userService.addUser(user2);
        });

        assertEquals("User with ID existingUser already exists.", exception.getMessage());
    }

    @Test
    public void testDeleteNonExistentUser() {
        User nonExistentUser = new User();
        nonExistentUser.setUserId("nonExistentUser");
        nonExistentUser.setPassword("password");

        // Ensure no exception is thrown
        assertDoesNotThrow(() -> {
            userService.deleteUser(nonExistentUser);
        });
    }

    @Test
    public void testVerifyNonExistentUser() {
        User nonExistentUser = new User();
        nonExistentUser.setUserId("nonExistentUser");
        nonExistentUser.setPassword("password");

        UserVerificationException exception = assertThrows(UserVerificationException.class, () -> {
            userService.verifyUser(nonExistentUser);
        });

        assertEquals("User id or password is incorrect", exception.getMessage());
    }

    @Test
    public void testDeleteUserAndEnsureDeleted() throws UserVerificationException {
        User user = new User();
        user.setUserId("deletableUser");
        user.setPassword("password");
        userService.addUser(user);

        userService.deleteUser(user);

        assertNull(userDao.getUserById("deletableUser"));
    }

    @Test
    public void testGetUserById() throws UserVerificationException {
        User user = new User();
        user.setUserId("retrieveUser");
        user.setPassword("password");
        userService.addUser(user);

        User retrievedUser = userDao.getUserById("retrieveUser");
        assertNotNull(retrievedUser);
        assertEquals("retrieveUser", retrievedUser.getUserId());
    }

    @Test
    public void testGetUserByIdNonExistent() {
        User retrievedUser = userDao.getUserById("nonExistentUser");
        assertNull(retrievedUser);
    }
}
