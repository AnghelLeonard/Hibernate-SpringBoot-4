---

# 📘 Summary of Item 29: *Why to Pay Attention to Spring Projections That Include Associated Collections*

## 🎯 Core Problem
When using **Spring Data projections** to fetch **parent + associated collections** (e.g., `Author` + `Book`) in JPA/Hibernate, developers often assume projections avoid entity loading and the Persistence Context.  
This assumption is **wrong** for many projection styles, leading to:

- ❌ **N+1 SELECT problems**  
- ❌ **Unnecessary entity loading**  
- ❌ **Persistence Context pollution**  
- ❌ **Broken or flattened data structures**

The item benchmarks **seven approaches** and shows how each behaves.

---

# 🧩 Approaches Compared

## 1. **Nested Spring Closed Projection + Query Builder**
**Example:** `AuthorDto` containing a nested `BookDto` list.

### Behavior
- Triggers **N+1 queries** (1 for authors + 1 per author for books)
- Loads **entities** into Persistence Context
- Produces correct JSON structure

### Verdict
🚫 **Worst performance**. Avoid.

---

## 2. **Nested Projection + Explicit JPQL**
**Example:**  
```java
SELECT a.name, a.genre, b AS books
FROM Author a JOIN a.books b
```

### Behavior
- Single SELECT  
- Still loads **full entities**  
- Loses tree structure (each book becomes a separate row)

### Verdict
⚠️ Better than Query Builder, but still loads too much and breaks structure.

---

## 3. **JOIN FETCH**
**Example:**  
```java
SELECT a FROM Author a JOIN FETCH a.books
```

### Behavior
- Single SELECT  
- Loads **full entities**  
- Preserves **tree structure**  
- Must use `Set` to avoid duplicates

### Verdict
👍 Good when you *need entities*, but still loads more than needed.

---

## 4. **Simple Closed Projection (Flat DTO)**
**Example:**  
```java
SELECT a.name, a.genre, b.title
```

### Behavior
- Single SELECT  
- **Bypasses Persistence Context**  
- Fetches only needed columns  
- But produces **flat rows** (no nested structure)

### Verdict
⚡ Very efficient, but you must reshape the data manually.

---

## 5. **List<Object[]> + Manual DTO Transformation**
### Behavior
- Single SELECT  
- Bypasses Persistence Context  
- Custom transformer rebuilds the tree structure

### Verdict
💡 **Best balance**: efficient + correct structure.  
Requires custom code.

---

## 6. **List<Object[]> with IDs + Transformer**
Same as above, but includes IDs to rebuild DTOs properly.

### Verdict
🏆 **Highly recommended** for performance + structure.

---

## 7. **JdbcTemplate**
### Behavior
- Manual SQL  
- Manual mapping  
- Bypasses Persistence Context  
- Fastest in benchmarks

### Verdict
🚀 **Fastest overall**, but requires more boilerplate.

---

# 📊 Performance Ranking (Fastest → Slowest)
1. **JdbcTemplate**  
2. **Object[] + Transformer**  
3. **Simple Projection (flat)**  
4. **JOIN FETCH**  
5. **Explicit JPQL + nested projection**  
6. **Query Builder + nested projection** (worst)

---

# 🧠 Key Takeaways

### ✔ Projections do **not** guarantee bypassing the Persistence Context  
Nested projections often force Hibernate to load entities.

### ✔ JOIN FETCH preserves structure but loads too much  
Good for entity graphs, not for lightweight DTOs.

### ✔ Flat projections are efficient but lose hierarchy  
You must rebuild the structure manually.

### ✔ The fastest and cleanest solutions:
- **JdbcTemplate**
- **Custom DTO transformer using Object[]**

### ✔ Avoid nested closed projections for associated collections  
They cause N+1 and unnecessary entity loading.

---