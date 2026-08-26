---

# 📝 Summary of Item 31: *How to Fetch DTO via Constructor Expression*

### 🎯 Goal
Fetch only **specific fields** (name and age) of `Author` entities that share the same genre, using **DTOs** or **Java records** instead of returning full entities.

---

## 📌 1. The Entity
The example uses a simple `Author` JPA entity with fields:
- `id`
- `age`
- `name`
- `genre`

---

## 📌 2. DTO / Record Definition
To fetch partial data, we have here two options:

### **DTO class**
```java
public class AuthorDto {
    private final String name;
    private final int age;
    public AuthorDto(String name, int age) { ... }
    public String getName() { ... }
    public int getAge() { ... }
}
```

### **Java record**
```java
public record AuthorRecord(String name, int age) {}
```

Both represent lightweight projections of the entity.

---

## 📌 3. Fetching DTOs via Spring Data Query Builder
Spring Data can automatically map query results to DTOs:

```java
List<AuthorDto> findByGenre(String genre);
```

This generates SQL like:
```
SELECT name, age FROM author WHERE genre = ?
```

---

## 📌 4. Fetching DTOs via JPQL Constructor Expression
If Spring Data’s query builder is insufficient, JPQL can explicitly construct DTOs:

```java
@Query("SELECT new com.bookstore.dto.AuthorDto(a.name, a.age) FROM Author a")
List<AuthorDto> fetchAuthorsDto();
```

Or using records:
```java
@Query("SELECT new com.bookstore.dto.AuthorRecord(a.name, a.age) FROM Author a")
List<AuthorRecord> fetchAuthorsRecord();
```

Generated SQL:
```
SELECT name, age FROM author
```

---

## 📌 5. Fetching DTOs via EntityManager
For full manual control:

```java
Query query = entityManager.createQuery(
    "SELECT new com.bookstore.dto.AuthorDto(a.name, a.age) FROM Author a",
    AuthorDto.class
);
List<AuthorDto> authors = query.getResultList();
```

Same pattern applies for records.

---

## ⭐ Key Takeaways
- DTOs and records allow **partial data fetching**, improving performance and reducing unnecessary data transfer.
- Spring Data can automatically map results to DTOs if constructor signatures match.
- JPQL constructor expressions provide more flexibility when Spring Data’s query builder is not enough.
- EntityManager offers the most manual control but requires more boilerplate.

---