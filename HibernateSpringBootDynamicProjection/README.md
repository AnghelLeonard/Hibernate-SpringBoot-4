# 📘 Summary of **Item 25: How to Fetch DTO via Spring Projections**
---

## 🧩 Core Idea
**Don’t fetch full Hibernate entities when you only need read‑only data.  
Use DTOs or Spring Projections instead.**  
Fetching entities triggers hydration, dirty checking, optimistic locking, and caching — all unnecessary overhead when no modifications are planned.

---

## 🏗️ Why Entities Are Expensive
Hibernate fetches data into the **Persistence Context** as a hydrated state (Object[] / EntityEntry).  
This hydrated state powers:
- Dirty checking  
- Versionless optimistic locking  
- Second‑level cache population  

If you fetch entities in **read‑write mode**, Hibernate keeps all this machinery active → **memory + CPU cost**.

Even **read‑only mode** still builds entities, just without hydration storage.

---

## 🎯 When to Use DTOs or Projections
Use DTOs / projections when:
- You need **read‑only** data  
- You want **only a subset of columns**  
- You want **better performance**  
- You want **no entity lifecycle overhead**

DTOs = classes with constructors  
Projections = interfaces (Spring auto‑generates proxies)

---

## 🧪 Interface‑Based Closed Projections
Example:

```java
public interface AuthorNameAge {
    String getName();
    int getAge();
}
```

Spring generates a proxy and fetches only the required columns.

Repository:

```java
List<AuthorNameAge> findFirst2ByGenre(String genre);
```

Generated SQL:

```
SELECT name, age FROM author WHERE genre=? LIMIT ?
```

---

## 📦 Using LIMIT with Spring Data
Spring Data 3.2 introduces `Limit`:

```java
List<AuthorNameAge> findByGenre(String genre, Limit limit);
```

Call:

```java
authorRepository.findByGenre("Anthology", Limit.of(2));
```

---

## 🧬 Native SQL & JPQL Projections
You can use projections with:
- Spring Query Builder  
- JPQL  
- Native SQL  
- Named queries (`@NamedQuery`, `@NamedNativeQuery`)  
- Properties‑based named queries  
- orm.xml named queries  

All can map directly into projection interfaces.

---

## 🏛️ Class‑Based Projections
DTO-style projection:

```java
public class AuthorNameAge {
    private String name;
    private int age;
    public AuthorNameAge(String name, int age) { ... }
}
```

Constructor argument names must match entity property names.

---

## 🔁 Reusing a Single Projection
Define a “heavy” projection:

```java
public interface AuthorDto {
    Integer getAge();
    String getName();
    String getGenre();
    String getEmail();
    String getAddress();
}
```

Then reuse it for multiple queries that fetch different subsets.

To avoid nulls in JSON:

```
spring.jackson.default-property-inclusion=NON_NULL
```

---

## 👁️ Using @JsonView (Alternative)
Define hierarchical views:

```java
public class Views {
    interface NameEmail {}
    interface NameEmailAgeGenre extends NameEmail {}
    interface All extends NameEmailAgeGenre {}
}
```

DTO:

```java
public record AuthorDto(
    @JsonView(Views.NameEmail.class) String name,
    @JsonView(Views.NameEmail.class) String email,
    @JsonView(Views.NameEmailAgeGenre.class) Integer age,
    @JsonView(Views.NameEmailAgeGenre.class) String genre,
    @JsonView(Views.All.class) String address
) {}
```

But: **@JsonView is inefficient** because the SQL still fetches *all columns*.

---

## ⚡ Dynamic Projections
Define one method:

```java
<T> T findByName(String name, Class<T> type);
```

Depending on `type`, Spring fetches only the required columns:

- `Author.class` → fetches all columns  
- `AuthorGenreDto.class` → fetches only `genre`  
- `AuthorNameEmailDto.class` → fetches only `name`, `email`

This is **the most efficient** approach.

---

## 🏁 Final Takeaways
- Avoid fetching entities for read‑only use cases.  
- Use DTOs or projections to reduce memory, CPU, and SQL load.  
- Interface‑based projections are simple and powerful.  
- Class‑based projections allow constructors and equals/hashCode.  
- Reusable projections + `NON_NULL` help avoid cluttered JSON.  
- @JsonView is flexible but **not efficient**.  
- **Dynamic projections** are an elegant and performant solution.

---