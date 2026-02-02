--

## ⭐ Item 2 Summary: Why to Avoid Unidirectional `@OneToMany`

Unidirectional `@OneToMany` (where only the parent knows about the children) seems simple, but it introduces **significant performance and design drawbacks** compared to bidirectional `@OneToMany` or unidirectional `@ManyToOne`.

### 🔧 1. Hibernate Creates a Junction Table
Because the child entity has no `@ManyToOne` back-reference, Hibernate must create a **join table** (e.g., `author_books`) to store the association.

This leads to:
- Extra table
- More joins
- More memory for indexes
- More complex queries

### 🧱 2. Insert Operations Become Inefficient
Persisting a parent with N children triggers:
- N inserts into the child table (expected)
- **N additional inserts** into the join table (unnecessary in a bidirectional mapping)

### 🔄 3. Updating the Collection Is Expensive
When adding or removing a child:
- Hibernate **deletes all rows** for that parent in the join table
- Re-inserts the entire collection

This happens even for a single-element change.

### 🗑 4. Delete Operations Are Also Inefficient
Removing a child triggers:
- Full deletion of all join-table rows for the parent
- Re-insertion of the remaining ones
- Plus the actual delete of the child

### 🏷 5. `@OrderColumn` Helps Only Slightly
Adding `@OrderColumn` reduces some of the full-table rewrites, but:
- Still requires extra inserts/updates
- Still slower than bidirectional `@OneToMany`
- Performance degrades when removing elements near the start of the list

### 🔗 6. `@JoinColumn` Removes the Join Table but Adds Updates
Using `@JoinColumn` eliminates the join table, but:
- Hibernate must issue **UPDATE statements** to set the foreign key on each child
- Still slower than bidirectional `@OneToMany`

### 📌 Final Conclusion
Unidirectional `@OneToMany` is **less efficient for reading, writing, and deleting** than:
- Bidirectional `@OneToMany` (recommended)
- Unidirectional `@ManyToOne` (also efficient)

**Rule of thumb:**  
👉 *Avoid unidirectional `@OneToMany` unless you have a very specific reason.*

---

---