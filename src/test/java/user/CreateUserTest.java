package user;

import model.User;
import base.BaseTest;
import client.UserClient;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Test;
import utils.UserGenerator;

import static org.hamcrest.CoreMatchers.equalTo;

public class CreateUserTest extends BaseTest {

    private final UserClient userClient = new UserClient();

    private String accessToken;

    @Test
    @DisplayName("Создание уникального пользователя")
    @Description("Проверка успешного создания нового пользователя")
    public void createUniqueUserSuccess() {

        User user = UserGenerator.getRandomUser();

        Response response = userClient.createUser(user);

        accessToken = response
                .body()
                .jsonPath()
                .getString("accessToken");

        response.then()
                .statusCode(200)
                .body("success", equalTo(true));
    }

    @Test
    @DisplayName("Создание уже существующего пользователя")
    public void createExistingUserFailed() {

        User user = UserGenerator.getRandomUser();

        Response firstResponse = userClient.createUser(user);

        accessToken = firstResponse
                .body()
                .jsonPath()
                .getString("accessToken");

        Response secondResponse = userClient.createUser(user);

        secondResponse.then()
                .statusCode(403)
                .body("message",
                        equalTo("User already exists"));
    }

    @Test
    @DisplayName("Создание пользователя без email")
    public void createUserWithoutEmailFailed() {

        User user = new User(
                "Margo",
                "",
                "123456"
        );

        Response response = userClient.createUser(user);

        response.then()
                .statusCode(403)
                .body("message",
                        equalTo("Email, password and name are required fields"));
    }

    @After
    public void tearDown() {

        if (accessToken != null) {
            userClient.deleteUser(accessToken);
        }
    }
}