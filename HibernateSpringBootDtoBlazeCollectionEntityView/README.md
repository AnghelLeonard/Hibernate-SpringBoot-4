## **Summary of Item 38 — Fetching DTOs with Blaze-Persistence Entity Views**

### **Purpose**
This item explains how to fetch lightweight DTO-like projections in a Spring Boot + JPA/Hibernate application using **Blaze-Persistence Entity Views**, focusing on retrieving only selected fields (e.g., author name and age) instead of full entities.

---

## **Key Concepts**

### **1. Blaze-Persistence Entity Views**
- Blaze-Persistence is an external library that provides a **rich Criteria API** and **Entity Views** for efficient DTO projection.
- Entity Views allow you to define interfaces that map to specific parts of an entity, avoiding loading unnecessary fields.

### **2. Example Entity**
The example uses an `Author` entity with fields:
- `id`
- `age`
- `name`
- `genre`

The goal: fetch **only `name` and `age`** for all authors.

---

## **3. Required Dependencies**
Two Maven dependencies must be added:
- `blaze-persistence-integration-spring-data-4.0`
- `blaze-persistence-integration-hibernate-7.2`

Both at version **1.6.18**.

---

## **4. Configuration**
A Spring `@Configuration` class sets up:
- `CriteriaBuilderFactory`
- `EntityViewManager`

These are created as Spring beans and wired using the `LocalContainerEntityManagerFactoryBean`.

Annotations used:
- `@EnableBlazeRepositories("com.bookstore")`
- `@EnableEntityViews("com.bookstore")`

---

## **5. Creating an Entity View**
To fetch only name and age, define:

```java
@EntityView(Author.class)
public interface AuthorView {
    String getName();
    int getAge();
}
```

This interface acts as a DTO projection.

---

## **6. Repository Setup**
Create a repository extending Blaze-Persistence’s `EntityViewRepository`:

```java
public interface AuthorViewRepository
    extends EntityViewRepository<AuthorView, Long> { }
```

This behaves like any Spring Data repository.

---

## **7. Fetching Data**
A service calls:

```java
authorViewRepository.findAll();
```

This generates SQL selecting only the required columns:

```
SELECT a1_0.age, a1_0.name FROM author a1_0
```

---

## **8. Complex Views (Nested Collections)**
Blaze-Persistence supports nested views, e.g., fetching authors and their books:

```java
@EntityView(Author.class)
public interface AuthorBookView {
    @IdMapping Long getId();
    String getName();
    Integer getAge();
    Set<BookView> getBooks();

    @EntityView(Book.class)
    interface BookView {
        @IdMapping Long getId();
        String getTitle();
    }
}
```

This fetches:
- Author: id, name, age  
- Books: id, title  

---

## **In Short**
Blaze-Persistence Entity Views provide a powerful, efficient way to fetch DTO-like projections in Spring Boot applications. They:
- Reduce data loading
- Integrate smoothly with Spring Data
- Support nested projections
- Generate optimized SQL automatically

---