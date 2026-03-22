---

# 📘 Summary of *Item 19: How to Map a Boolean to a Yes/No*

## 🎯 Core Problem  
A legacy database contains an `author` table where the `best_selling` column is stored as a `VARCHAR(3)` with values `"Yes"` or `"No"`.  
Because the schema cannot be changed, Hibernate cannot directly map this column to a Java `Boolean` using its built‑in types.

Hibernate’s default Boolean mappings expect
- `BIT`
- `INTEGER` (0/1)
- `CHAR` (Y/N or T/F)

None of these match `VARCHAR(3)`.

---

## 💡 Solution: Create a Custom Attribute Converter  
To bridge the mismatch, the document proposes implementing a custom `AttributeConverter<Boolean, String>`.

### ✔️ Converter Behavior
- **convertToDatabaseColumn(Boolean attr)**  
  - Stores `"Yes"` when the Boolean is `true`  
  - Stores `"No"` when the Boolean is `false` or `null`

- **convertToEntityAttribute(String dbData)**  
  - Returns `false` only when the value is `"No"`  
  - Returns `true` for any other value (including `"Yes"`)

### ✔️ Auto-Apply Option  
Annotating the converter with:

```java
@Converter(autoApply = true)
```

makes Hibernate apply it automatically to all `Boolean` attributes.

If you want to apply it only to specific fields, set `autoApply = false` and annotate the field:

```java
@Convert(converter = BooleanConverter.class)
private Boolean bestSelling;
```

### ⚠️ Limitation  
`AttributeConverter` cannot be used on fields annotated with `@Enumerated`.

---

## 🧩 Why This Matters  
This approach allows you to:
- Keep the legacy database unchanged  
- Maintain clean Java domain models  
- Ensure consistent Boolean handling across all CRUD operations  

---