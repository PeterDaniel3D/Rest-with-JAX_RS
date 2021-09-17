package rest;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dtos.PersonDTO;
import dtos.PersonsDTO;
import facades.PersonFacade;
import utils.EMF_Creator;

import javax.persistence.EntityManagerFactory;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("person")
public class PersonResource {

    private static final EntityManagerFactory EMF = EMF_Creator.createEntityManagerFactory();
       
    private static final PersonFacade FACADE =  PersonFacade.getPersonFacade(EMF);
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
            
    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public String demo() {
        return "{\"msg\":\"Hello World\"}";
    }

    @GET
    @Path("all")
    @Produces({MediaType.APPLICATION_JSON})
    public Response getAllPersons() {
        PersonsDTO persons = FACADE.getAllPersons();
        System.out.println(persons.toString());
        return Response.ok().entity(GSON.toJson(persons)).build();
    }

    @GET
    @Path("{id}")
    @Produces({MediaType.APPLICATION_JSON})
    public Response getPerson(@PathParam("id") Integer id) {
        PersonDTO pDTO = FACADE.getPerson(id);
        return Response.ok().entity(GSON.toJson(pDTO)).build();
    }

    @POST
    @Produces({MediaType.APPLICATION_JSON})
    @Consumes({MediaType.APPLICATION_JSON})
    public Response addPerson(String str) {
        PersonDTO pDTO = GSON.fromJson(str, PersonDTO.class);
        PersonDTO result = FACADE.addPerson(pDTO.getFirstName(), pDTO.getLastName(), pDTO.getPhone());
        return Response.ok().entity(GSON.toJson(result)).build();
    }

    @PUT
    @Path("{id}")
    @Produces({MediaType.APPLICATION_JSON})
    @Consumes({MediaType.APPLICATION_JSON})
    public Response editPerson(@PathParam("id") Integer id, String str) {
        PersonDTO pDTO = GSON.fromJson(str, PersonDTO.class);
        pDTO.setId(id);
        PersonDTO result = FACADE.editPerson(pDTO);
        return Response.ok().entity(GSON.toJson(result)).build();
    }

    @DELETE
    @Path("{id}")
    @Produces({MediaType.APPLICATION_JSON})
    public Response deletePerson(@PathParam("id") Integer id) {
        PersonDTO result = FACADE.deletePerson(id);
        return Response.ok().entity(GSON.toJson(result)).build();
    }
}
