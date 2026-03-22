---

# 📘 Summary of *Item 17: How to Clone Entities*

This item explains practical techniques for cloning JPA/Hibernate entities. Particularly when working with relationships such as `@ManyToMany`. 
It focuses on two scenarios

---

## 🧩 1. Cloning the Parent Entity Only (and Reusing Children)

**Use case:**  
You want to create a new `Author` who shares the same books as an existing author, but has different personal attributes (e.g., name, age).

**Approach:**  
- Add a **copy constructor** to `Author` that copies only selected fields (e.g., `genre`) and **associates existing books** using `books.addAll()`.
- Avoid using the helper method `addBook()` because it triggers extra `SELECT` statements due to bidirectional synchronization.
- After cloning, set the new author’s specific fields (name, age) and save.

**Result:**  
- Only the new `Author` is inserted.
- No new `Book` rows are created.
- A new set of entries is added to the join table `author_book`.

---

## 📚 2. Cloning the Parent *and* the Child Entities

**Use case:**  
You want to duplicate an author *and* create fresh copies of all their books.

**Approach:**  
- Add a copy constructor to `Book` that clones all its fields.
- Modify the `Author` copy constructor to:
  - Create new `Book` instances via `new Book(book)`
  - Add them using `addBook()`, which synchronizes both sides of the relationship.

**Result:**  
- New `Author` and new `Book` rows are inserted.
- New join-table entries link the cloned author to the cloned books.

---

## 🔀 3. Combining Both Behaviors

You can make cloning flexible by adding a boolean flag:

```java
public Author(Author author, boolean cloneChildren)
```

- If `cloneChildren == false`: reuse existing books (`addAll`)
- If `cloneChildren == true`: deep-clone each book

This allows the service layer to choose the cloning strategy dynamically.

---

## 🧠 Key Takeaways

- **Copy constructors** give fine-grained control over what gets cloned.
- **Shallow cloning** (reusing children) avoids unnecessary inserts and is often sufficient.
- **Deep cloning** is useful when you need independent copies of child entities.
- **Avoid extra SELECTs** by using `addAll()` instead of helper methods when you don’t need bidirectional synchronization.
- The examples use `IDENTITY` generation, so unsaved entities show `null` IDs until persisted.

---