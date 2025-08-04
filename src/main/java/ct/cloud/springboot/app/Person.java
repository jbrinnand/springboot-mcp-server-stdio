package ct.cloud.springboot.app;

import java.util.Objects;

public class Person {
    private Long id;
    private String name;
    private String email;
    private String phone;
    private String nationality;

    // Default constructor for JPA/JSON
    public Person() {
    }

    // All-args constructor
    public Person(Long id, String name, String email, String phone, String nationality) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.nationality = nationality;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }


    public String getPhone() {
        return phone;
    }
    public void setPhone(String phone) {
        this.phone = phone;
    }


    public String getNationality() {
        return nationality;
    }
    public void setNationality(String nationality) {
        this.nationality = nationality;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Person person = (Person) o;
        return Objects.equals(id, person.id) &&
               Objects.equals(name, person.name) &&
               Objects.equals(email, person.email) &&
               Objects.equals(phone, person.phone) &&
               Objects.equals(nationality, person.nationality);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, email, phone, nationality);
    }

    @Override
    public String toString() {
        return "Person{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", phone='" + phone + '\'' +
                ", nationality='" + nationality + '\'' +
                '}';
    }
}
