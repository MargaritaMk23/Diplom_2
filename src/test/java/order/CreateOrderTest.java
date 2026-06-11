package order;

import model.Order;
import model.User;
import base.BaseTest;
import client.OrderClient;
import client.UserClient;
import constants.IngredientsData;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Test;
import utils.UserGenerator;

import java.util.List;

import static org.hamcrest.CoreMatchers.equalTo;

public class CreateOrderTest extends BaseTest {

    private final UserClient userClient = new UserClient();
    private final OrderClient orderClient = new OrderClient();

    private String accessToken;

    @Test
    @DisplayName("Создание заказа с авторизацией")
    public void createOrderWithAuthorizationSuccess() {

        User user = UserGenerator.getRandomUser();

        Response createUserResponse =
                userClient.createUser(user);

        accessToken = createUserResponse
                .body()
                .jsonPath()
                .getString("accessToken");

        Order order = new Order(List.of(
                IngredientsData.BUN,
                IngredientsData.SAUCE
        ));

        Response response =
                orderClient.createOrderWithAuth(order, accessToken);

        response.then()
                .statusCode(200)
                .body("success", equalTo(true));
    }

    @Test
    @DisplayName("Создание заказа без авторизации")
    public void createOrderWithoutAuthorizationSuccess() {

        Order order = new Order(List.of(
                IngredientsData.BUN,
                IngredientsData.SAUCE
        ));

        Response response =
                orderClient.createOrderWithoutAuth(order);

        response.then()
                .statusCode(200)
                .body("success", equalTo(true));
    }

    @Test
    @DisplayName("Создание заказа без ингредиентов")
    public void createOrderWithoutIngredientsFailed() {

        Order order = new Order();

        Response response =
                orderClient.createOrderWithoutAuth(order);

        response.then()
                .statusCode(400)
                .body("success", equalTo(false));
    }

    @Test
    @DisplayName("Создание заказа с неверным хешем ингредиента")
    public void createOrderWithWrongIngredientHashFailed() {

        Order order = new Order(List.of(
                IngredientsData.WRONG_HASH
        ));

        Response response =
                orderClient.createOrderWithoutAuth(order);

        response.then()
                .statusCode(500);
    }

    @After
    public void tearDown() {

        if (accessToken != null) {
            userClient.deleteUser(accessToken);
        }
    }
}