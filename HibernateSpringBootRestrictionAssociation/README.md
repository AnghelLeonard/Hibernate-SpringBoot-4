---

# 📘 Summary of *Item 10: How to Filter Associations via a Hibernate-Specific @SQLRestriction Annotation*

## 🎯 Core Idea
Hibernate’s older filtering annotations like `@Where` and `@WhereJoinTable` are deprecated. The recommended modern replacement is `@SQLRestriction` (and `@SQLJoinTableRestriction` for join tables). These annotations let you append fixed SQL `WHERE` clauses to association queries.

However, they are **always applied**, **cannot be disabled**, and **cannot accept parameters**, making them less flexible than filters or `JOIN FETCH WHERE`.

---

## 🧩 When to Use `@SQLRestriction`
Use it **only when**:
- `JOIN FETCH WHERE` (Item 39) is not suitable, and
- `@NamedEntityGraph` (Items 7 & 8) doesn’t fit your use case.

It can also be useful for **soft delete** implementations.

---

## 📚 Example Scenario
The item uses an `Author` entity with a bidirectional lazy `@OneToMany` association to `Book`.

The goal is to lazily fetch:
- All books
- Books priced **≤ $20**
- Books priced **> $20**

### Implementation
The `Author` entity defines three lists:
- `books` — all books
- `cheapBooks` — `@SQLRestriction("price <= 20")`
- `restOfBooks` — `@SQLRestriction("price > 20")`

Hibernate automatically appends these restrictions to the SQL queries when the corresponding getter is accessed.

---

## 🛠️ Example Queries Triggered
### Fetch cheap books:
```sql
WHERE cb1_0.author_id=? AND (cb1_0.price <= 20)
```

### Fetch expensive books:
```sql
WHERE rob1_0.author_id=? AND (rob1_0.price > 20)
```

These are **lazy-loaded**, meaning they run in separate SELECTs after the author is fetched.

---

## ⚠️ Limitations of `@SQLRestriction`
- Always applied (no dynamic enabling/disabling)
- Cannot accept parameters
- Not suitable when you need to fetch parent + filtered children in a **single SELECT**
- Less flexible than Hibernate filters

---

## ✅ Better Alternative in Many Cases
`JOIN FETCH WHERE` is often superior because:
- It fetches parent + filtered children in one query
- It allows **parameter binding** (e.g., dynamic price thresholds)

---

## 🧠 Bottom Line
`@SQLRestriction` is a simple, static way to filter associations at the SQL level. It’s useful for fixed, always-on filters (like soft deletes or static price categories), but not for dynamic or parameterized filtering.

---