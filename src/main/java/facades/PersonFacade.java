package facades;

import dtos.PersonDTO;

import dtos.PersonsDTO;
import entities.Address;
import entities.Person;
import errorhandling.MissingInputException;
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
    public PersonDTO addPerson(String fName, String lName, String phone) throws MissingInputException {
        Person person = new Person(fName, lName, phone);

        if ((fName == null) || (lName == null)) {
            throw new MissingInputException(400, "First Name and/or Last Name is missing");
        }

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
    public PersonDTO deletePerson(int id) throws PersonNotFoundException {
        EntityManager em = getEntityManager();
        Person person = em.find(Person.class, id);
        Address address = em.find(Address.class, person.getAddress().getId());
        if (person == null) {
            throw new PersonNotFoundException(404, "Could not delete, provided id does not exist");
        }
        try {
            em.getTransaction().begin();
            em.remove(person);
            em.remove(address);
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
            throw new PersonNotFoundException(404, "No person with provided id found");
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
    public PersonDTO editPerson(PersonDTO pDTO) throws PersonNotFoundException, MissingInputException {
        EntityManager em = getEntityManager();

        Person person = em.find(Person.class, pDTO.getId());
        if (person == null) {
            throw new PersonNotFoundException(404, "Could not update, provided id does not exist");
        }

        if ((pDTO.getFirstName() == null) || (pDTO.getLastName() == null)) {
            throw new MissingInputException(400, "First Name and/or Last Name is missing");
        }

        person.setFirstName(pDTO.getFirstName());
        person.setLastName(pDTO.getLastName());
        person.setPhone(pDTO.getPhone());
        person.setLastEdited(new Date());

        try {
            em.getTransaction().begin();
            //em.merge(person); <-- Koden virker uden?
            em.getTransaction().commit();
        } finally {
            em.close();
        }
        return new PersonDTO(person);
    }
}
