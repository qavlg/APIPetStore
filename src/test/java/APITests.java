
import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Random;

import static io.restassured.RestAssured.baseURI;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;


public class APITests {

    Random random = new Random();
    int petID = random.nextInt(99999);

    @BeforeEach
    public void setUp() {
        RestAssured.baseURI = "https://petstore.swagger.io/v2/";
    }

    @Test
    public void petNotFoundTest_BDD() {
        given().when()
                .get(baseURI + "pet/{id}", petID)
                .then()
                .log().body()
                .statusCode(404)
                .statusLine("HTTP/1.1 404 Not Found")
                .body("type",equalTo("error"))
                .body("message", equalTo("Pet not found"));
    }
}