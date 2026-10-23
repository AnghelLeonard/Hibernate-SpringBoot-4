package com.bookstore.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import org.hibernate.Hibernate;

@Entity
@Table(name = "orders")
public class Order implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String customerName;

    // OneToMany relationship to the static inner class (is like a regular OneToMany)
    @OneToMany(cascade = CascadeType.ALL,
            mappedBy = "order", orphanRemoval = true)
    private List<OrderLine> lines = new ArrayList<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public List<OrderLine> getLines() {
        return Collections.unmodifiableList(this.lines);
    }

    public void addBook(String isbn, int quantity, double price) {
        OrderLine newLine = new OrderLine(this, isbn, quantity, price);
        this.lines.add(newLine);
    }

    public void removeBook(String isbn) {

        Iterator<OrderLine> iterator = this.lines.iterator();

        while (iterator.hasNext()) {
            OrderLine line = iterator.next();
            if (line.getIsbn().equals(isbn)) {
                line.setOrder(null);
                iterator.remove();
                break;
            }
        }
    }

    public double getTotalAmount() {
        return lines.stream().mapToDouble(line -> line.price * line.quantity).sum();
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();

        sb.append("Order{")
                .append("id=").append(id)
                .append(", customerName='").append(customerName).append('\'');

        if (Hibernate.isInitialized(lines)) {
            sb.append(", lineCount=").append(lines.size());
            sb.append(", totalAmount=").append(getTotalAmount());
        } else {
            sb.append(", lines=[Not Initialized]");
        }
        sb.append('}');

        return sb.toString();
    }

    // static inner entity
    @Entity
    @Table(name = "order_lines")
    // use 'protected' visibility to hide the class from the rest of the application
    protected static class OrderLine {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        private String isbn;
        private int quantity;
        private double price;

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "order_id", referencedColumnName = "id")
        private Order order;

        protected OrderLine() {
        }

        // constructor is only accessible inside the Order class
        private OrderLine(Order order, String isbn, int quantity, double price) {
            this.order = order;
            this.isbn = isbn;
            this.quantity = quantity;
            this.price = price;
        }

        String getIsbn() {
            return isbn;
        }

        void setOrder(Order order) {
            this.order = order;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }

            OrderLine oLine = (OrderLine) o;

            return isbn != null ? isbn.equals(oLine.isbn) : oLine.isbn == null;
        }

        @Override
        public int hashCode() {
            return isbn != null ? isbn.hashCode() : 0;
        }

        @Override
        public String toString() {
            return "OrderLine{" + "id=" + id + ", isbn=" + isbn + ", quantity="
                    + quantity + ", price=" + price + '}';
        }
    }
}
