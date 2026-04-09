---

## Summary of Item 35 – *Fetching DTOs via Hibernate Transformers*

### **Purpose**
Thi item explains how to use **Hibernate result transformers**—specifically **TupleTransformer**—to map query results directly into **DTOs (Data Transfer Objects)** instead of returning entities. This is useful when you want only a subset of fields (e.g., name + age) and want to avoid exposing full entities.

---

## **Key Concepts**

### **1. Hibernate Result Transformers**
- Provide a **custom way to transform query results**.
- Work with both **JPQL** and **native SQL**.
- Two main functional interfaces:
  - **TupleTransformer** – transforms each row (tuple)
  - **ResultListTransformer** – transforms the entire result list

---

## **2. Example Entity**
The example uses an `Author` entity with fields:
- `id`
- `age`
- `name`
- `genre`

---

## **3. DTO Approaches**

### **A. DTO with Constructor (No Setters)**
```java
public class AuthorDtoNoSetters {
    private final String name;
    private final int age;
}
```

**Mapping logic:**
- Use `setTupleTransformer` to manually instantiate the DTO.
- Query:
  ```java
  SELECT a.name as name, a.age as age FROM Author a
  ```

### **B. DTO with Setters (No Constructor)**
```java
public class AuthorDtoWithSetters {
    private String name;
    private int age;
}
```

**Mapping logic:**
- Instantiate empty DTO, then call setters inside the transformer.

---

## **4. JPQL + TupleTransformer Usage**
Both examples follow this pattern:

```java
entityManager.createQuery("SELECT ...")
    .unwrap(org.hibernate.query.Query.class)
    .setTupleTransformer((tuples, aliases) -> {
        // map tuple to DTO
    })
    .getResultList();
```

---

## **5. Native SQL Support**
- Same approach works with native queries.
- Use:
  ```java
  createNativeQuery(...)
      .unwrap(org.hibernate.query.NativeQuery.class)
  ```

---

## **6. SQL Generated**
Both DTO-fetching methods produce the same SQL:
```
SELECT a1_0.name, a1_0.age FROM author a1_0
```

---

## **Core Takeaway**
Hibernate’s **TupleTransformer** allows you to efficiently fetch **partial data** into **custom DTOs**, avoiding full entity loading and giving you complete control over how query results are mapped.

---