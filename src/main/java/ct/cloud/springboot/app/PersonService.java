package ct.cloud.springboot.app;

import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import lombok.extern.slf4j.Slf4j;


@Service
@Slf4j
public class PersonService {
    // In-memory storage (replace with a real database in production)
    private final Map<Long, Person> personStore = new ConcurrentHashMap<>();
    private final AtomicLong idGenerator = new AtomicLong(1);

    @PostConstruct
    public void init() {
        // Initialize with some sample data
        savePerson(createPerson(1L, "John Doe", "john.doe@example.com", "123-456-7890", "USA"));
        savePerson(createPerson(2L, "Jane Smith", "jane.smith@example.com", "234-567-8901", "Canada"));
        savePerson(createPerson(3L, "Bob Johnson", "bob.johnson@example.com", "345-678-9012", "UK"));
        log.info("PersonService initialized with " + personStore.size() + " sample records");
    }

    @Tool(name = "getPersonById",  description = "Find a person by their ID")
    public Person getPersonById(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("Argument 'id' is required and must not be null");
        }
        log.info("Going to find person with ID: " + id);
        Person person = personStore.get(id);
        if (person == null) {
            throw new NoSuchElementException("No person found with ID: " + id);
        }
        log.info("Found person with ID: " + id);
        return person;
    }

//    @Tool(name = "getAllPeople", description = "Get all people in the system")
    @Tool
    public List<Person> getAllPeople() {
        return new ArrayList<>(personStore.values());
    }

    @Tool(description = "Create a new person")
    public Person createPerson(
            @ToolParam(description = "The name of the person") String name,
            @ToolParam(description = "The email address of the person") String email,
            @ToolParam(description = "The phone number of the person") String phone,
            @ToolParam(description = "The nationality of the person") String nationality) {

        Person person = new Person();
        person.setId(idGenerator.getAndIncrement());
        person.setName(name);
        person.setEmail(email);
        person.setPhone(phone);
        person.setNationality(nationality);

        return savePerson(person);
    }

    @Tool(description = "Update an existing person")
    public Person updatePerson(
            @ToolParam(description = "The ID of the person to update") Long id,
            @ToolParam(description = "The updated name of the person", required = false) String name,
            @ToolParam(description = "The updated email of the person", required = false) String email,
            @ToolParam(description = "The updated phone number of the person", required = false) String phone,
            @ToolParam(description = "The updated nationality of the person", required = false) String nationality) {

        Person existing = getPersonById(id);
        if (name != null) existing.setName(name);
        if (email != null) existing.setEmail(email);
        if (phone != null) existing.setPhone(phone);
        if (nationality != null) existing.setNationality(nationality);

        return savePerson(existing);
    }

    @Tool(description = "Delete a person by their ID")
    public String deletePerson(
            @ToolParam(description = "The ID of the person to delete") Long id) {
        if (personStore.remove(id) != null) {
            return "Successfully deleted person with ID: " + id;
        }
        throw new NoSuchElementException("No person found with ID: " + id);
    }

    @Tool(name = "searchPeopleByName", description = "Search for people by name")
    public List<Person> searchPeopleByName(String name) {
        String searchTerm = name.toLowerCase();
        return personStore.values().stream()
                .filter(p -> p.getName().toLowerCase().contains(searchTerm))
                .toList();
    }

    // Helper method to save a person (could be replaced with JPA repository in a real app)
    private Person savePerson(Person person) {
        personStore.put(person.getId(), person);
        return person;
    }
    
    // Helper method to create a person with all fields
    private Person createPerson(Long id, String name, String email, String phone, String nationality) {
        Person person = new Person();
        person.setId(id);
        person.setName(name);
        person.setEmail(email);
        person.setPhone(phone);
        person.setNationality(nationality);
        return person;
    }
}
