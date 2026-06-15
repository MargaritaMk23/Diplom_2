package order;

import model.Order;
import model.User;
import base.BaseTest;
import client.OrderClient;
import client.UserClient;
import constants.IngredientsData;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Test;
import utils.UserGenerator;

import java.util.List;

import static org.apache.http.HttpStatus.*;
import static org.hamcrest.CoreMatchers.equalTo;

public class CreateOrderTest extends BaseTest {

    private final UserClient userClient = new UserClient();
    private final OrderClient orderClient = new OrderClient();

    private String accessToken;

    @Test
    @DisplayName("Создание заказа с авторизацией")
    @Description("Проверка успешного создания заказа авторизованным пользователем")
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
                .statusCode(SC_OK)
                .body("success", equalTo(true));
    }

    @Test
    @DisplayName("Создание заказа без авторизации")
    @Description("Проверка успешного создания заказа без авторизации")
    public void createOrderWithoutAuthorizationSuccess() {

        Order order = new Order(List.of(
                IngredientsData.BUN,
                IngredientsData.SAUCE
        ));

        Response response =
                orderClient.createOrderWithoutAuth(order);

        response.then()
                .statusCode(SC_OK)
                .body("success", equalTo(true));
    }

    @Test
    @DisplayName("Создание заказа без ингредиентов")
    @Description("Проверка ошибки при создании заказа без ингредиентов")
    public void createOrderWithoutIngredientsFailed() {

        Order order = new Order();

        Response response =
                orderClient.createOrderWithoutAuth(order);

        response.then()
                .statusCode(SC_BAD_REQUEST)
                .body("success", equalTo(false));
    }

    @Test
    @DisplayName("Создание заказа с неверным хешем ингредиента")
    @Description("Проверка ошибки при создании заказа с неверным хешем ингредиента")
    public void createOrderWithWrongIngredientHashFailed() {

        Order order = new Order(List.of(
                IngredientsData.WRONG_HASH
        ));

        Response response =
                orderClient.createOrderWithoutAuth(order);

        response.then()
                .statusCode(SC_INTERNAL_SERVER_ERROR);
    }

    @After
    public void tearDown() {

        if (accessToken != null) {
            userClient.deleteUser(accessToken);
        }
    }
}