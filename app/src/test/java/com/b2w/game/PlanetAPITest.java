package com.b2w.game;

import com.b2w.game.model.PlanetWFilm;
import static io.restassured.RestAssured.given;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import static javax.ws.rs.core.Response.Status.*;
import static org.hamcrest.Matchers.not;

@QuarkusTest
class PlanetAPITest {

    private static final String BASE_URL = "/api/planeta";

    @Test
    void testAPISW() {
        final Set<ExoPlanet> planets = ExoPlanet.makeRandomSetSW();
        given()
                .contentType(ContentType.TEXT)
                .body("true")
                .when()
                .put(BASE_URL + "/swapi")
                .then()
                .statusCode(NO_CONTENT.getStatusCode());
        planets.forEach(p -> {
            given()
                    .contentType(ContentType.JSON)
                    .body(p.toString())
                    .when()
                    .post(BASE_URL)
                    .then()
                    .statusCode(CREATED.getStatusCode());
        });
        PlanetWFilm[] todos = given()
                .when()
                .contentType(ContentType.JSON)
                .get(BASE_URL)
                .then()
                .statusCode(OK.getStatusCode())
                .extract()
                .body().as(PlanetWFilm[].class);
        Assertions.assertThat(todos.length > 0);
        Assertions.assertThat(Arrays.stream(todos).allMatch(p -> p.getNumFilmes() >= 0));
    }

    @Test
    void testAPI() {
        final Set<ExoPlanet> planets = ExoPlanet.makeRandomSet();
        given()
                .contentType(ContentType.TEXT)
                .body("false")
                .when()
                .put(BASE_URL + "/swapi")
                .then()
                .statusCode(NO_CONTENT.getStatusCode());
        planets.parallelStream().forEach(p -> {
            given()
                    .contentType(ContentType.JSON)
                    .body(p.toString())
                    .when()
                    .post(BASE_URL)
                    .then()
                    .statusCode(CREATED.getStatusCode());
        });
        PlanetWFilm[] todos = given()
                .when()
                .contentType(ContentType.JSON)
                .get(BASE_URL)
                .then()
                .statusCode(OK.getStatusCode())
                .extract()
                .body().as(PlanetWFilm[].class);
        Assertions.assertThat(planets.equals(new HashSet<>(Arrays.asList(todos))));
        List<PlanetWFilm> plist = new ArrayList<>(planets);
        Collections.shuffle(plist);
        plist.parallelStream().forEach(planet -> {
            String id = planet.id.toString();
            PlanetWFilm res = given()
                    .when()
                    .contentType(ContentType.JSON)
                    .get(BASE_URL + "/{id}", id)
                    .then()
                    .statusCode(OK.getStatusCode())
                    .extract()
                    .body().as(PlanetWFilm.class);
            Assertions.assertThat(ExoPlanet.same(planet, res));
            res = given()
                    .contentType(ContentType.JSON)
                    .when()
                    .get(BASE_URL + "/busca/{nome}", planet.getNome())
                    .then()
                    .statusCode(OK.getStatusCode())
                    .extract()
                    .body().as(PlanetWFilm.class);
            Assertions.assertThat(ExoPlanet.same(planet, res));
            given()
                    .contentType(ContentType.JSON)
                    .when()
                    .delete(BASE_URL + "/{id}", id)
                    .then()
                    .statusCode(NO_CONTENT.getStatusCode());
            given()
                    .when()
                    .contentType(ContentType.JSON)
                    .get(BASE_URL + "/{id}", id)
                    .then()
                    .statusCode(not(OK.getStatusCode())); // 204 or 404
        });
    }
}
