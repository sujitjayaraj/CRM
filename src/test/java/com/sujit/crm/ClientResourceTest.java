package com.sujit.crm;

import io.restassured.RestAssured;
import io.restassured.http.Header;
import io.restassured.internal.util.IOUtils;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.io.InputStream;

@TestPropertySource("/test.properties")
@SpringBootTest(classes = { CrmApplication.class }, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@RunWith(SpringRunner.class)
public class ClientResourceTest {
    @LocalServerPort
    private int port;
    @Before
    public void setUp() {
        RestAssured.port = port;
    }
    @Test
    public void shouldCreatedClient() throws IOException {
        byte[] body = loadResource();
        RequestSpecification request = RestAssured.given().body(body).header(new Header("Content-Type", "application/json"));
        Response response = request.post(getMailEndpoint());
        response.then().statusCode(201);
    }
    @Test
    public void shouldReturnCreatedUser() throws IOException {
        byte[] body = loadResource();
        String id = RestAssured.given().body(body).header(new Header("content-type", "application/json")).post(getMailEndpoint()).getBody().print();
        Response response = RestAssured.given().auth().basic("sujitjayaraj05@gmail.com", "pass").get(getMailEndpoint() + id);
        response.then().statusCode(200).body("name", Matchers.equalTo("Sujit"))
                .body("status", Matchers.equalTo("lead"))
                .body("contactPerson.lastname", Matchers.equalTo("pai"))
                .body("address.city", Matchers.equalTo("Mangalore"));
    }
    private String getIdEndpoint() {
        return "/clients/";
    }
    private String getMailEndpoint() {
        return "clients/test@gmail.com";
    }

    private byte[] loadResource() throws IOException {
        InputStream resourceAsStream = this.getClass().getResourceAsStream("/Client.json");
        assert resourceAsStream != null;
        return IOUtils.toByteArray(resourceAsStream);
    }
}