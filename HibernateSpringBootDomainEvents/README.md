---

# 📘 Summary of *Item 20: The Best Way to Publish Domain Events from Aggregate Roots*

## 🎯 **Core Idea**
The item explains how **Spring Data** supports publishing **domain events** directly from **aggregate roots** in a Domain-Driven Design (DDD) application. It shows best practices, pitfalls, and performance considerations when handling these events—especially when they involve long-running tasks or database writes.

---

# 🧩 Key Concepts

## 🏷️ **1. Domain Events in Spring Data**
- Aggregate roots can publish domain events using:
  - `@DomainEvents` — exposes events to be published.
  - `@AfterDomainEventPublication` — clears events after publishing.
- Spring provides `AbstractAggregateRoot` to simplify event registration via `registerEvent()`.

---

## 📚 **2. Example Scenario**
A **BookReview** entity:
- Is saved with status `CHECK`.
- Registers a `CheckReviewEvent`.
- After saving, Spring publishes the event.
- An event handler checks grammar/content and updates the review status to `ACCEPT` or `REJECT`.

---

# ⚙️ Transaction Behavior & Pitfalls

## ⏳ **Synchronous Execution Problems**
When the event handler:
- Runs **after commit** (`AFTER_COMMIT`),  
- And performs **database writes**,  
→ The write **does not occur**, because the original transaction is already closed.

### Why?
- The persistence context is still open, but **no new commit** can occur.
- Long-running tasks (e.g., simulated here via `Thread.sleep(40000)`) keep connections busy.

---

# 🔧 Fixing Transaction Issues

## ✔️ **Solution: Use `Propagation.REQUIRES_NEW`**
Annotating the event handler with:

```java
@TransactionalEventListener
@Transactional(propagation = Propagation.REQUIRES_NEW)
```

forces Spring to:
- Suspend the original transaction,
- Start a **new** one for the event handler,
- Allow database updates to commit properly.

### Drawback:
- Two database connections remain open → **performance penalty**.

---

# 🚀 Asynchronous Execution (Recommended)

## ✔️ Best Approach
Use:
- `@EnableAsync`
- `@Async`
- `@Transactional(propagation = REQUIRES_NEW)`

### Benefits:
- The main request returns immediately.
- The event handler runs in a separate thread.
- Only one DB connection is held at a time.
- Long-running tasks no longer block the user.

---

# 📉 Performance Considerations

## Avoid long-running transactions because:
- They hold DB connections.
- They reduce scalability.
- They conflict with MVCC principles.

---

# 🧠 Guidelines for Choosing Event Handling Strategy

## **Asynchronous Execution**
Use when tasks are slow or independent:
- If no DB operations → no `@Transactional`.
- If DB reads only → `@Transactional(readOnly = true, REQUIRES_NEW)`.
- If DB writes → `@Transactional(REQUIRES_NEW)`.
- Avoid async in `BEFORE_COMMIT`.

## **Synchronous Execution**
Use only when necessary:
- `BEFORE_COMMIT` → OK for quick tasks that must run before commit.
- `AFTER_COMPLETION` → OK for quick tasks without DB writes.

---

# ⚠️ Caveats of Spring Domain Events
- Work only with **Spring Data repositories**.
- Events fire **only when `save()` is called**.
- If event publication fails, handlers are **not notified**.
- Consider alternatives:
  - JPA callbacks
  - Observer pattern
  - Hibernate `@Formula`
  - Custom event infrastructure

---