package com.siku.storz;

import com.siku.storz.dto.ProductDTO;
import com.siku.storz.dto.SectionDTO;
import com.siku.storz.dto.StoreDTO;
import com.siku.storz.service.ProductService;
import com.siku.storz.service.SectionService;
import com.siku.storz.service.StoreService;
import io.restassured.RestAssured;
import io.restassured.authentication.FormAuthConfig;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.testng.AbstractTransactionalTestNGSpringContextTests;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.core.Is.is;

@SpringBootTest(
        classes = StorzApplication.class,
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
public class SectionControllerTest extends AbstractTransactionalTestNGSpringContextTests {
    private static final String SECTION_NAME = "section1";

    @LocalServerPort
    private int port;

    @Autowired
    private SectionService sectionService;

    @Autowired
    private StoreService storeService;

    @BeforeMethod
    public void init() {
        RestAssured.port = port;
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails(LogDetail.ALL);
    }

    @BeforeClass
    public void initializeSection() {

        StoreDTO fstStoreDTO = new StoreDTO(1, "magazin1", "TM", null);
        SectionDTO fstSectionDTO = new SectionDTO(1, "section1", fstStoreDTO);
        storeService.save(fstStoreDTO);
        sectionService.save(fstSectionDTO);
    }

    @Test
    public void givenTheContentTypeIsCorrect_WhenGettingASection_LoggedInAsManager_ThenAllGood() {
        given().auth().form("admin", "password")
                .accept(ContentType.JSON).
                when()
                .get("/section/1").
                then()
                .statusCode(HttpStatus.OK.value())
                .body("name", is(SECTION_NAME));
    }

    @Test
    public void givenTheContentTypeIsCorrect_WhenDeletingASection_LoggedInAsManager_ThenAccessError() {
        given().auth()
                .form("manager", "password",
                        new FormAuthConfig("/login", "username", "password")
                                .withLoggingEnabled())
                .accept(ContentType.JSON).
                when()
                .delete("/section/1").
                then()
                .statusCode(HttpStatus.FORBIDDEN.value()).log().all();


    }

    @Test
    public void givenTheContentTypeIsCorrect_WhenDeletingASection_LoggedInAsAdmin_ThenAllGood() {
        given().auth()
                .form("admin", "password",
                        new FormAuthConfig("/login", "username", "password")
                                .withLoggingEnabled())
                .accept(ContentType.JSON).
                when()
                .delete("/section/1").
                then()
                .statusCode(HttpStatus.OK.value()).log().all();
    }
}
