package com.siku.storz;

import com.siku.storz.dto.ProductDTO;
import com.siku.storz.service.ProductService;
import io.restassured.RestAssured;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.testng.AbstractTransactionalTestNGSpringContextTests;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.core.Is.is;

@SpringBootTest(
        classes = StorzApplication.class,
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
public class ProductControllerTest extends AbstractTransactionalTestNGSpringContextTests {

    private static final String PRODUCT_NAME = "prod1";

    @LocalServerPort
    private int port;

    @Autowired
    private ProductService productService;

    @BeforeMethod
    public void init() {
        RestAssured.port = port;
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails(LogDetail.ALL);
    }

    @BeforeClass
    public void initializeProduct() {
        // the saved section will init some products
        ProductDTO fstProdDTO = new ProductDTO("prod1", 10);
        productService.save(fstProdDTO);
    }

    @Test
    public void givenTheContentTypeIsCorrect_WhenGettingAProduct_ThenAllGood() {
        given().auth().form("manager", "password")
                .accept(ContentType.JSON).
                when()
                .get("/product/1").
                then()
                .statusCode(HttpStatus.OK.value())
                .body("name", is(PRODUCT_NAME));
    }

    @Test
    public void givenTheContentTypeIsCorrect_WhenGettingAllProducts_ThenAllGood() {
        given().auth().form("manager", "password")
                .accept(ContentType.JSON).
                when()
                .get("/product").
                then()
                .statusCode(HttpStatus.OK.value())
                .body("$.size", is(1)) // the response array size is 10
                .body("[0].name", is(PRODUCT_NAME));
    }

    // a sample of using a dataProvider
    @Test(dataProvider = "dataProvider")
    public void givenTheContentTypeIsCorrect_WhenUsingADataProvider_ThenAllGood(final String productId, final int statusCode) {
        given().auth().form("manager", "password")
                .accept(ContentType.JSON).urlEncodingEnabled(false).
                when()
                .get("/product/" + productId).
                then()
                .statusCode(statusCode);
    }

    @DataProvider(name = "dataProvider", parallel = true)
    public Object[][] dataProvider() {
        return new Object[][]{
                {"1", HttpStatus.OK.value()}, // productId and status code
                {"13", HttpStatus.BAD_REQUEST.value()}
        };
    }
}
