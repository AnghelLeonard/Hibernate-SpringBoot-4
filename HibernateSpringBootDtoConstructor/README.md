---

## 📘 Summary of *Item 31: How to Fetch DTO via Constructor Expression*  

This item explains how to fetch partial data from a JPA entity into a custom DTO (Data Transfer Object) using two approaches **Spring Data Query Builder** and **JPQL Constructor Expressions**.

### 🔹 1. The Goal  
Retrieve only **name** and **age** of authors who share a given **genre**, without loading the entire `Author` entity.

### 🔹 2. DTO-Based Projection  
A simple DTO (`AuthorDto`) is created with:
- `final` fields (`name`, `age`)
- A constructor matching the selected fields
- Only getters (no setters needed)

### 🔹 3. Approach A — Spring Data Query Builder  
Define a repository method:

```java
List<AuthorDto> findByGenre(String genre);
```

Spring Data automatically:
- Generates SQL selecting only the required columns  
- Maps results into `AuthorDto` instances

**Generated SQL example:**  
`SELECT name, age FROM author WHERE genre = ?`

This is the simplest and most idiomatic approach when Spring Data can derive the query.

### 🔹 4. Approach B — JPQL Constructor Expression  
If Query Builder is insufficient, you can explicitly use JPQL:

```java
@Query("SELECT new com.bookstore.dto.AuthorDto(a.name, a.age) FROM Author a")
List<AuthorDto> fetchAuthors();
```

JPQL uses the DTO constructor to instantiate objects directly from query results.

### 🔹 5. Approach C — EntityManager  
For full manual control:

```java
Query query = entityManager.createQuery(
    "SELECT new com.bookstore.dto.AuthorDto(a.name, a.age) FROM Author a",
    AuthorDto.class
);
List<AuthorDto> authors = query.getResultList();
```

### 🔹 6. Key Takeaways  
- DTO projections improve performance by selecting only needed fields.  
- Spring Data’s derived queries are easiest but limited.  
- JPQL constructor expressions offer flexibility and explicit control.  
- EntityManager provides the lowest-level option when needed.

---