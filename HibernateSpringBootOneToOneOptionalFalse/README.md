---

# 📘 Summary of Item 11: *Optimizing `@OneToOne` with `@MapsId`*

This document explains how to optimize **unidirectional** and **bidirectional** `@OneToOne` JPA associations by using the `@MapsId` annotation. It highlights performance drawbacks of the standard mappings and shows how `@MapsId` solves them.

---

## 🔹 1. Regular Unidirectional `@OneToOne`

**Setup:**  
- `Author` = parent  
- `Book` = child  
- Child holds a foreign key (`author_id`)

**Issue:**  
When fetching a Book by its Author, the parent **does not know the child’s ID**, so Hibernate must run an extra query:

- Even if both entities are in the **Second Level Cache**, Hibernate still hits the database to find the child.

**Result:**  
Unnecessary queries → performance penalty.

---

## 🔹 2. Regular Bidirectional `@OneToOne`

**Setup:**  
- Parent (`Author`) has `mappedBy` reference to child (`Book`)

**Issue:**  
Even with `FetchType.LAZY`, fetching the parent triggers **two queries**:

1. Fetch Author  
2. Fetch Book  

Hibernate must load the child to know whether the parent’s reference should be `null` or a proxy.

**Result:**  
Extra query even when the child isn’t needed → wasted resources.

---

## 🔹 3. Using `@MapsId` to Optimize

`@MapsId` allows the child entity to **share the same primary key** as the parent.  
This means:

- `Book.id` = `Author.id`
- No `@GeneratedValue` on the child
- The foreign key is also the primary key

**Benefits:**

### ✅ Eliminates extra queries  
The parent now *knows* the child’s ID, so fetching the child can use `findById()` directly.

### ✅ Works perfectly with Second Level Cache  
If the Book is cached, Hibernate retrieves it **without** a database round trip.

### ✅ No unnecessary child loading  
Fetching the parent no longer forces Hibernate to load the child.

### ✅ Reduced memory footprint  
Only one key is stored instead of both a primary key and a foreign key.

---

## 🔹 4. Example Behavior with `@MapsId`

When saving a new Book:

```java
book.setAuthor(author);
bookRepository.save(book);
```

Hibernate inserts:

```
INSERT INTO book (isbn, title, author_id)
VALUES (?, ?, ?)
```

The `author_id` becomes the Book’s primary key.

Fetching the Book becomes trivial:

```java
bookRepository.findById(author.getId());
```

---

## 🎯 Key Takeaway

`@MapsId` is the **recommended approach** for `@OneToOne` associations because it:

- Removes unnecessary queries  
- Improves cache efficiency  
- Avoids forced eager-like behavior  
- Simplifies the model by sharing primary keys  

It solves the major performance drawbacks of both unidirectional and bidirectional `@OneToOne` mappings.

---