---

# 📘 Summary of Item 28: Efficiently Fetching Spring Projections with *-to-One Associations

This item analyzes **four different ways** to fetch DTO-style data in Spring Data JPA when dealing with a `@ManyToOne` (or generally *-to-one) association—specifically `Book` → `Author`.  
The goal: fetch **book title + author name + author genre** efficiently.

---

## 🎯 The Four Approaches Compared

### **1. Nested Closed Projection (interface-based nested DTO)**  
**Example:**  
```java
public interface BookDto {
  String getTitle();
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
- **Slowest** approach due to SpEL + reflection overhead

---

## 📊 Performance Ranking (Fast → Slow)

1. **Raw data (`Object[]`)**  
2. **Simple closed projection**  
3. **Nested closed projection**  
4. **Open projection (SpEL)** ← slowest

Benchmarking was done using JMH on MySQL, Windows 11, i7 CPU.

---

## 🧠 Key Takeaways

- If you want **maximum performance**, use **raw data** or **simple closed projections**.  
- If you need **nested structure**, prefer **nested closed projections** over open projections.  
- Avoid open projections unless you absolutely need virtual properties—they are the slowest.  
- Nested closed projections are convenient but fetch unnecessary columns and load entities into the Persistence Context.

---