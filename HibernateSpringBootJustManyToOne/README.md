---

# 📘 Item 3 summary - *How Efficient Is the Unidirectional @ManyToOne Association*

## 🧩 Core Idea  
Item 3 evaluates the performance and behavior of a **unidirectional @ManyToOne** association (e.g., many Books → one Author) and compares it implicitly to the alternatives discussed earlier. 
The conclusion is straightforward
👉 **Unidirectional @ManyToOne is highly efficient** and often preferable when you don’t need bidirectional navigation.

---

# 🏗️ Why @ManyToOne Is Efficient

### 🔹 1. **Foreign Key Lives on the Child Side**  
The child entity (Book) owns the foreign key (`author_id`).  
This means:
- No junction table  
- No extra INSERT/DELETE cycles  
- No collection synchronization overhead  

This is true for both unidirectional and bidirectional mappings.

---

# ➕ Adding a New Book to an Author

### ✔️ Most efficient approach:
```java
Author author = authorRepository.getReferenceById(4L);
Book book = new Book();
book.setAuthor(author);
bookRepository.save(book);
```

### Why it’s efficient:
- `getReferenceById()` avoids a SELECT (lazy proxy)
- Only **one INSERT** is executed  
  → The `author_id` is set directly in the INSERT statement

---

# 📚 Fetching All Books of an Author

### ✔️ Simple JPQL query:
```java
SELECT b FROM Book b WHERE b.author.id = :id
```

### Performance:
- Only **one SELECT**  
- Returned list is **not managed** as a collection, but each Book is managed individually  
- Updates to books trigger normal Hibernate dirty checking

---

# 📄 Pagination Support

Using Spring Data pagination:
- Executes **two SELECTs** (one for data, one for count)
- Still efficient and scalable  
- Recommended when the author has many books

---

# ➕ Adding a Book After Fetching Books

### Pattern:
1. Fetch all books  
2. Create a new Book  
3. Set its author  
4. Save it  

### SQL:
- One SELECT  
- One INSERT  
👉 Still very efficient

---

# ❌ Deleting a Book

Deleting a book from the fetched list:
- One SELECT (to fetch books)
- One DELETE (for the removed book)

No extra updates, no junction table cleanup.

---

# 🏁 Final Verdict

### ⭐ Unidirectional @ManyToOne is:
- **Simple**
- **Fast**
- **SQL-efficient**
- **Easy to maintain**
- **Free of the performance penalties** seen in unidirectional @OneToMany

### When to use it:
- When you don’t need to navigate from parent → children  
- When performance and simplicity matter  
- When avoiding unnecessary collection management overhead

---