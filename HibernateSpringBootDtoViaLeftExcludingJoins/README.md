## **Summary of Item 43 — How to Write JOIN Statements**

### **1. Overview of SQL/JPQL JOIN Types**
This item explains the three major JOIN categories:

- **INNER JOIN** — returns rows that exist in both tables.
- **OUTER JOIN**  
  - **LEFT OUTER JOIN** — all rows from left table + matches from right  
  - **RIGHT OUTER JOIN** — all rows from right table + matches from left  
  - **FULL OUTER JOIN** — all rows from both tables (inclusive or exclusive)
- **CROSS JOIN** — Cartesian product; every row of A combined with every row of B.

It also notes that in JPQL/SQL:
- Writing `JOIN` means **INNER JOIN** by default.
- Writing `LEFT/RIGHT/FULL JOIN` means the corresponding **OUTER JOIN**.

---

## **2. JOINs as a Solution to LazyInitializationException**
JOINs are recommended to avoid `LazyInitializationException`, especially when fetching read‑only data.  
Combining JOINs with **DTO projections** (e.g., Spring interfaces) is highlighted as a best practice.

---

## **3. Examples Using Author–Book Entities**
This item uses a bidirectional `@OneToMany` (Author → Books) association and shows how to fetch DTOs like:

```java
public interface AuthorNameBookTitle {
    String getName();
    String getTitle();
}
```

For each JOIN type, the document provides:

- **JPQL examples**
- **Native SQL examples**
- **Variants depending on which table is considered A or B**
- **Filtering with WHERE clauses**

---

## **4. INNER JOIN**
- Fetches only authors who have books (or vice versa).
- Examples show JPQL and SQL versions.
- Demonstrates adding filters (e.g., genre, price).

---

## **5. LEFT JOIN**
- Returns all authors, even those without books.
- Shows JPQL and SQL examples.
- Mentions exclusive LEFT JOIN examples available on GitHub.

---

## **6. RIGHT JOIN**
- Opposite of LEFT JOIN: returns all books, even those without authors.
- JPQL and SQL examples included.
- Exclusive RIGHT JOIN examples referenced.

---

## **7. CROSS JOIN**
- No ON/WHERE clause → Cartesian product.
- Example: combining `Book` and `Format` entities with no relationship.
- Recommendation: **always use explicit JOINs** to avoid unwanted CROSS JOINs.

---

## **8. FULL JOIN**
- MySQL does **not** support FULL JOIN.
- Examples shown for PostgreSQL.
- Includes JPQL and SQL versions.

---

## **9. Simulating FULL JOIN in MySQL**
Since MySQL lacks FULL JOIN, the document shows how to simulate it using:

```
(LEFT JOIN)
UNION
(RIGHT JOIN WHERE left.id IS NULL)
```

Notes:
- `UNION` removes duplicates.
- Use `UNION ALL` if duplicates are expected.
- JPA does not support UNION → must use native SQL.

---

## **10. Key Takeaways**
- Prefer **explicit JOINs** to avoid accidental CROSS JOINs.
- Use JOINs + DTOs for efficient read‑only queries.
- FULL JOIN requires workarounds in MySQL.
- Always inspect generated SQL when using Criteria API.

---