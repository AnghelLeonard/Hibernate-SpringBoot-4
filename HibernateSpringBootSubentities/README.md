---

# 📘 Summary of Item 24: *Lazy Loading Entity Attributes via Subentities*

This item explains an alternative strategy for lazily loading large or optional entity attributes in Hibernate **without relying on bytecode enhancement** or dealing with issues like Open Session in View or Jackson serialization.

## 🎯 Goal
Load the lightweight attributes of an `Author` entity eagerly (`id`, `age`, `name`, `genre`) while loading the heavy `avatar` field **only when needed**.

---

# 🧩 Core Idea: Split the Entity into Subentities

![](https://github.com/AnghelLeonard/Hibernate-SpringBoot-4/blob/main/HibernateSpringBootSubentities/attributes%20lazy%20loading%20via%20subentites.png)

Instead of placing all fields in a single entity, the solution uses:

### **1. A `@MappedSuperclass` (BaseAuthor)**
Contains the attributes that should always be loaded eagerly:
- `id`
- `age`
- `name`
- `genre`

This class is **not** an entity and has **no table** of its own.

### **2. Two Subentities Mapped to the Same Table**
Both extend `BaseAuthor` and are explicitly mapped to the same table (`author`):

#### **AuthorShallow**
- Contains only the base attributes.
- Used when you want the lightweight version.
- SQL loads only the basic fields.

#### **AuthorDeep**
- Inherits base attributes.
- Adds the `avatar` field.
- Used when you need the full profile.
- SQL loads the avatar as well.

Mapping both with `@Table(name = "author")` ensures:
- No duplicated columns
- No accidental creation of multiple tables

---

# 🗄️ Repository Usage

Two Spring Data repositories are created:

- `AuthorShallowRepository`
- `AuthorDeepRepository`

Calling `findAll()` on each produces different SQL:

| Repository | Loaded Fields | SQL Behavior |
|-----------|----------------|--------------|
| **Shallow** | id, age, name, genre | Avatar **not** loaded |
| **Deep** | All fields including avatar | Avatar **loaded** |

---

# 🧠 Why This Approach Helps

Hibernate supports lazy loading of basic fields, but:
- It requires **bytecode enhancement**
- It can cause issues with:
  - Open Session in View
  - JSON serialization

Using subentities avoids these problems entirely.

---

# ✅ Conclusion

Splitting the entity into shallow and deep versions provides a clean, reliable way to lazily load heavy attributes like BLOBs. It avoids the complexity of Hibernate bytecode enhancement and keeps SQL queries efficient.