package com.bookstore.forum.repository;

import com.bookstore.forum.entity.Post;
import com.bookstore.forum.entity.PostDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {

    List<Post> findByTitle(String title);

    @Query("select p from Post p join fetch p.comments where p.id = :id")
    Post findByIdWithComments(Long id);

    @Query("select pd from PostDetails pd where pd.post.id = :id")
    PostDetails findDetails(Long id);
}
