package user;

import model.User;
import base.BaseTest;
import client.UserClient;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Test;
import utils.UserGenerator;

import static org.hamcrest.CoreMatchers.equalTo;

public class LoginUserTest extends BaseTest {

    private final UserClient userClient = new UserClient();

    private String accessToken;

    @Test
    @DisplayName("Логин существующего пользователя")
    public void loginWithValidCredentialsSuccess() {

        User user = UserGenerator.getRandomUser();

        Response createResponse = userClient.createUser(user);

        accessToken = createResponse
                .body()
                .jsonPath()
                .getString("accessToken");

        User loginUser = new User(
                user.getEmail(),
                user.getPassword()
        );

        Response loginResponse = userClient.loginUser(loginUser);

        loginResponse.then()
                .statusCode(200)
                .body("success", equalTo(true));
    }

    @Test
    @DisplayName("Логин с неверными данными")
    public void loginWithWrongCredentialsFailed() {

        User user = new User(
                "wrong@mail.com",
                "wrongPassword"
        );

        Response response = userClient.loginUser(user);

        response.then()
                .statusCode(401)
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
