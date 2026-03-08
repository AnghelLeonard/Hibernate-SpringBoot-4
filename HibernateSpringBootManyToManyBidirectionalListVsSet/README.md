---

# 📘 Summary of Item 5: *Why Set Is Better than List in @ManyToMany*

## 🎯 Core Idea  
Hibernate internally treats a `@ManyToMany` as **two unidirectional @OneToMany associations**.  
Because of this, using a **List** causes Hibernate to delete and reinsert *all* join-table rows whenever an element is removed or reordered.  
Using a **Set** avoids this and results in **far fewer SQL statements**.

---

# 🔍 Why List Performs Poorly

When using `List<Book>` and removing one book:

- Hibernate **deletes all rows** in the join table for that parent.
- Then it **reinserts all remaining rows** to match the in-memory order.
- This happens because List implies **positional order**, and Hibernate must preserve it.

### Example SQL (List):
```
DELETE FROM author_book_list WHERE author_id = ?
INSERT INTO author_book_list (author_id, book_id) VALUES (?, ?)
INSERT INTO author_book_list (author_id, book_id) VALUES (?, ?)
...
```

👉 **More books = more reinserts = worse performance.**

---

# 🌟 Why Set Performs Better

When using `Set<Book>`:

- Removing an element triggers **only one DELETE** in the join table.
- No reinserts.
- No reordering logic.

### Example SQL (Set):
```
DELETE FROM author_book_set
WHERE author_id = ? AND book_id = ?
```

👉 **Always a single DELETE. Extremely efficient.**

---

# 📌 Key Takeaways

### ✔ Always use `Set` for @ManyToMany  
- Avoids unnecessary DELETE + INSERT cycles  
- Avoids reordering overhead  
- Produces minimal SQL  
- Much better scalability  

### ✔ When order matters  
You can still use `Set` and apply:

- `@OrderBy` → orders results via SQL (`ORDER BY`)  
- `@OrderColumn` → stores order in a dedicated column (less common for ManyToMany)

Hibernate will internally use a `LinkedHashSet` to preserve the order returned by the query.
---