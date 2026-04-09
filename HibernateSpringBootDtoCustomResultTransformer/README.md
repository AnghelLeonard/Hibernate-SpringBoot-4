---

## **Summary of Item 36: Fetching DTOs with a Custom Hibernate Transformer**

**Core idea**  
When you need to fetch complex DTOs involving parent–child relationships 
(e.g., Author → Books) using a native SQL query, 
Hibernate’s built‑in transformers are not enough. Instead, you must
 implement a **custom TupleTransformer and ResultListTransformer** 
to assemble the DTO graph manually.

---

### **Problem Context**
You have two entities

- **Author**: id, name, genre, age, books  
- **Book**: id, title, isbn  
- Relationship: **@OneToMany (lazy), bidirectional**

You want a DTO structure:

- **AuthorDto**: authorId, name, age, List<BookDto>
- **BookDto**: bookId, title

A SQL JOIN returns a flattened result set, so you need to rebuild the hierarchical DTO structure manually.

---

## **Why a Custom Transformer Is Needed**
Hibernate’s built‑in transformers cannot:

- Group rows by author
- Aggregate multiple books under the same author
- Build nested DTO lists

Thus, you implement:

- **TupleTransformer** → transforms each row (Object[])  
- **ResultListTransformer** → transforms the entire result list (e.g., deduplicate authors)

---

## **How the Custom Transformer Works**

### **1. transformTuple(Object[] row, String[] aliases)**  
Executed for each row:

- Extract authorId, name, age  
- Check if the author already exists in a map  
- If not, create a new AuthorDto  
- Create a BookDto from the row  
- Add the book to the author’s list  
- Store/update the author in the map  
- Return the AuthorDto (ignored later by transformList)

### **2. transformList(List list)**  
Executed once at the end:

- Returns **all unique AuthorDto objects** from the map

This ensures authors are not duplicated even though the SQL JOIN returns multiple rows per author.

---

## **DAO Implementation**
A native SQL query is created:

```sql
SELECT a.id AS author_id, a.name AS name, a.age AS age,
       b.id AS book_id, b.title AS title
FROM author a
JOIN book b ON a.id = b.author_id
```

Hibernate unwraps the query to `NativeQuery`, then applies:

- `.setTupleTransformer(abt)`
- `.setResultListTransformer(abt)`

The result is a **List<AuthorDto>** with nested books.

---

## **REST Endpoint**
A simple controller exposes:

```
GET /authorWithBook
```

Returning JSON like:

```json
[
  {
    "id": 1,
    "name": "Mark Janel",
    "age": 23,
    "books": [
      {"id": 3, "title": "The Beatles Anthology"},
      {"id": 7, "title": "Anthology Of An Year"},
      {"id": 8, "title": "Anthology From A to Z"},
      {"id": 9, "title": "Past Anthology"}
    ]
  },
  ...
]
```

---

## **Key Takeaways**
- Hibernate’s built‑in transformers cannot build nested DTO structures from JOIN queries.
- A **custom transformer** is required to:
  - Group rows by parent entity
  - Build child DTO lists
  - Deduplicate parent DTOs
- Implementing both **TupleTransformer** and **ResultListTransformer** is the modern approach (post‑Hibernate 5.2).
- This pattern is essential for efficient DTO projections in complex domain models.

---