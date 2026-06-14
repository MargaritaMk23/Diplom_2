package user;

import model.User;
import base.BaseTest;
import client.UserClient;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import utils.UserGenerator;

import static org.apache.http.HttpStatus.*;
import static org.hamcrest.CoreMatchers.equalTo;

public class LoginUserTest extends BaseTest {

    private final UserClient userClient = new UserClient();

    private User user;
    private String accessToken;

    @Before
    public void setUp() {
        user = UserGenerator.getRandomUser();
        userClient.createUser(user);
    }

    @Test
    @DisplayName("Логин с неверным логином")
    @Description("Проверка ошибки при неверном email")
    public void loginWithWrongEmailFailed() {

        User wrongUser = new User(
                "wrong_" + user.getEmail(),
                user.getPassword()
        );

        Response response = userClient.loginUser(wrongUser);

        response.then()
                .statusCode(SC_UNAUTHORIZED)
                .body("message",
                        equalTo("email or password are incorrect"));
    }

    @Test
    @DisplayName("Логин с неверным паролем")
    @Description("Проверка ошибки при неверном password")
    public void loginWithWrongPasswordFailed() {

        User wrongUser = new User(
                user.getEmail(),
                "wrong_" + user.getPassword()
        );

        Response response = userClient.loginUser(wrongUser);

        response.then()
                .statusCode(SC_UNAUTHORIZED)
                .body("message",
                        equalTo("email or password are incorrect"));
    }

    @After
    public void tearDown() {
        if (accessToken != null) {
            userClient.deleteUser(accessToken);
        }
    }
}
