---

# 📘 Summary of *Item 22: Why Use Read‑Only Entities When You Plan to Update Later*

## 🎯 Core Idea  
When you load an entity in one transaction and update it in a later transaction (a common pattern in web apps), it’s more efficient to load the entity **in read‑only mode** during the first step. This avoids unnecessary memory and CPU overhead in Hibernate.

---

# 🔍 Why Read‑Only Mode Matters

## 1. **Read‑Write Mode (Default)**
When you fetch an entity normally:
- Hibernate marks it as **MANAGED**.
- It stores the **hydrated state** (a snapshot of the entity’s values).
- Even if you don’t modify it in that transaction, Hibernate:
  - Keeps the snapshot in memory.
  - Performs **dirty checking** at flush time.
  - Scans the entity for changes.

### 🔻 Drawbacks
- Wasted memory (hydrated state stored unnecessarily)
- Wasted CPU (dirty checking and GC work)
- No benefit, since the entity becomes **detached** anyway when returned

---

## 2. **Read‑Only Mode**
When you fetch the entity with `@Transactional(readOnly = true)`:
- Hibernate marks it as **READ_ONLY**
- **No hydrated state** is stored
- **No dirty checking**
- **No automatic flush**

### 🔺 Benefits
- Lower memory usage  
- Lower CPU usage  
- Faster transaction execution  

This is ideal when the entity is only being *read* in the current transaction and will be modified later in a different one.

---

# 🔄 Updating the Entity Later
After the read-only fetch:
1. The entity becomes **detached**.
2. You modify it in memory.
3. You merge it back using `save()` or `EntityManager.merge()`.

Hibernate then:
- Performs a `SELECT` to reattach the entity
- Performs an `UPDATE` to apply changes

This is the correct and efficient workflow.

---

# 🌐 Real‑World Context: HTTP Long Conversations
This pattern is common in web apps:
- **Request 1:** Load profile → user sees data  
- **User thinks/edits**  
- **Request 2:** Submit changes → server updates DB  

Read‑only mode optimizes the first request.

---

# ⚠️ Note on Extended Persistence Context
The item warns that using an **Extended Persistence Context** in Spring is tricky and should be avoided unless you fully understand it.

---