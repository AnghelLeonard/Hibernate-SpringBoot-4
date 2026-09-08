---

# 🧩 Summary of Item 10: *How to handle a huge Cartesian Product  via aggregated joins*

### ⭐ Core Problem  
A large native SQL query joining **Author**, **Book**, **Tag**, **Publisher**, **Review**, and **Reviewer** produces a **massive Cartesian Product** returning flat `List<Object[]>` results with no hierarchical structure.

---

## 🚀 Proposed Solution: Split the Query into Multiple JOIN FETCH Queries  
Instead of one huge native SQL query, we can **split the workload into several SELECT statements**—each using `JOIN FETCH`— may dramatically improves performance and preserves hierarchy.

### 1. **Fetch Authors + Books**  
One-to-many association.

### 2. **Fetch Authors + Tags**  
Many-to-many association.

### 3. **Fetch Books + Publishers + Reviews**  
Many-to-one + one-to-many.

### 4. **Fetch Reviews + Reviewers**  
Many-to-many.

All queries run inside the **same Persistence Context** and **read-only transaction**, allowing Hibernate proxies to progressively populate the full object graph.

### ⏱ Performance sample 
- **Native SQL:** ~8757 ms  
- **Hibernate/JPA JOIN FETCH approach:** **200–300 ms**  
- **Blaze Persistence MULTISET:** ~600–700 ms  

Hibernate/JPA is the fastest in this example.

---

## 🧠 Why It Works  
- Avoids Cartesian Products  
- Preserves hierarchical structure  
- Uses Hibernate’s lazy proxies to merge data from multiple queries  
- No need for custom mappers  
- Still read-only, so Persistence Context overhead is minimal

---

## 🔧 Alternative Approaches  
- **Blaze Persistence MULTISET** (maintains hierarchy, slower than JPA here)  
- **jOOQ MULTISET** (recommended via “jOOQ Masterclass”)  
- **JPA Entity Graphs** (another option, example available on GitHub, if you prefer entity graphs)

---

## 📌 Final Takeaway  
Splitting large hierarchical loads into multiple `JOIN FETCH` queries inside a single read-only Persistence Context can **dramatically outperform** a single native SQL query.  
However, this is **not universally true**—benchmarking is essential because results depend on schema size, cardinality, database, and hardware.

---