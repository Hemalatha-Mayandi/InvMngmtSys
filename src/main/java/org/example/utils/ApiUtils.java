package org.example.utils;

import io.qameta.allure.Step;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.example.domain.AuthReq;
import org.example.domain.AuthResponse;
import org.example.domain.Order;
import org.example.domain.Products;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class ApiUtils {

    private static final PropertyUtils propertyUtils = new PropertyUtils("api.properties");

    @Step
    public Response getAuthToken() {

        AuthReq auth = new AuthReq();
        auth.setUsername(propertyUtils.getProperty("username"));
        auth.setPassword(propertyUtils.getProperty("password"));

        RequestSpecification reqSpec = new RequestSpecBuilder().setBaseUri(propertyUtils.getProperty("api.base.uri"))
                .setContentType(ContentType.JSON).build();

        return HttpClientAllureWrapper.restAssuredClient(requestSpecification -> requestSpecification
                .given()
                .spec(reqSpec)
                .body(auth)
                .basePath(propertyUtils.getProperty("auth.token"))
                .post());
    }

    @Step
    public AuthResponse generateAuthToken() {
        Response authToken = getAuthToken();

        return authToken.as(AuthResponse.class);
    }

    @Step
    public Products[] getProductsRequest() {
        RequestSpecification reqSpec = new RequestSpecBuilder().setBaseUri(propertyUtils.getProperty("api.base.uri"))
                .setContentType(ContentType.JSON).build();

        return HttpClientAllureWrapper.restAssuredClient(requestSpecification -> requestSpecification
                        .given()
                        .spec(reqSpec)
                        .basePath(propertyUtils.getProperty("inventory.management.system.product"))
                        .get())
                .then().extract().response().as(Products[].class);
    }

    @Step
    public String getRandomProductIdfromProducts() {
        Products[] productsRequest = getProductsRequest();

        if (productsRequest != null && productsRequest.length > 0) {
            List<Products> productList = Arrays.asList(productsRequest);
            int randomIndex = new Random().nextInt(productList.size());
            return productList.get(randomIndex).getProductId();
        }

        return null;
    }

    @Step
    public Response getProductsByProductIdRequest(String productId) {
        RequestSpecification reqSpec = new RequestSpecBuilder().setBaseUri(propertyUtils.getProperty("api.base.uri"))
                .setContentType(ContentType.JSON).build();

        return HttpClientAllureWrapper.restAssuredClient(requestSpecification -> requestSpecification
                        .given()
                        .spec(reqSpec)
                        .basePath(propertyUtils.getProperty("inventory.management.system.product.by.id"))
                        .pathParams("productId", productId)
                        .get())
                .then().extract().response();
    }

    @Step
    public Response postProductsRequest(Map<String, Object> data) {

        String products = String.format(
                "{\n" +
                        "  \"name\": \"%s\",\n" +
                        "  \"productType\": \"%s\",\n" +
                        "  \"price\": %.2f,\n" +
                        "  \"quantity\": %d\n" +
                        "}",
                data.get("name"),
                data.get("productType"),
                ((Number) data.get("price")).doubleValue(),
                ((Number) data.get("quantity")).intValue());

        RequestSpecification reqSpec = new RequestSpecBuilder().setBaseUri(propertyUtils.getProperty("api.base.uri"))
                .setContentType(ContentType.JSON).build();

        return HttpClientAllureWrapper.restAssuredClient(requestSpecification -> requestSpecification
                .given()
                .spec(reqSpec)
                .body(products)
                .header("authorization", generateAuthToken().getToken())
                .basePath(propertyUtils.getProperty("inventory.management.system.product"))
                .post());
    }

    @Step
    public Products putProductsRequest(Map<String, Object> data) {
        String products = String.format(
                "{\n" +
                        "  \"name\": \"%s\",\n" +
                        "  \"productType\": \"%s\",\n" +
                        "  \"price\": %.2f,\n" +
                        "  \"quantity\": %d\n" +
                        "}",
                data.get("name"),
                data.get("productType"),
                ((Number) data.get("price")).doubleValue(),
                ((Number) data.get("quantity")).intValue());

        RequestSpecification reqSpec = new RequestSpecBuilder().setBaseUri(propertyUtils.getProperty("api.base.uri"))
                .setContentType(ContentType.JSON).build();

        return HttpClientAllureWrapper.restAssuredClient(requestSpecification -> requestSpecification
                        .given()
                        .spec(reqSpec)
                        .body(products)
                        .header("authorization", generateAuthToken().getToken())
                        .pathParams("productId", data.get("name"))
                        .basePath(propertyUtils.getProperty("inventory.management.system.product.by.id"))
                        .put())
                .then().extract().response().as(Products.class);
    }

    @Step
    public Response deleteProductsRequest(String productId) {
        RequestSpecification reqSpec = new RequestSpecBuilder().setBaseUri(propertyUtils.getProperty("api.base.uri"))
                .setContentType(ContentType.JSON).build();

        return HttpClientAllureWrapper.restAssuredClient(requestSpecification -> requestSpecification
                        .given()
                        .spec(reqSpec)
                        .header("authorization", generateAuthToken().getToken())
                        .pathParams("productId", productId)
                        .basePath(propertyUtils.getProperty("inventory.management.system.product.by.id"))
                        .delete())
                .then().extract().response();
    }

    @Step
    public Response ordersRequest(Map<String, Object> data) {

        String orderRequest = String.format(
                "{\n" +
                        "  \"orderType\": \"%s\",\n" +
                        "  \"productId\": \"%s\",\n" +
                        "  \"quantity\": %d\n" +
                        "}",
                data.get("orderType"),
                data.get("productId"),
                data.get("quantity")
        );

        return HttpClientAllureWrapper.restAssuredClient(requestSpecification -> requestSpecification
                .baseUri(propertyUtils.getProperty("api.base.uri"))
                .body(orderRequest)
                .basePath(propertyUtils.getProperty("create.orders"))
                .header("authorization", generateAuthToken().getToken())
                .header("content-type", "application/json")
                .header("accept", "application/json")
                .post());
    }

    @Step
    public Response retrieveProductsStockDetailsRequest(String productId) {

        return HttpClientAllureWrapper.restAssuredClient(requestSpecification -> requestSpecification
                .baseUri(propertyUtils.getProperty("api.base.uri"))
                .pathParams("productId", productId)
                .basePath(propertyUtils.getProperty("get.products.stock.details"))
                .header("authorization", generateAuthToken().getToken())
                .header("content-type", "application/json")
                .header("accept", "application/json")
                .get());

    }
}
