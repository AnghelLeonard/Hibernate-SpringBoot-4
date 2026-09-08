---

# ⭐ Summary of Item 46: *How to Batch Inserts in Spring Boot Style*

### **Why batching matters**
Batching groups multiple INSERT/UPDATE/DELETE statements into fewer database round trips.  

-Example of 1,000 inserts →  
- Without batching, we have 1,000 round trips  
- With batch size, we have 30 34 round trips

This dramatically improves performance, especially for large data loads.

---

## ⚙️ **How to enable batching**

### **1. Set Hibernate batch size**
Recommended: **5–30**

```
spring.jpa.properties.hibernate.jdbc.batch_size=30
```

Do **not** confuse with `hibernate.jdbc.fetch_size` (generally avoid for MySQL/PostgreSQL).

---

## 🐬 **MySQL-specific optimizations**

Add these JDBC URL flags:

- **rewriteBatchedStatements=true**  
  Rewrites multiple INSERTs into a single multi-value INSERT.

- **cachePrepStmts=true**  
  Enables client-side prepared statement caching.

- **useServerPrepStmts=true**  
  Enables server-side prepared statements.

Final JDBC URL example:

```
jdbc:mysql://localhost:3306/bookstoredb?
cachePrepStmts=true
&useServerPrepStmts=true
&rewriteBatchedStatements=true
```

---

## 🧱 **Entity requirements**

To allow batching:

- **Avoid `GenerationType.IDENTITY`**  
  (MySQL AUTO_INCREMENT disables batching)

Use instead:

- `GenerationType.AUTO`
- Or manually assigned IDs

Avoid UUIDs for performance reasons.

---

## ⚠️ **Problems with Spring Data `saveAll()`**

`saveAll()` is convenient but **not ideal for batching**:

- Accumulates too many entities in Persistence Context → memory + performance issues  
- Uses `merge()` → triggers SELECTs before INSERTs  
- Creates unnecessary lists of persisted entities  
- Only flushes once at transaction commit

---

## 🛠️ **Recommended: Custom batching implementation**

A custom `saveInBatch()` method gives full control:

### **Key best practices**
- Commit **after each batch**
- Use **persist()** instead of merge()
- Avoid long-running transactions (better for MVCC)
- After each batch:
  - Flush and clear Persistence Context
  - Begin new transaction or control begin/commit cycles in a single transaction

### **Architecture**
- Create `BatchRepository` interface  
- Implement via `BatchRepositoryImpl` extending `SimpleJpaRepository`  
- Use a `BatchExecutor` component that:
  - Manages EntityManager
  - Controls begin/commit cycles
  - Persists entities in batches

### **Example behavior**
Processing 1,000 entities with batch size 30 → **34 batches + 34 flushes**.

---

## 🔍 **Final advice**
Batching can be silently disabled by misconfiguration.  
Use tools like **DataSource-Proxy** to verify actual batch execution.

---