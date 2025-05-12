package regression;

import io.restassured.response.Response;
import org.apache.http.HttpStatus;
import org.example.domain.AuthResponse;
import org.example.domain.Order;
import org.example.domain.ProductsInStock;
import org.example.stepdefs.ShopInventoryApiRegressionStepDef;
import org.example.utils.ApiUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class ShopInventoryApiTest {

    private final ApiUtils apiutils = new ApiUtils();
    private final ShopInventoryApiRegressionStepDef shopInventoryApiRegressionStepDef = new ShopInventoryApiRegressionStepDef();


    private static final Logger logger = LoggerFactory.getLogger(ShopInventoryApiTest.class);

    @Test
    @DisplayName("testAuthTokenResponse")
    public void testAuthTokenResponse() {
        AuthResponse authResponse = shopInventoryApiRegressionStepDef.generateAuthToken();

        assertEquals(HttpStatus.SC_OK, apiutils.getAuthToken().getStatusCode());
        assertNotNull(authResponse.getToken(), "Auth token should not be null");
    }

    @Test
    @DisplayName("testRetrieveStockDetailsOfProduct")
    public void testRetrieveStockDetailsOfProduct() {
        String randomProductIdfromProducts = apiutils.getRandomProductIdfromProducts();

        Response productsByProductIdRequest = apiutils.getProductsByProductIdRequest(randomProductIdfromProducts);

        assertEquals(HttpStatus.SC_OK, productsByProductIdRequest.getStatusCode());
        assertEquals(randomProductIdfromProducts, productsByProductIdRequest.getBody().jsonPath().get("productId"));
    }

    @Test
    @DisplayName("testAddProduct")
    public void testAddProduct() {
        String randomProductIdfromProducts = apiutils.getRandomProductIdfromProducts();

        logger.info(randomProductIdfromProducts);

        Map<String, Object> addParams = new HashMap<>();
        addParams.put("name", randomProductIdfromProducts);
        addParams.put("productType", "games");
        addParams.put("quantity", 19.99);
        addParams.put("price", 10);

        Response response = apiutils.postProductsRequest(addParams);

        assertEquals(HttpStatus.SC_CREATED, response.getStatusCode());

    }

    @Test
    @DisplayName("testUpdateProduct")
    public void testUpdateProduct() {
        String randomProductIdfromProducts = apiutils.getRandomProductIdfromProducts();

        logger.info(randomProductIdfromProducts);
        Map<String, Object> addParams = new HashMap<>();
        addParams.put("name", randomProductIdfromProducts);
        addParams.put("productType", "games");
        addParams.put("quantity", 19.99);
        addParams.put("price", 10);

        Response response = apiutils.postProductsRequest(addParams);

        apiutils.putProductsRequest(addParams);
        assertEquals(HttpStatus.SC_CREATED, response.getStatusCode());
    }

    @Test
    @DisplayName("testDeleteProduct")
    public void testDeleteProduct() {

        String randomProductIdfromProducts = apiutils.getRandomProductIdfromProducts();

        logger.info(randomProductIdfromProducts);

        Response response = apiutils.deleteProductsRequest(randomProductIdfromProducts);

        assertEquals(HttpStatus.SC_OK, response.getStatusCode());
        assertEquals("Product removed", response.then().extract().jsonPath().get("message").toString());
    }

    @Test
    @DisplayName("testBuysProduct")
    public void testBuysProduct() {
        String randomProductIdfromProducts = apiutils.getRandomProductIdfromProducts();

        logger.info(randomProductIdfromProducts);

        Map<String, Object> addParams = new HashMap<>();
        addParams.put("orderType", "buy");
        addParams.put("productId", randomProductIdfromProducts);
        addParams.put("quantity", 2);

        Response response = shopInventoryApiRegressionStepDef.fetchOrderResponse(addParams);
        Order order = response.as(Order.class);
        assertEquals("true", order.getSuccess());
        assertEquals(HttpStatus.SC_CREATED, response.getStatusCode());
        assertEquals(order.getProductId(), randomProductIdfromProducts);
        assertEquals(order.getQuantity(), addParams.get("quantity"));
        assertEquals(order.getOrderType(), addParams.get("orderType"));

        /**Check the stock **/
        Response buyProductStock = shopInventoryApiRegressionStepDef
                .fetchProductStockDetailsResponse(randomProductIdfromProducts);
        System.out.println(buyProductStock.prettyPrint());
        buyProductStock.body().as(Order.class);

        assertEquals(HttpStatus.SC_OK, buyProductStock.getStatusCode());
    }

    @Test
    public void testSellProduct() {
        Response productInStock = shopInventoryApiRegressionStepDef.fetchProductInStock();

        ProductsInStock productsInStock = productInStock.as(ProductsInStock.class);

        Map<String, Object> addParams = new HashMap<>();
        addParams.put("orderType", "sell");
        addParams.put("productId", productsInStock.getProductId());
        addParams.put("quantity", productsInStock.getCurrentStock());

        Response response = shopInventoryApiRegressionStepDef.fetchOrderResponse(addParams);
        Order order = response.as(Order.class);
        assertEquals(HttpStatus.SC_CREATED, response.getStatusCode());
        assertEquals(order.getProductId(), productsInStock.getProductId());
        assertEquals(order.getQuantity(), productsInStock.getCurrentStock());
        assertEquals(order.getOrderType(), addParams.get("orderType"));
    }
}
