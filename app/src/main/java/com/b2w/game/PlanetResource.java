package com.b2w.game;

import com.b2w.game.model.StarWarsResponse;
import com.b2w.game.swapi.SWAPIClient;
import com.b2w.game.model.Planet;
import com.b2w.game.model.PlanetWFilm;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;
import javax.ws.rs.*;
import static javax.ws.rs.core.MediaType.*;
import javax.ws.rs.core.Response;
import static javax.ws.rs.core.Response.Status.*;
import org.bson.types.ObjectId;
import org.eclipse.microprofile.rest.client.inject.RestClient;

@Path("/api/planeta")
@Consumes(APPLICATION_JSON)
@Produces(APPLICATION_JSON)
public class PlanetResource {
    
    @RestClient
    SWAPIClient swapi;
    private final ConcurrentMap<String, Integer> cache = new ConcurrentHashMap<>();
    private final AtomicBoolean SWAPIenabled = new AtomicBoolean(false);
    private static final int MAX_PLANETS = 5000;
    
    @GET
    public List<PlanetWFilm> list() {
        return Planet
                .todos()
                .parallelStream()
                .limit(MAX_PLANETS)
                .map(planet -> new PlanetWFilm(planet, count(planet.getNome())))
                .collect(Collectors.toList());
    }

    @GET
    @Path("{id}")
    public PlanetWFilm get(@PathParam("id") String id) {
        Planet planet = Planet.findById(new ObjectId(id));
        return planet != null ? new PlanetWFilm(planet, count(planet.getNome())) : null;
    }
    
    @POST
    public Response create(Planet planet) {
        planet.persist();
        return Response.status(CREATED.getStatusCode()).build();
    }

    @DELETE
    @Path("{id}")
    public void delete(@PathParam("id") String id) {
        Planet.deleteById(new ObjectId(id));
    }

    @GET
    @Path("busca/{nome}")
    public Planet search(@PathParam("nome") String nome) {
        return Planet.findByNome(nome);
    }

    @PUT
    @Path("swapi")
    @Consumes(TEXT_PLAIN)
    public Response enableSWAPI(String cond) {
        SWAPIenabled.set(Boolean.parseBoolean(cond));
        return Response.status(NO_CONTENT.getStatusCode()).build();
    }
    
    private int count(String nome) {
        if (!SWAPIenabled.get()) return -1;
        if (cache.containsKey(nome)) {
            return cache.get(nome);
        }
        StarWarsResponse response = swapi.getByName(nome);
        if (response == null || response.getResults().length == 0) return -1;
        int count = response.getResults()[0].getFilms().length;
        cache.put(nome, count);
        return count;
    }
}