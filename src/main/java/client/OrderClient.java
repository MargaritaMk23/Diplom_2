package client;

import model.Order;
import io.qameta.allure.Step;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class OrderClient extends BaseClient {

    @Step("Создание заказа с авторизацией")
    public Response createOrderWithAuth(Order order, String token) {

        return given()
                .spec(getSpec())
                .filter(new AllureRestAssured())
                .header("Authorization", token)
                .body(order)
                .when()
                .post("/api/orders");
    }

    @Step("Создание заказа без авторизации")
    public Response createOrderWithoutAuth(Order order) {

        return given()
                .spec(getSpec())
                .filter(new AllureRestAssured())
                .body(order)
                .when()
                .post("/api/orders");
    }
}
