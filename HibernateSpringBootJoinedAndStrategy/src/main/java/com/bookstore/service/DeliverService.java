package com.bookstore.service;

import com.bookstore.entity.Book;
import com.bookstore.repository.BookRepository;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Service;
import com.bookstore.strategy.Delivery;
import jakarta.annotation.PostConstruct;
import org.hibernate.Hibernate;

@Service
public class DeliverService implements Deliverable {

    private final BookRepository bookRepository;
    private final List<Delivery<? extends Book>> deliverStrategies;

    private final Map<Class<? extends Book>, Delivery<? extends Book>> deliverStrategiesMap = new HashMap<>();

    public DeliverService(BookRepository bookRepository, List<Delivery<? extends Book>> deliverStrategies) {
        this.bookRepository = bookRepository;
        this.deliverStrategies = deliverStrategies;
    }

    @PostConstruct
    public void init() {
        deliverStrategies.forEach((deliverStrategy) -> {
            deliverStrategiesMap.put(deliverStrategy.ofBook(), deliverStrategy);
        });
    }

    @Override
    public void process() {

        List<Book> allBooks = bookRepository.findAll();

        for (Book book : allBooks) {
            Delivery deliveryStrategy = deliverStrategiesMap.get(Hibernate.getClass(book));

            if (deliveryStrategy == null) {
                throw new IllegalStateException("No delivery strategy found for " + book.getClass().getSimpleName());
            }

            deliveryStrategy.deliver(book);
        }
    }
}
