package facades;

import dtos.PersonDTO;

import dtos.PersonsDTO;
import entities.Person;
import errorhandling.PersonNotFoundException;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.TypedQuery;

import java.util.Date;
import java.util.List;

public class PersonFacade implements IPersonFacade {

    private static PersonFacade instance;
    private static EntityManagerFactory emf;

    private PersonFacade() {
    }

    public static PersonFacade getPersonFacade(EntityManagerFactory _emf) {
        if (instance == null) {
            emf = _emf;
            instance = new PersonFacade();
        }
        return instance;
    }

    private EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    @Override
    public PersonDTO addPerson(String fName, String lName, String phone) {
        Person person = new Person(fName, lName, phone);
        EntityManager em = getEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(person);
            em.getTransaction().commit();
        } finally {
            em.close();
        }
        return new PersonDTO(person);
    }

    @Override
    public PersonDTO deletePerson(int id) {
        EntityManager em = getEntityManager();
        Person person = em.find(Person.class, id);
        try {
            em.getTransaction().begin();
            em.remove(person);
            em.getTransaction().commit();
        } finally {
            em.close();
        }
        return new PersonDTO(person);
    }

    @Override
    public PersonDTO getPerson(int id) throws PersonNotFoundException {
        EntityManager em = getEntityManager();
        Person person = em.find(Person.class, id);
        if (person == null) {
            throw new PersonNotFoundException(404, "message: No person with provided id found");
        }
        return new PersonDTO(person);
    }

    @Override
    public PersonsDTO getAllPersons() {
        EntityManager em = getEntityManager();
        TypedQuery<Person> query = em.createQuery("SELECT p FROM Person p", Person.class);
        List<Person> persons = query.getResultList();
        return new PersonsDTO(persons);
    }

    @Override
    public PersonDTO editPerson(PersonDTO pDTO) {
        EntityManager em = getEntityManager();

        Person person = em.find(Person.class, pDTO.getId());
        person.setFirstName(pDTO.getFirstName());
        person.setLastName(pDTO.getLastName());
        person.setPhone(pDTO.getPhone());
        person.setLastEdited(new Date());

        try {
            em.getTransaction().begin();
            em.getTransaction().commit();
        } finally {
            em.close();
        }
        return new PersonDTO(person);
    }
}
