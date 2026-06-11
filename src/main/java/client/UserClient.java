package client;

import model.User;
import io.qameta.allure.Step;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class UserClient extends BaseClient {

    @Step("Создание пользователя")
    public Response createUser(User user) {

        return given()
                .spec(getSpec())
                .filter(new AllureRestAssured())
                .body(user)
                .when()
                .post("/api/auth/register");
    }

    @Step("Логин пользователя")
    public Response loginUser(User user) {

        return given()
                .spec(getSpec())
                .filter(new AllureRestAssured())
                .body(user)
                .when()
                .post("/api/auth/login");
    }

    @Step("Удаление пользователя")
    public Response deleteUser(String token) {

        return given()
                .spec(getSpec())
                .filter(new AllureRestAssured())
                .header("Authorization", token)
                .when()
                .delete("/api/auth/user");
    }
}
