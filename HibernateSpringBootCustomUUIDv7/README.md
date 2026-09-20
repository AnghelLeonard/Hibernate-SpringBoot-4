---

# 📘 Summary of *Item 21: How to Use Direct Fetching*

## 🎯 Core Idea
Direct fetching—loading an entity by its ID—is the most efficient way to retrieve a single JPA entity **when you already know the identifier** and **don’t plan to navigate lazy associations** in the current Persistence Context.

---

# 🔍 Key Concepts

## 💤 Lazy vs. ⚡ Eager Associations
- **LAZY** (default for `@OneToMany`, `@ManyToMany`): Not loaded until accessed.
- **EAGER** (default for `@OneToOne`, `@ManyToOne`): Always loaded with the entity.
- Direct fetching still respects these defaults, which can cause unnecessary queries if not managed carefully.

**Best practice:**  
Keep *all* associations LAZY and manually fetch what you need.

---

# 🛠️ Ways to Fetch by ID

## 1. **Spring Data `findById()`**
- Returns `Optional<T>`.
- Internally uses `EntityManager.find()`.
- Generates a simple `SELECT ... WHERE id=?`.

## 2. **EntityManager.find()**
- Injected via `@PersistenceContext`.
- Same SQL as Spring Data.

## 3. **Hibernate Session.find()**
- Requires unwrapping the Hibernate `Session`.
- Same SQL as above.

All three methods behave identically in terms of SQL and caching.

---

# 🧠 Hibernate’s Session-Level Repeatable Reads
Hibernate guarantees that within a single Persistence Context:
- The **first** fetch loads the entity into the First-Level Cache.
- **Subsequent** fetches of the same ID return the cached entity.
- This prevents lost updates and ensures consistent reads.

### Example:
Calling:
- `findById(1)`
- `find(Author.class, 1)`
- `findViaSession(Author.class, 1)`

…within the same transaction triggers **only one SQL SELECT**.

---

# 🧪 Explicit JPQL/SQL Queries Behave Differently
If you write:

```java
@Query("SELECT a FROM Author a WHERE a.id = ?1")
```

Hibernate **still executes the SQL**, even if the entity is already in the Persistence Context.

But:
- The returned entity is replaced with the cached one.
- The fresh database snapshot is ignored.

So:
- **Two SELECTs** occur if you call `findById()` and then a JPQL query.

---

# 🔄 Behavior in Concurrent Transactions
A detailed example shows:
- Transaction A loads Author(id=1) → gets “Mark Janel”.
- Transaction B loads and updates the same author → changes name to “Alicia Tom”.
- Back in Transaction A:
  - `findById()` returns “Mark Janel” (cached).
  - JPQL/SQL entity queries also return “Mark Janel” (snapshot ignored).
  - **But JPQL/SQL projections** (e.g., `SELECT a.name`) return **fresh DB values** (“Alicia Tom”).

### Why?
- Entity queries respect Hibernate’s session-level repeatable reads.
- Projections do **not**; they always hit the database.

---

# 🧩 Isolation Levels Matter
- Under **READ_COMMITTED**, projections return the latest DB state.
- Under **REPEATABLE_READ**, projections also return the cached value.

Hibernate’s session-level repeatable reads ≠ database isolation levels.

---

# 📦 Loading Multiple Entities by ID
Options include:
- `findAllById()`
- JPQL `IN` queries
- Spring Data `Specification`
- Hibernate’s `MultiIdentifierLoadAccess` (supports batching and optional session checks)

All benefit from session-level repeatable reads.

---

# 🧭 Practical Recommendations
- Use `findById()` or `EntityManager.find()` for ID-based fetching.
- Avoid explicit JPQL/SQL for simple ID lookups.
- Keep associations LAZY and fetch manually when needed.
- Use projections only when you *want* the latest DB state.
- For multiple IDs, prefer `IN` queries or Hibernate batch loaders.

---