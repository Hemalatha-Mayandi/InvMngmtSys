package org.example.stepdefs;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import org.example.domain.AuthResponse;
import org.example.utils.ApiUtils;

import java.util.Map;

public class ShopInventoryApiRegressionStepDef {

    private final ApiUtils apiUtils = new ApiUtils();

    @Step
    public AuthResponse generateAuthToken() {
        Response authToken = apiUtils.getAuthToken();

        return authToken.as(AuthResponse.class);
    }

    @Step
    public Response fetchOrderResponse(Map<String, Object> data) {
        Response response = apiUtils.ordersRequest(data);
        return response;
    }
    
    @Step
    public Response fetchProductStockDetailsResponse(String productId){
        return apiUtils.retrieveProductsStockDetailsRequest(productId);
    }

    @Step
    public Response fetchProductInStock() {

        int currentStock =0;
        Response response = null;
        while(currentStock == 0 ) {
            String randomProductIdfromProducts = apiUtils.getRandomProductIdfromProducts();
            System.out.println(randomProductIdfromProducts + " Random");
            /**Check the stock **/
            response = fetchProductStockDetailsResponse(randomProductIdfromProducts);
            System.out.println(response.body().prettyPrint());
            currentStock = response.body().jsonPath().get("currentStock");

        }
        return response;
    }

}
