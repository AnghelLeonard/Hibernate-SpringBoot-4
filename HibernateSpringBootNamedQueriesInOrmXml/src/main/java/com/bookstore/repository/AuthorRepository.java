package com.bookstore.repository;

import java.util.List;
import com.bookstore.entity.Author;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.NativeQuery;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional(readOnly = true)
public interface AuthorRepository extends PagingAndSortingRepository<Author, Long> {

    List<Author> fetchAll();

    Author fetchByNameAndAge(String name, int age);

    // causes exception
    // List<Author> fetchViaSort(Sort sort);
    
    // causes exception
    // List<Author> fetchViaSortWhere(int age, Sort sort);
    
    Page<Author> fetchPageSort(Pageable pageable);
    
    Page<Author> fetchPageSortWhere(int age, Pageable pageable);
    
    Slice<Author> fetchSliceSort(Pageable pageable);
    
    Slice<Author> fetchSliceSortWhere(int age, Pageable pageable);
    
    @NativeQuery
    List<Author> fetchAllNative();
    
    @NativeQuery
    Author fetchByNameAndAgeNative(String name, int age);
    
    // causes exception
    // @NativeQuery
    // List<Author> fetchViaSortNative(Sort sort);

    // causes exception
    // @NativeQuery
    // List<Author> fetchViaSortWhereNative(int age, Sort sort);
    
    @NativeQuery
    Page<Author> fetchPageSortNative(Pageable pageable);
    
    @NativeQuery
    Page<Author> fetchPageSortWhereNative(int age, Pageable pageable);
    
    @NativeQuery
    Slice<Author> fetchSliceSortNative(Pageable pageable);
    
    @NativeQuery
    Slice<Author> fetchSliceSortWhereNative(int age, Pageable pageable);
}
