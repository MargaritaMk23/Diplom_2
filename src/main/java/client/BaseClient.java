package client;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;

public class BaseClient {

    protected RequestSpecification getSpec() {

        return new RequestSpecBuilder()
                .setContentType(ContentType.JSON)
                .build();
    }
}