# APPENDIX B

## Associations Efficiency

The first chapter of this book covered the best practices for writing efficient associations. As a quick guide, try to follow these rules:

### One-to-One
* Unidirectional/bidirectional `@OneToOne` with `@MapsId` and bidirectional `@OneToOne` with `@MapsId` on child and `optional=false` on parent is efficient
* Bidirectional `@OneToOne` without `@MapsId` and Bytecode Enhancement and bidirectional `@OneToOne` with Bytecode Enhancement and `optional=true` is less efficient

### One-to-Many
* Bidirectional `@OneToMany` and unidirectional `@ManyToOne` are efficient
* Bidirectional `@OneToMany` with `@JoinColumn(name = "foo_id", insertable = false, updatable = false)`, unidirectional `@OneToMany` with `Set`, `@JoinColumn` and/or `@OrderColumn` are less efficient
* Unidirectional `@OneToMany` with `List` is quite inefficient

### Many-to-Many
* Unidirectional/bidirectional `@ManyToMany` with `Set` and the alternative (relying on two bidirectional `@OneToMany` associations) are efficient
* Unidirectional/bidirectional `@ManyToMany` with `@OrderColumn` is less efficient
* Unidirectional/bidirectional `@ManyToMany` with `List` is quite inefficient
