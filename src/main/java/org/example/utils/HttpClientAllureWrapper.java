package org.example.utils;

import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import java.util.function.Function;

import static io.restassured.RestAssured.with;
import io.qameta.allure.restassured.AllureRestAssured;

public class HttpClientAllureWrapper {
    private HttpClientAllureWrapper() {
    }

    public static Response restAssuredClient(Function<RequestSpecification, Response> request) {
        return request.apply(restAssuredSpecificationWithAllure());
    }

    private static RequestSpecification restAssuredSpecificationWithAllure() {
        return with().filter(new AllureRestAssured()).log().all();
//        return with().filter(new AllureRestAssured()).log().ifValidationFails();
    }
}
