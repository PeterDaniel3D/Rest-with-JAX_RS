package dtos;

import entities.Person;

public class PersonDTO {
    private Integer id;
    private String firstName;
    private String lastName;
    private String phone;
    private String street;
    private int zip;
    private String city;

    public PersonDTO(Person p) {
        this.id = p.getId();
        this.firstName = p.getFirstName();
        this.lastName = p.getLastName();
        this.phone = p.getPhone();
        this.street = p.getAddress().getStreet();
        this.zip = p.getAddress().getZip();
        this.city = p.getAddress().getCity();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
