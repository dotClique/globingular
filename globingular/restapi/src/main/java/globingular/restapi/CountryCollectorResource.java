package globingular.restapi;

import globingular.core.Country;
import globingular.core.CountryCollector;
import globingular.core.Visit;
import globingular.core.World;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

import java.time.LocalDateTime;

/**
 * Root resource (exposed at "myresource" path).
 */
@Path("countryCollector")
public class CountryCollectorResource {

    /**
     * Method handling HTTP GET requests. The returned object will be sent
     * to the client as "application/json" media type.
     *
     * @return String that will be returned as a application/json response.
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Country getCountry() {
        return new Country("NO", "Norway");
    }

    /**
     * Method handling HTTP GET requests. The returned object will be sent
     * to the client as "application/json" media type.
     *
     * @param id The ID of the User whose CountryCollector to return
     * @return String that will be returned as a text/plain response.
     */
    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public CountryCollector getOne(@PathParam("id") final String id) {
        var no = new Country("NO", "Norway");
        var cc = new CountryCollector(new World(no));
        cc.registerVisit(new Visit(
                no,
                LocalDateTime.parse("2020-11-02T22:22:28.792700100"),
                LocalDateTime.parse("2020-11-03T22:22:28.792700100")));
        return cc;
    }
}
