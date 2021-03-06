package facades;

import dtos.PersonDTO;
import entities.Address;
import entities.Person;
import errorhandling.MissingInputException;
import errorhandling.PersonNotFoundException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import utils.EMF_Creator;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

class PersonFacadeTest {

    private static EntityManagerFactory emf;
    private static PersonFacade facade;
    private static Person p1, p2, p3;
    private static Address a1, a2, a3;

    public PersonFacadeTest() {
    }

    @BeforeAll
    public static void setUpClass() {
        emf = EMF_Creator.createEntityManagerFactoryForTest();
        facade = PersonFacade.getPersonFacade(emf);
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
            em.createNamedQuery("Address.deleteAllRows").executeUpdate();
            em.persist(p1);
            em.persist(p2);
            em.persist(p3);
            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }

    @Test
    public void addPerson() throws MissingInputException {
        PersonDTO expected = new PersonDTO(p1);
        PersonDTO actual = facade.addPerson("PF1", "PL1", "11111111");
        //assertEquals(expected, actual);
        assertTrue(Objects.equals(expected.getFirstName(), actual.getFirstName()) &&
                Objects.equals(expected.getLastName(), actual.getLastName()));
    }

    @Test
    public void deletePerson() throws PersonNotFoundException {
        long expected = p1.getId();
        long actual = facade.deletePerson(p1.getId()).getId();
        assertEquals(expected, actual);
    }

    @Test
    public void getPerson() throws PersonNotFoundException {
        String expected = p1.getFirstName();
        String actual = facade.getPerson(p1.getId()).getFirstName();
        assertEquals(expected, actual);
    }

    @Test
    public void getAllPersons() {
        assertEquals(3, facade.getAllPersons().getSize()); //<-- Alternativ metode til .getSize()
    }

    @Test
    public void editPerson() throws MissingInputException, PersonNotFoundException {
        p1.setFirstName("Peter");
        PersonDTO expected = new PersonDTO(p1);
        PersonDTO actual = facade.editPerson(new PersonDTO(p1));
        assertEquals(expected, actual);
    }
}