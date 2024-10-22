import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import static io.restassured.RestAssured.baseURI;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.lessThan;


public class ApiTests {

    Random random = new Random();
    int petId = random.nextInt(99999);
    Integer id = random.nextInt(99999);

    @BeforeEach
    public void setUp() {
        RestAssured.baseURI = "https://petstore.swagger.io/v2/";
    }

    @BeforeAll
    public static void deletePetBeforeGet() {
        RestAssured.baseURI = "https://petstore.swagger.io/v2/";
    }

    @Test
    public void petNotFoundTestBdd() {
        given().when()
                .get(baseURI + "pet/{id}", petId)
                .then()
                .log().body()
                .statusCode(404)
                .statusLine("HTTP/1.1 404 Not Found")
                .body("type", equalTo("error"),
                        "message", equalTo("Pet not found"));
    }

    @Test
    public void newPetTest() {

        String name = "dogg";
        String status = "sold";

        Map<String, String> request = new HashMap<>();
        request.put("id", id.toString());
        request.put("name", name);
        request.put("status", status);

        Response response = given()
                .contentType("application/json")
                .body(request)
                .when()
                .post(baseURI + "pet/");
        response
                .then()
                .log().all()
                .time(lessThan(2000L))
                .assertThat()
                .statusCode(200)
                .body("id", equalTo(id))
                .body("name", equalTo(name))
                .body("status", equalTo(status));
    }

    @Test
    public void petFoundTestBdd() {
        given().when()
                .get(baseURI + "pet/{id}", 19295)
                .then()
                .log().body()
                .statusCode(200)
                .statusLine("HTTP/1.1 200 OK")
                .body("status", equalTo("sold"));
    }
}