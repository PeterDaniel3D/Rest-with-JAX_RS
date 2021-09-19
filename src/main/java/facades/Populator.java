/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package facades;

import entities.Address;
import entities.Person;
import utils.EMF_Creator;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

public class Populator {
    public static void populate() {
        EntityManagerFactory emf = EMF_Creator.createEntityManagerFactory();
        EntityManager em = emf.createEntityManager();

        Person p1 = new Person("PF1", "PL1", "11111111");
        Person p2 = new Person("PF2", "PL2", "22222222");
        Person p3 = new Person("PF3", "PL3", "33333333");

        Address a1 = new Address("ADR1", 1000, "BY1");
        Address a2 = new Address("ADR2", 2000, "BY2");
        Address a3 = new Address("ADR3", 3000, "BY3");

        p1.setAddress(a1);
        p2.setAddress(a2);
        p3.setAddress(a3);

        try {
            em.getTransaction().begin();
            em.persist(p1);
            em.persist(p2);
            em.persist(p3);
            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }

    public static void main(String[] args) {
        populate();
    }
}
