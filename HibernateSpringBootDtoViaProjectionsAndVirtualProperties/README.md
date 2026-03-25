---

# ✨ Summary of *Item 27: How to Enrich Spring Projections with Virtual Properties*

## 🎯 Core Idea
Spring Data projections can be enhanced with **virtual properties**—fields that **do not exist in the underlying Domain Model** but are computed at runtime. These are typically implemented using **interface-based open projections** combined with **Spring Expression Language (SpEL)**.

---

## 🧩 What Are Open Projections?
Open projections are Spring interfaces where:
- Some methods map directly to entity fields.
- Other methods compute values dynamically using `@Value` and SpEL.
- They allow mixing real entity data with computed or constant values.

---

## 🛠 Example Explained
The item shows a projection interface:

```java
public interface AuthorNameAge {
    String getName();
    @Value("#{target.age}")
    String years();
    @Value("#{ T(java.lang.Math).random() * 10000 }")
    int rank();
    @Value("5")
    String books();
}
```

![](https://github.com/AnghelLeonard/Hibernate-SpringBoot-4/blob/main/HibernateSpringBootDtoViaProjectionsAndVirtualProperties/dto%20spring%20projection%20and%20virtual%20properties.png)

### What each method does:
- `getName()` → maps to the entity’s `name`.
- `years()` → exposes the entity’s `age` under a different name.
- `rank()` → generates a random number at runtime.
- `books()` → returns a constant value `"5"`.

---

## 🗄 Repository Query
The repository fetches only `name` and `age`:

```java
@Query("SELECT a.name AS name, a.age AS age FROM Author a WHERE a.age >= ?1")
List<AuthorNameAge> fetchByAge(int age);
```

Even though the query returns only two fields, the projection enriches the result with additional computed properties.

---

## 📤 Example Output
When iterating through results, the projection prints:

- Name (from DB)
- Age (via `years()`)
- Rank (random)
- Books (constant)

Example output:

```
Author name: Olivia Goy | Age: 43 | Rank: 3435 | Books: 5
```

---

## 📌 Key Takeaways
- Spring projections can include **virtual fields** using SpEL.
- These fields can:
  - Map to existing entity fields under new names.
  - Compute values dynamically.
  - Provide constants.
- This technique is useful for lightweight DTO-like views without creating separate classes.
- Only the fields referenced in the query are fetched from the database; the rest are computed in memory.

---