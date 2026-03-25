---

# 📘 Summary of Item 25: *How to Fetch DTO via Spring Projections*

## 🎯 Core Idea
The item explains why and how to fetch **DTOs (Data Transfer Objects)** efficiently in Spring/Hibernate applications using **Spring Projections**, instead of loading full entities when only read‑only data is needed.

---

# 🧠 Why Not Always Fetch Entities?

### Hibernate’s Default Behavior
- Hibernate loads data into the **Persistence Context** (first-level cache) as a **hydrated state**.
- This hydrated state supports:
  - Dirty checking  
  - Optimistic locking  
  - Second-level cache population  

### Problem
If you fetch entities **only to read them**, Hibernate still:
- Stores hydrated state in memory  
- Performs dirty checking  
- Consumes CPU cycles  

➡️ **Unnecessary overhead** when no modifications are planned.

### Alternatives
- **Read-only mode** (`@Transactional(readOnly = true)`) reduces overhead but still loads entities.
- **DTOs** are the best choice for pure read-only data.

---

# 🧩 DTOs vs Spring Projections

| Aspect | DTO | Spring Projection |
|-------|-----|-------------------|
| Structure | Class with constructor + getters/setters | Interface with getters |
| Instantiation | Manual mapping or constructor-based | Spring auto-generates proxy |
| Use case | Flexible, can include logic | Lightweight, ideal for simple read-only views |

Spring also supports **class-based projections**, which behave like DTOs but are still managed by Spring.

---

# 🧱 Example: Interface-Based Projection

### Entity
`Author(id, age, name, genre)`

### Projection
```java
public interface AuthorNameAge {
    String getName();
    int getAge();
}
```

### Repository
```java
List<AuthorNameAge> findFirst2ByGenre(String genre);
```

Spring generates SQL like:
```
SELECT name, age FROM author WHERE genre=? LIMIT 2
```

---

# 🧰 Using Projections with JPQL or Native SQL
You can use projections with:
- Spring Data query methods  
- JPQL queries  
- Native SQL queries  

Column aliases (`AS name`) ensure correct mapping when DB column names differ from entity fields.

---

# 🏷️ Using Named Queries with Projections
The document shows how to combine:
- `@NamedQuery`
- `@NamedNativeQuery`
- `orm.xml`
- `jpa-named-queries.properties`

All can return:
- Scalar values (e.g., `List<String>`)
- Projection interfaces (e.g., `List<AuthorNameAge>`)

Spring automatically maps results to projections.

---

# 🏗️ Class-Based Projections
Instead of an interface:

```java
public class AuthorNameAge {
    private String name;
    private int age;
    public AuthorNameAge(String name, int age) { ... }
}
```

Constructor argument names must match entity property names.

---

# 🔁 Reusing a Projection
When an entity has many fields, instead of creating many projections, define one “maximal” projection:

```java
public interface AuthorDto {
    Integer getAge();
    String getName();
    String getGenre();
    String getEmail();
    String getAddress();
}
```

Then reuse it in multiple queries that fetch different subsets of fields.  
Use:

```
spring.jackson.default-property-inclusion=NON_NULL
```

to avoid serializing nulls.

---

# 🔄 Dynamic Projections
Spring allows returning different projection types from the same query method:

```java
<T> T findByName(String name, Class<T> type);
```

Usage:
```java
Author a = repo.findByName("Joana", Author.class);
AuthorGenreDto g = repo.findByName("Joana", AuthorGenreDto.class);
AuthorNameEmailDto e = repo.findByName("Joana", AuthorNameEmailDto.class);
```

This avoids writing multiple methods for the same query.

---

# ✅ Key Takeaways
- Use **DTOs or projections** for read-only data to avoid Hibernate overhead.
- **Interface-based projections** are lightweight and auto-mapped.
- **Class-based projections** allow constructors and custom logic.
- **Dynamic projections** let one method return multiple types.
- Projections work with **query methods**, **JPQL**, **native SQL**, and **named queries**.
- Reusable projections reduce duplication when entities have many fields.

---