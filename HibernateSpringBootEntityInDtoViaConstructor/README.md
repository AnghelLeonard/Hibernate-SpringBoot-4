---

## 📘 Summary of Item 32: *Fetching Entities in a DTO via Constructor Expression*

This item explains how to fetch data from two unrelated entities—**Author** and **Book**—that share a common attribute (**genre**) but have **no materialized association** in JPA.

### 🔍 Goal
Join `Author` and `Book` using their shared `genre` field and return results in a **DTO** containing:
- the full `Author` entity  
- only the `title` field from `Book`

### 🧱 DTO Structure
A simple DTO (`BookstoreDto`) is created with:
- `Author author`
- `String title`
- A constructor accepting both fields

### 🧠 JPQL Query
A constructor expression is used to populate the DTO:

```java
@Query("SELECT new com.bookstore.dto.BookstoreDto(a, b.title) " +
       "FROM Author a JOIN Book b ON a.genre=b.genre ORDER BY a.id")
List<BookstoreDto> fetchAll();
```

### 📊 SQL Generated
The query executes as a **single SELECT** joining the two tables on `genre`.

### 🔄 Behavior in Transactions
If an `Author` is updated within the same transaction, Hibernate automatically issues an `UPDATE`.

---