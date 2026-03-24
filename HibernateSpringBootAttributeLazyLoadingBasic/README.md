---

# 📘 Summary of Item 23: *Lazy Loading Entity Attributes with Hibernate Bytecode Enhancement*

This item explains **how to lazily load large entity attributes**—such as BLOBs—in Hibernate using **Bytecode Enhancement**, and how to avoid common pitfalls like **N+1 queries** and **LazyInitializationException**.

---

## 🧩 Why Lazy Loading Attributes Matters
- By default, Hibernate loads **all attributes eagerly**, even large ones like images.
- In the example, the `Author` entity has an `avatar` field (a byte array).  
  Loading this on every query wastes resources.
- Lazy loading ensures the avatar is fetched **only when needed**.

---

## ⚙️ How to Enable Attribute Lazy Loading

### **1. Add Hibernate Bytecode Enhancement**
- Configure the `hibernate-maven-plugin` in `pom.xml`.
- Enable:
  ```
  <enableLazyInitialization>true</enableLazyInitialization>
  ```
- Enhancement happens at **build time**, so no runtime overhead.
- Without this step, attribute lazy loading **does not work**.

### **2. Mark Attributes as Lazy**
Annotate the large field:
```java
@Lob
@Basic(fetch = FetchType.LAZY)
private byte[] avatar;
```

---

## 🔍 How Lazy Loading Behaves in Practice

### Fetching authors by age:
```sql
SELECT id, age, genre, name FROM author WHERE age >= ?
```
The avatar is **not** loaded.

### Accessing the avatar later:
Calling `getAvatar()` triggers a **secondary SELECT**:
```sql
SELECT avatar FROM author WHERE id = ?
```

### Important:
Accessing lazy attributes **outside a Hibernate session** causes a `LazyInitializationException`.

---

## ⚠️ The N+1 Problem with Lazy Attributes
If you loop through authors and call `getAvatar()` for each, Hibernate fires:
- 1 query for the list  
- N queries for each avatar  

This leads to performance degradation.

### How to avoid it:
- Use DTO projections:
  ```java
  @Query("SELECT a.name, a.avatar FROM Author a WHERE a.age >= ?1")
  List<AuthorDto> findDtoByAgeGreaterThanEqual(int age);
  ```
- Or use subentities (referenced in Item 24).

---

## 🧨 LazyInitializationException in Spring Boot

### Why it happens:
- Spring Boot enables **Open Session in View (OSIV)** by default.
- OSIV keeps the session open during view rendering, so lazy attributes load fine.
- If OSIV is disabled (`spring.jpa.open-in-view=false`), and Jackson tries to serialize lazy attributes, it triggers a lazy load **without a session**, causing an exception.

---

## 🛠️ Solutions to Avoid LazyInitializationException

### **1. Set Explicit Default Values**
Before returning the entity:
```java
if (author.getAge() < 40) {
    author.getAvatar(); // load it
} else {
    author.setAvatar(null); // default value
}
```
Then configure Jackson to skip default values:
```java
@JsonInclude(Include.NON_DEFAULT)
```

### **2. Use a Custom Jackson Filter**
- Configure Jackson to ignore certain fields (e.g., `avatar`).

---

# 🧾 Key Takeaways
- Lazy loading large attributes improves performance.
- Hibernate Bytecode Enhancement is **required** for attribute-level lazy loading.
- Be careful with N+1 queries when accessing lazy fields in loops.
- Disabling OSIV requires handling lazy attributes manually to avoid exceptions.
- Use DTOs or Jackson configuration to control serialization.

---