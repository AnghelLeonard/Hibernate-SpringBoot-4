---

# 📘 Summary of Item 26: *How to Add an Entity in a Spring Projection*

This item explains how to include full JPA entities inside Spring projections (DTOs), even though projections are typically used for read‑only, 
partial data. It covers two scenarios

---

## 🟦 1. **Materialized Association (Actual @OneToMany Relationship)**

### **Scenario**
- `Author` and `Book` have a bidirectional lazy `@OneToMany` association.
- The projection (`BookstoreDto`) needs:
  - the full `Author` entity
  - only the `title` from `Book`

### **Projection**
```java
public interface BookstoreDto {
    Author getAuthor();
    String getTitle();
}
```

### **Repository Query**
```java
@Query("SELECT a AS author, b.title AS title FROM Author a JOIN a.books b")
List<BookstoreDto> fetchAll();
```

### **Behavior**
- Returned `Author` entities are **managed by Hibernate**.
- Any modifications to them inside a transactional context trigger **automatic UPDATEs** via dirty checking.

### **Example**
```java
dto.get(0).getAuthor().setGenre("Poetry"); // triggers UPDATE
```

---

## 🟩 2. **Non‑Materialized Association (No Direct Relationship)**

### **Scenario**
- `Author` and `Book` are not related via JPA.
- They share a common attribute: `genre`.
- You still want to fetch both into the same projection.

### **Repository Query**
```java
@Query("SELECT a AS author, b.title AS title 
       FROM Author a JOIN Book b ON a.genre=b.genre ORDER BY a.id")
List<BookstoreDto> fetchAll();
```

### **Behavior**
- Even without a mapped relationship, the query joins the tables using `genre`.
- Returned `Author` entities are still **managed**, so changes trigger UPDATEs.

### **Example**
```java
dto.get(0).getAuthor().setAge(47); // triggers UPDATE
```

---

## 📝 Key Takeaways

- Spring projections **can include full entities**, not just scalar fields.
- If the projection includes an entity, and the query loads it, Hibernate will:
  - return it as a **managed entity**
  - propagate any changes automatically via **dirty checking**
- This works whether the association is:
  - **materialized** (mapped with JPA annotations), or  
  - **non‑materialized** (joined manually in JPQL)

---