package com.bookstore.service;

import com.bookstore.entity.Book;
import com.bookstore.entity.Ebook;
import com.bookstore.entity.Paperback;
import com.bookstore.repository.BookRepository;
import com.bookstore.visitor.Visitor;
import jakarta.annotation.PostConstruct;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.aop.support.AopUtils;
import org.springframework.stereotype.Service;

@Service
public class VisitorService implements Visitable {

    private final BookRepository bookRepository;
    private final List<Visitor> visitors;
    private final Map<Class<? extends Visitor>, Visitor> visitorsMap = new HashMap<>();

    public VisitorService(BookRepository bookRepository, List<Visitor> visitors) {
        this.bookRepository = bookRepository;
        this.visitors = visitors;
    }

    @PostConstruct
    @SuppressWarnings("unchecked")
    public void init() {
        visitors.forEach(visitor -> {
            Class<? extends Visitor> targetClass
                    = (Class<? extends Visitor>) AopUtils.getTargetClass(visitor);
            visitorsMap.put(targetClass, visitor);
        });
    }

    @Override
    public void accept(Class<? extends Visitor> clazzVisitor) {
        
        Visitor currentVisitor = visitorsMap.get(clazzVisitor);
        
        if (currentVisitor == null) {
            throw new IllegalArgumentException("No visitor registered for: " + clazzVisitor.getName());
        }

        List<Book> allBooks = bookRepository.findAll();
        for (Book book : allBooks) {
            Book unproxied = (Book) org.hibernate.Hibernate.unproxy(book);
            switch (unproxied) {
                case Ebook ebook ->
                    currentVisitor.visit(ebook);
                case Paperback paperback ->
                    currentVisitor.visit(paperback);
                default ->
                    throw new IllegalArgumentException("Unknown book type: " + unproxied.getClass());
            }
        }
    }
}
