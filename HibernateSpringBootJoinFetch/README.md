## **Summary of Item 39 — How to Effectively Fetch Parent and Association in One SELECT**

### **Core Problem**
You have two entities — `Author` and `Book` — in a **bidirectional lazy @OneToMany / @ManyToOne** association.  
By default:
- Loading an `Author` does **not** load its `books`
- Loading a `Book` does **not** load its `author`

This leads to:
- **Two SELECTs** instead of one  
- Risk of **LazyInitializationException** if the second SELECT happens outside an active Hibernate session

---

## **Goal**
Perform these operations efficiently:
1. Fetch an **Author by name**, including their **Books**
2. Fetch a **Book by ISBN**, including its **Author**

And do it in **one SQL SELECT**, not two.

---

## **Why Not Use EAGER Fetching?**
The text strongly warns:

> **DON’T switch associations to EAGER.**

Reasons:
- EAGER cannot be overridden at query time  
- It often leads to **Cartesian products**, **huge result sets**, and **performance issues**  
- LAZY is safer and more flexible

---

## **Correct Solution: Use `JOIN FETCH` at Query Level**
`JOIN FETCH` is a JPA feature that initializes associations in a **single SELECT** while keeping the entity mapping LAZY.

### **Example Repositories**

#### **Fetch Author + Books**
```java
@Query("SELECT a FROM Author a JOIN FETCH a.books WHERE a.name = ?1")
Author fetchAuthorWithBooksByName(String name);
```

#### **Fetch Book + Author**
```java
@Query("SELECT b FROM Book b JOIN FETCH b.author WHERE b.isbn = ?1")
Book fetchBookWithAuthorByIsbn(String isbn);
```

### **Generated SQL**
Both queries produce a **single JOIN-based SELECT**, loading parent + association together.

---

## **Performance Considerations**
### **JOIN FETCH trade-offs**
- JOINs can produce **Cartesian products**  
  - Example: 100 authors × 5 books = **500 rows**
- But LAZY fetching can cause **N+1 queries**  
  - Example: 100 authors → **100 secondary queries**

**Rule of thumb from the text:**
> It is better to have a large Cartesian product than a large number of database round trips.

But:
- If you can avoid a huge Cartesian product with a few targeted queries, do that instead.

---

## **Key Takeaways**
- Keep associations **LAZY** at the entity level  
- Use **JOIN FETCH** when you need to load associations for modification  
- Use **JOIN + DTO** when the data is read-only  
- Avoid EAGER fetching — it cannot be overridden and often harms performance  
- JOIN FETCH solves both:
  - LazyInitializationException  
  - Extra SELECTs  

---