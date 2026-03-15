---

# 📘 **Summary of Item 7 — How to Fetch Associations via JPA Entity Graphs**

Item 7 explains how **JPA Entity Graphs** provide a powerful, flexible, and efficient way to control which associations are fetched when loading entities. They help you avoid common performance pitfalls like the **N+1 query problem**, and they give you fine‑grained control without modifying your domain model.

---

## 🎯 **Why Entity Graphs Matter**
Entity Graphs let you specify, at query time, **which relationships should be eagerly fetched**, overriding the default `LAZY` or `EAGER` settings. This avoids:
- Unpredictable lazy loading
- Excessive joins
- N+1 SELECT explosions
- Hard‑coded fetch strategies in entity annotations

They are especially useful when different use cases require different fetch plans.

---

## 🧩 **Two Types of Entity Graphs**

### **1. Fetch Graph**
- Only attributes explicitly listed in the graph are fetched eagerly.
- Everything else remains lazy.
- Ideal for **precise, minimal fetch plans**.

### **2. Load Graph**
- Attributes in the graph are fetched eagerly.
- Attributes not listed fall back to their default fetch type.
- Useful when you want to *add* eager associations without overriding everything.

---

## 🛠️ **How to Define Entity Graphs**

### **A. Named Entity Graphs (via annotations)**
Defined directly on the entity:

```java
@NamedEntityGraph(
    name = "author-books",
    attributeNodes = @NamedAttributeNode("books")
)
```

Used in repositories or EntityManager queries.

### **B. Dynamic Entity Graphs (built at runtime)**

```java
EntityGraph<Author> graph = em.createEntityGraph(Author.class);
graph.addAttributeNodes("books");
```

More flexible — no need to modify entity classes.

---

## 🚀 **Using Entity Graphs in Queries**

### **JPQL**
```java
TypedQuery<Author> q = em.createQuery(
    "SELECT a FROM Author a WHERE a.id = :id", Author.class);
q.setHint("jakarta.persistence.fetchgraph", graph);
```

### **Spring Data JPA**
```java
@EntityGraph(value = "author-books", type = EntityGraphType.FETCH)
Author findByName(String name);
```

---

## ⚡ **Performance Benefits**
Entity Graphs help you:
- Avoid N+1 queries by preloading required associations
- Reduce unnecessary joins
- Keep entities configured with `LAZY` loading while still fetching what you need
- Tailor fetch plans per use case without touching the domain model

---

## 🧠 **Best Practices**
- Keep default fetch types **LAZY** in your entities.
- Use Entity Graphs to **opt‑in** to eager loading when needed.
- Prefer **FETCH graphs** for predictable performance.
- Use **Named graphs** for common fetch plans; dynamic graphs for special cases.

---