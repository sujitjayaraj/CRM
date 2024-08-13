package com.sujit.crm;

import io.restassured.RestAssured;
import io.restassured.http.Header;
import io.restassured.internal.util.IOUtils;
import io.restassured.response.Response;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.io.InputStream;

@TestPropertySource("/test.properties")
@SpringBootTest(classes = CustomerRelationshipManagerApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@RunWith(SpringRunner.class)
public class ClientResourceTest {

    @LocalServerPort
    private int port;

    @Before
    public void setUp() {
        RestAssured.port = port;
    }

    @Test
    public void should_create_client() throws IOException {
        byte[] body = loadResource("/Client.json");
        String id = RestAssured.given().body(body).header(new Header("content-type", "application/json"))
                .post(getMailEndpoint()).getBody().print();

        Response response = RestAssured.given().auth().basic("john.doe@gmail.com", "pass").get(getIdEndpoint() + id);

        response.then().statusCode(200).body("name", Matchers.equalTo("Sujit"))
                .body("status", Matchers.equalTo("lead"))
                .body("contactPerson.lastname", Matchers.equalTo("Doe"))
                .body("address.city", Matchers.equalTo("San Francisco"));
    }

    private String getIdEndpoint() {
        return "/clients/";
    }

    private String getMailEndpoint() {
        return "/clients/test@mail.in";
    }

    private byte[] loadResource(String resourcePath) throws IOException {
        InputStream resourceAsStream = this.getClass().getResourceAsStream(resourcePath);
        assert resourceAsStream != null;
        return IOUtils.toByteArray(resourceAsStream);
    }
}
