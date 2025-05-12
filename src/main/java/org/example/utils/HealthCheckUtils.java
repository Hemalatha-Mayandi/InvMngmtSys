package org.example.utils;

import io.qameta.allure.Step;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;
import org.example.domain.Status;

public class HealthCheckUtils {
    private static final PropertyUtils propertyUtils = new PropertyUtils("api.properties");

    @Step
    public Status getDbStatus() {

        Status status = new Status();
        status.setStatus("OK");
        status.setDbStatus("Connected");

        RequestSpecification reqSpec = new RequestSpecBuilder().setBaseUri(propertyUtils.getProperty("api.base.uri"))
                .setContentType(ContentType.JSON).build();

        ValidatableResponse response = HttpClientAllureWrapper.restAssuredClient(requestSpecification -> requestSpecification
                        .given()
                        .spec(reqSpec)
                        .body(status)
                        .basePath(propertyUtils.getProperty("health.check"))
                        .get())
                .then().statusCode(200);

        return response.extract().response().as(Status.class);
    }
}
