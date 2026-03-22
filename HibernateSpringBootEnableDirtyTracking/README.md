---

# 📝 Summary of *Item 18: Why and How to Activate Dirty Tracking*

Hibernate 5 introduced **Dirty Tracking** as a more efficient alternative to the older **Dirty Checking** mechanism. Instead of using Java Reflection to inspect every property of every managed entity—which becomes slow when many entities are involved—Dirty Tracking allows each entity to **track its own attribute changes**.

## 🚀 Why Dirty Tracking Matters
- **Performance boost**: Especially noticeable when many entities are managed in the Persistence Context.
- **Less overhead at flush time**: Hibernate asks entities to report their changed attributes instead of computing state differences.

## ⚙️ How It Works
Dirty Tracking requires **Hibernate Bytecode Enhancement**, a build-time process that instruments entity classes by injecting additional code.

To enable it, you must:
1. Add the Hibernate enhancement plugin (Maven/Gradle/Ant supported).
2. Set the configuration flag:
   ```xml
   <enableDirtyTracking>true</enableDirtyTracking>
   ```

Once enabled, Hibernate injects a `DirtyTracker` field into each entity:
```java
@Transient
private transient DirtyTracker $$_hibernate_tracker;
```
During flush, Hibernate calls `$$_hibernate_hasDirtyAttributes()` to retrieve changed properties.

## 🔧 Bytecode Enhancement Overview
- Usually performed at **build-time** (no runtime penalty).
- Can also run at **runtime** or **deploy-time**.
- Supports three mechanisms:
  - **Dirty Tracking** (this item)
  - **Attribute lazy initialization**
  - **Association management** (automatic synchronization of bidirectional relationships)

## 🔍 How to Verify It’s Working
- Decompile an entity class and look for the injected tracker field.
- Or check logs for messages like:
  ```
  Successfully enhanced class file [...]
  ```

## 📌 Additional Notes
- Even with Dirty Tracking, keeping a **thin Persistence Context** is recommended.
- Hibernate still stores entity snapshots internally.

---