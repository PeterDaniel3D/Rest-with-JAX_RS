package rest;

import dtos.PersonDTO;
import entities.Address;
import entities.Person;
import io.restassured.RestAssured;
import io.restassured.parsing.Parser;
import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.grizzly.http.util.HttpStatus;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;
import org.junit.jupiter.api.*;
import utils.EMF_Creator;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriBuilder;
import java.net.URI;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;

class PersonResourceTest {

    private static final int SERVER_PORT = 7777;
    private static final String SERVER_URL = "http://localhost/api";
    private static Person p1, p2, p3;
    private static Address a1, a2, a3;

    static final URI BASE_URI = UriBuilder.fromUri(SERVER_URL).port(SERVER_PORT).build();
    private static HttpServer httpServer;
    private static EntityManagerFactory emf;

    static HttpServer startServer() {
        ResourceConfig rc = ResourceConfig.forApplication(new ApplicationConfig());
        return GrizzlyHttpServerFactory.createHttpServer(BASE_URI, rc);
    }

    @BeforeAll
    public static void setUpClass() {
        EMF_Creator.startREST_TestWithDB();
        emf = EMF_Creator.createEntityManagerFactoryForTest();

        httpServer = startServer();
        RestAssured.baseURI = SERVER_URL;
        RestAssured.port = SERVER_PORT;
        RestAssured.defaultParser = Parser.JSON;
    }

    @AfterAll
    public static void closeTestServer() {
        EMF_Creator.endREST_TestWithDB();
        httpServer.shutdownNow();
    }

    @BeforeEach
    public void setUp() {
        EntityManager em = emf.createEntityManager();
        p1 = new Person("PF1", "PL1", "11111111");
        p2 = new Person("PF2", "PL2", "22222222");
        p3 = new Person("PF3", "PL3", "33333333");

        a1 = new Address("ADR1", 1000, "BY1");
        a2 = new Address("ADR2", 2000, "BY2");
        a3 = new Address("ADR3", 3000, "BY3");

        p1.setAddress(a1);
        p2.setAddress(a2);
        p3.setAddress(a3);

        try {
            em.getTransaction().begin();
            em.createNamedQuery("Person.deleteAllRows").executeUpdate();
            em.persist(p1);
            em.persist(p2);
            em.persist(p3);
            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    public void testServerIsUp() {
        System.out.println("Test server is running (okay)");
        given().when().get("/person").then().statusCode(200);
    }

    @Test
    void demo() {
        given()
                .contentType("application/json")
                .get("/person").then()
                .assertThat()
                .statusCode(HttpStatus.OK_200.getStatusCode())
                .body("msg", equalTo("Hello World"));
    }

    @Test
    void getAllPersons() {
        given()
                .contentType(MediaType.APPLICATION_JSON)
                .get("/person/all").then()
                .assertThat()
                .statusCode(HttpStatus.OK_200.getStatusCode())
                .body("all", hasSize(3));
    }

    @Test
    void getPerson() {
        given()
                .contentType(MediaType.APPLICATION_JSON)
                .get("/person/" + p1.getId()).then()
                .assertThat()
                .statusCode(HttpStatus.OK_200.getStatusCode())
                .body("firstName", equalTo("PF1"));
    }

    @Test
    void addPerson() {
        PersonDTO pTest = new PersonDTO(new Person("Test1", "Test2", "12345678", a1));

        given()
                .contentType(MediaType.APPLICATION_JSON)
                .body(pTest)
                .when()
                .post("/person").then()
                .assertThat()
                .statusCode(HttpStatus.OK_200.getStatusCode())
                .body("firstName", equalTo("Test1"))
                .body("lastName", equalTo("Test2"))
                .body("phone", equalTo("12345678"));
    }

    @Test
    void editPerson() {
        PersonDTO pTest = new PersonDTO(new Person("Test1", "Test2", "12345678", a3));

        given()
                .contentType(MediaType.APPLICATION_JSON)
                .body(pTest)
                .when()
                .put("/person/" + p1.getId()).then()
                .assertThat()
                .statusCode(HttpStatus.OK_200.getStatusCode())
                .body("firstName", equalTo("Test1"))
                .body("lastName", equalTo("Test2"))
                .body("phone", equalTo("12345678"));
    }

    @Test
    void deletePerson() {
        given()
                .contentType(MediaType.APPLICATION_JSON)
                .delete("/person/" + p1.getId()).then()
                .assertThat()
                .statusCode(HttpStatus.OK_200.getStatusCode())
                .body("firstName", equalTo("PF1"));

    }
}