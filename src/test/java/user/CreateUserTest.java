package user;

import model.User;
import base.BaseTest;
import client.UserClient;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.Test;

import static org.apache.http.HttpStatus.SC_FORBIDDEN;
import static org.hamcrest.CoreMatchers.equalTo;

public class CreateUserTest extends BaseTest {

    private final UserClient userClient = new UserClient();

    @Test
    @DisplayName("Создание пользователя без email")
    @Description("Проверка ошибки при отсутствии email")
    public void createUserWithoutEmailFailed() {

        User user = new User(
                "",
                "123456",
                "Margo"
        );

        Response response = userClient.createUser(user);

        response.then()
                .statusCode(SC_FORBIDDEN)
                .body("message",
                        equalTo("Email, password and name are required fields"));
    }

    @Test
    @DisplayName("Создание пользователя без password")
    @Description("Проверка ошибки при отсутствии password")
    public void createUserWithoutPasswordFailed() {

        User user = new User(
                "test@mail.com",
                "",
                "Margo"
        );

        Response response = userClient.createUser(user);

        response.then()
                .statusCode(SC_FORBIDDEN)
                .body("message",
                        equalTo("Email, password and name are required fields"));
    }

    @Test
    @DisplayName("Создание пользователя без name")
    @Description("Проверка ошибки при отсутствии name")
    public void createUserWithoutNameFailed() {

        User user = new User(
                "test@mail.com",
                "123456",
                ""
        );

        Response response = userClient.createUser(user);

        response.then()
                .statusCode(SC_FORBIDDEN)
                .body("message",
                        equalTo("Email, password and name are required fields"));
    }
}