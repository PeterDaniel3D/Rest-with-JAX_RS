package facades;

import entities.Person;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import utils.EMF_Creator;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import static org.junit.jupiter.api.Assertions.*;

class PersonFacadeTest {

    private static EntityManagerFactory emf;
    private static PersonFacade facade;

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
        try {
            em.getTransaction().begin();
            em.createNamedQuery("Person.deleteAllRows").executeUpdate();
            em.persist(new Person("PF1", "PL1", "11111111"));
            em.persist(new Person("PF2", "PL2", "22222222"));
            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }

    @Test
    public void getPersonFacade() {
    }

    @Test
    public void addPerson() {
    }

    @Test
    public void deletePerson() {
    }

    @Test
    public void getPerson() {
    }

    @Test
    public void getAllPersons() {
    }

    @Test
    public void editPerson() {
    }
}