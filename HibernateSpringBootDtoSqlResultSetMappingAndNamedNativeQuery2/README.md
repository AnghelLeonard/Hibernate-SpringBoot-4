---

## Summary of Item 34 — *Fetching DTOs with @SqlResultSetMapping and @NamedNativeQuery*

### **Purpose of the Item**
This item explains how to use **JPA’s @SqlResultSetMapping** together with **@NamedNativeQuery** to fetch
- Scalar values  
- DTOs via constructor mapping  
- Entities via entity mapping  

It focuses on Spring Boot + Hibernate usage.

---

## **1. Scalar Mapping (ColumnResult)**

**Goal:** Fetch a single column (e.g., `name`) from the `author` table.

**Key points:**
- Use `@SqlResultSetMapping` with `@ColumnResult`.
- Define a `@NamedNativeQuery` that selects only the needed column.
- Repository method uses `@Query(nativeQuery = true)` and returns `List<String>`.

**Example:**  
Mapping only the `name` column and returning a list of names.

---

## **2. Constructor Mapping (DTO Projection)**

**Goal:** Fetch only selected fields (e.g., `name` and `age`) into a DTO (`AuthorDto`).

**Why:**  
Native queries **cannot** use JPQL constructor expressions, so `ConstructorResult` is required.

**Key points:**
- Add `@SqlResultSetMapping` with `@ConstructorResult`.
- Map each column to constructor parameters.
- Define a `@NamedNativeQuery` selecting only the required fields.
- Repository returns `List<AuthorDto>`.

**DTO example:**  
A simple immutable class with `name` and `age` fields and a matching constructor.

---

## **3. Entity Mapping (EntityResult)**

**Goal:** Fetch full entities (or multiple entities) using native queries.

**Key points:**
- Use `EntityResult` inside `@SqlResultSetMapping`.
- Useful when native SQL joins or complex queries are needed.
- GitHub examples are referenced for full implementations.

---

## **4. Additional Notes**

- The examples follow the convention `{EntityName}.{RepositoryMethodName}` for naming queries, but you can also use `@Query(name="...")`.
- XML-based mappings (`orm.xml`) are also possible.
- If you want to bypass `@NamedNativeQuery`, you can use `EntityManager` directly with `SqlResultSetMapping`.

---

## **Core Takeaway**
**@SqlResultSetMapping + @NamedNativeQuery** gives you full control over how native SQL results are mapped:
- **ColumnResult** → scalar values  
- **ConstructorResult** → DTOs  
- **EntityResult** → entities  

This is essential when working with native SQL in Spring Boot + JPA, especially for performance‑optimized projections.

---