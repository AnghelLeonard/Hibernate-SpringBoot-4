package com.bookstore.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "author")
public class AuthorShallow extends BaseAuthor {    
}
