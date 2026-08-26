---

# 📘 Summary of Item 28: *Efficiently Fetching Spring Projections with *-to-One Associations*

This item analyzes **four different ways** to fetch DTO-style data in Spring Data JPA when dealing with a `@ManyToOne` (or generally *-to-one) 
association—specifically `Book` → `Author`.  The goal is to fetch **book title + book rank + author name + author genre** efficiently.

---

## 🎯 The Four Approaches Compared

### **1. Nested Closed Projection (interface-based nested DTO)**  
**Example:**  
```java
public interface BookDto {
  String getTitle();
  Integer getRank();
  AuthorDto getAuthor();
  interface AuthorDto {
    String getName();
    String getGenre();
  }
}
```

**Pros**
- Very easy to implement  
- Maintains object structure (Book → Author)

**Cons**
- SQL fetches *all* author columns (id, age, etc.)  
- Persistence Context loads read-only Author entities  
- More memory usage + GC overhead  
- Slower than raw projections

**SQL:**  
Fetches unnecessary columns → inefficient.

---

### **2. Simple Closed Projection (flat DTO)**  
**Example:**  
```java
public interface SimpleBookDto {
  String getTitle();
  Integer getRank();
  String getName();
  String getGenre();
}
```

**Pros**
- SQL fetches *only* required columns  
- Persistence Context is **empty** (no entities loaded)  
- Fastest among structured projections

**Cons**
- Flat structure (no nested Author object)  
- You may need to reshape data manually

**SQL:**  
Exactly the requested columns → very efficient.

---

### **3. Raw Data (List<Object[]>)**  
**Example:**  
```java
List<Object[]> findByViaQueryArrayOfObjects();
```

**Pros**
- Fastest possible  
- Zero overhead  
- No Persistence Context usage

**Cons**
- No type safety  
- No structure  
- Requires manual mapping

This is the **performance winner**, but least convenient.

---

### **4. Simple Open Projection (virtual properties via SpEL)**  
**Example:**  
```java
public interface VirtualBookDto {
  String getTitle();
  Integer getRank();
  @Value("#{ @authorMapper.buildAuthorDto(target.name, target.genre) }")
  AuthorClassDto getAuthor();
}
```

**Pros**
- Maintains nested structure  
- Still fetches only needed columns  
- No Persistence Context usage

**Cons**
- Requires extra mapper class  
- SpEL + reflection overhead may be slow

---