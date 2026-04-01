---

# 📘 Summary of Item 30: *How to Fetch All Entity Attributes via Spring Projection*

This item explains multiple ways to fetch **all basic attributes** of an entity (Author) using **Spring Data JPA projections**, comparing their **performance** and **impact on the Persistence Context**.

---

## 🧩 Context

The `Author` entity has four fields: **id, age, genre, name**.  
Goal: Fetch a **read‑only DTO** containing *all* these attributes efficiently.

Spring offers several ways to do this, but they differ significantly in performance and how they interact with the **Persistence Context (PC)**.

---

# 🔍 Approaches Compared

Below is a breakdown of each approach, how it behaves, and its performance implications.

---

## 1. **Fetching Read‑Only Entities via `findAll()`**
- Uses Spring Data’s built‑in `findAll()` with `@Transactional(readOnly = true)`.
- Loads **entities** into the Persistence Context in **READ_ONLY** mode.
- No dirty checking, no flush.
- **PC contains 5 managed entities**.
- ❗ Slowest approach in benchmarks.
- ✔ Good only if you *intend to modify entities later*.
- ❌ Not ideal for DTO‑style read‑only use cases.

---

## 2. **Interface‑Based Closed Projection via Query Builder**
```java
List<AuthorDto> findBy();
```
- Generates SQL selecting only the needed columns.
- **PC remains empty**.
- ✔ Very efficient.
- ✔ Simple to implement.
- ⚠ Avoid returning `List<Object[]>` — it loads entities into PC unnecessarily.

---

## 3. **JPQL with `@Query` — Improper Version**
```java
@Query("SELECT a FROM Author a")
List<AuthorDto> fetchAsDto();
```
- Although the SQL selects only columns, JPQL `SELECT a` loads **entities**.
- **PC contains 5 read‑only entities**.
- Spring must also build DTOs → extra overhead.
- ❌ Inefficient and misleading.

---

## 4. **JPQL with Explicit Column List**
```java
@Query("SELECT a.id AS id, a.age AS age, a.name AS name, a.genre AS genre FROM Author a")
List<AuthorDto> fetchAsDtoColumns();
```
- Explicit column selection prevents entity loading.
- **PC remains empty**.
- ✔ Very efficient.
- ✔ Recommended when using JPQL.

---

## 5. **Native Query with `@Query`**
```java
@Query(value = "SELECT id, age, name, genre FROM author", nativeQuery = true)
List<AuthorDto> fetchAsDtoNative();
```
- Straightforward SQL.
- **PC remains empty**.
- ✔ Fast, but slightly slower than JPQL explicit columns in benchmarks.

---

# ⚡ Performance Comparison (from Figure 3‑9)

For 50 authors, benchmarked using JMH:

| Approach | Performance |
|---------|-------------|
| **Fastest** | Native query, JPQL with explicit columns |
| **Middle** | Query builder projection |
| **Slowest** | Fetching read‑only entities (`findAll()`) |

Hardware used: Windows 11, Intel i7 2.10GHz, 6GB RAM.  
Both app and MySQL ran on the same machine.

---

# 🧠 Key Takeaways

- If you need **all fields** but want **maximum performance**, use:
  - **JPQL with explicit column list**, or  
  - **Native query**.
- If you want simplicity and still good performance:
  - **Query builder projection** (`findBy()`).
- Avoid:
  - JPQL `SELECT a` when returning DTOs.
  - Fetching read‑only entities unless you plan to modify them later.

---