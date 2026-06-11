package utils;

import model.User;

import java.util.UUID;

public class UserGenerator {

    public static User getRandomUser() {

        String email = UUID.randomUUID() + "@yandex.ru";
        String password = "123456";
        String name = "Margo";

        return new User(name, email, password);
    }
}
