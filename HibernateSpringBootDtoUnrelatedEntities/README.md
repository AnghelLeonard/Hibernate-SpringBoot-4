## **Summary of Item 42: How to Fetch DTO from Unrelated Entities**  


### **Core Idea**
Thi item explains how to fetch a DTO (Spring projection) that combines data from **two unrelated JPA entities**—entities with **no explicit relationship** defined between them—using **Hibernate’s support for explicit joins on unrelated entities** (introduced in Hibernate 5.1).

### **Context**
- Two tables/entities: **Author** and **Book**  
- They have **no foreign key relationship**, but they share a common column: **name** (the author’s name)
- Goal: Retrieve a DTO containing:
  - Author name  
  - Book title  
  - Filtered by a given book price

### **Key Technique**
Hibernate allows **explicit joins** between unrelated entities using SQL-like syntax.

### **Example JPQL Query**
The repository method uses a custom `@Query`:

```java
@Query(value = "SELECT a.name AS name, b.title AS title "
             + "FROM Author a INNER JOIN Book b ON a.name = b.name "
             + "WHERE b.price = ?1")
List<BookstoreDto> fetchAuthorNameBookTitleWithPrice(int price);
```

### **Generated SQL**
```sql
SELECT a1_0.name, b1_0.title
FROM author a1_0
JOIN book b1_0 ON a1_0.name = b1_0.name
WHERE b1_0.price = ?
```

### **Takeaway**
Even without JPA relationships, you can join entities by manually specifying the join condition in JPQL. This is useful when:
- Legacy schemas lack foreign keys  
- Entities are intentionally kept independent  
- You need flexible DTO projections across tables

---