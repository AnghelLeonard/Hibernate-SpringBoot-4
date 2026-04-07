---

# 📘 Summary of Item 33: *How to Fetch DTO via JPA Tuple*

### 🎯 Goal  
Fetch only selected fields (e.g., `name` and `age`) from the `Author` entity **without creating a DTO class**, using **jakarta.persistence.Tuple**.

---

# 🧩 Why Use `Tuple` Instead of `Object[]`?

- **Keeps aliases** — if your query uses `AS name`, Tuple preserves `"name"`  
- **Automatic type casting** — no manual casting needed  
- **Type‑safe** — `TupleElement` uses generics  
- **Works everywhere** — JPQL, Criteria API, and native SQL

Tuple is one of the **best options for scalar projections** in JPA.

---

# 🏗️ Example Entity  
```java
@Entity
public class Author implements Serializable {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private int age;
    private String name;
    private String genre;
}
```

---

# 🗂️ Spring Data Repository Examples

### **JPQL version**
```java
@Query("SELECT a.name AS name, a.age AS age FROM Author a")
List<Tuple> fetchAuthors();
```
Generated SQL:
```
SELECT a1_0.id, a1_0.name, a1_0.age FROM author a1_0
```

### **Native SQL version**
```java
@Query(value = "SELECT name, age FROM author", nativeQuery = true)
List<Tuple> fetchAuthors();
```
Generated SQL:
```
SELECT name, age FROM author
```

⚠️ Using Spring Data query builder with Tuple will fetch **all** entity attributes, not just selected ones.

---

# 📥 Accessing Tuple Data
```java
for (Tuple author : authors) {
    System.out.println("Author name: " + author.get("name")
        + " | Age: " + author.get("age"));
}
```

Example output:
```
Author name: Mark Janel | Age: 23
Author name: Olivia Goy | Age: 43
Author name: Quartis Young | Age: 51
```

You can also check types:
```java
author.get("name") instanceof String;   // true
author.get("age") instanceof Integer;   // true
```

---

# 🧰 Using EntityManager Directly

### Native SQL
```java
Query query = entityManager.createNativeQuery(
    "SELECT name, age FROM author", Tuple.class);
List<Tuple> authors = query.getResultList();
```

### JPQL
```java
TypedQuery<Tuple> query = entityManager.createQuery(
    "SELECT a.name AS name, a.age AS age FROM Author a", Tuple.class);
List<Tuple> authors = query.getResultList();
```

### Criteria API
Use:
```java
CriteriaQuery<Tuple> createTupleQuery()
```

---