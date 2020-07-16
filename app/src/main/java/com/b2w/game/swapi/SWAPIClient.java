package com.b2w.game.swapi;

/**
 *
 * @author msa
 * @see   https://swapi.dev/
 */
import com.b2w.game.model.StarWarsResponse;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

@Path("/api")
@RegisterRestClient(configKey="swapi-api")
public interface SWAPIClient {

    @GET
    @Path("/planets/")
    @Produces("application/json")
    public StarWarsResponse getByName(@QueryParam("search") String search);
}