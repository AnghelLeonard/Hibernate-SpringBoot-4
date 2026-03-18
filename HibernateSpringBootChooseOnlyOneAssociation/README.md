---

# 📘 Summary of *Item 12: How to Validate that Only One Association Is Non‑Null*

### 🎯 **Purpose**
The document explains how to enforce a rule in a JPA/Hibernate application so that a `Review` entity can be linked 
to **exactly one** of three possible entities, `Book`, `Article`, or `Magazine`.

---

# 🧩 Key Concepts

## 1. **The Problem**
A `Review` entity has three `@ManyToOne` associations:
- `Book`
- `Article`
- `Magazine`

Only **one** of these should be set for any given review.  
The application must prevent cases where:
- None are set, or  
- More than one is set.

---

## 2. **Application‑Level Validation (Bean Validation)**

### ✔️ **Custom Annotation**
A class‑level annotation `@JustOneOfMany` is created to enforce the rule.

```java
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {JustOneOfManyValidator.class})
public @interface JustOneOfMany {
    String message() default "A review can be associated with either a book, a magazine or an article";
}
```

### ✔️ **Validator Logic**
The validator checks how many associations are non‑null:

```java
return Stream.of(
    review.getBook(), review.getArticle(), review.getMagazine()
)
.filter(Objects::nonNull)
.count() == 1;
```

If the count is not exactly 1, validation fails.

### ✔️ **Usage**
Add the annotation to the `Review` entity:

```java
@Entity
@JustOneOfMany
public class Review { ... }
```

---

## 3. **Examples**

### ✅ Valid Case
A review linked only to a `Book` passes validation.

### ❌ Invalid Case
A review linked to both an `Article` and a `Magazine` fails validation.

---

## 4. **Database‑Level Validation (Optional but Recommended)**

Because **native SQL queries bypass Bean Validation**, the document recommends adding a **(MySQL) trigger** to enforce the rule at the database level.

### ✔️ Trigger Logic
The trigger checks combinations of non‑null foreign keys and raises an error if more than one is set.

---

# 📝 **In Short**
The item teaches you how to ensure that a `Review` entity is associated with **exactly one** of three possible parent entities. It covers:
- Creating a custom Bean Validation annotation  
- Implementing a validator  
- Applying it to the entity  
- Testing valid and invalid cases  
- Adding a (MySQL) trigger for extra safety  

---