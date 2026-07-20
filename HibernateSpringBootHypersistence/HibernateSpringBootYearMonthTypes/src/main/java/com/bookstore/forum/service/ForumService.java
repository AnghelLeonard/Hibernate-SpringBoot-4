package com.bookstore.forum.service;

import com.bookstore.forum.entity.PostDetails;
import com.bookstore.forum.repository.PostDetailsRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.YearMonth;
import java.util.List;

@Service
public class ForumService {

    private final PostDetailsRepository postDetailsRepository;

    public ForumService(PostDetailsRepository postDetailsRepository) {
        this.postDetailsRepository = postDetailsRepository;
    }

    @Transactional
    public PostDetails save(PostDetails details) {
        return postDetailsRepository.save(details);
    }

    @Transactional(readOnly = true)
    public PostDetails findById(Long id) {
        return postDetailsRepository.findById(id).orElseThrow();
    }

    @Transactional(readOnly = true)
    public List<PostDetails> findByPublishedOn(YearMonth publishedOn) {
        return postDetailsRepository.findByPublishedOn(publishedOn);
    }

    @Transactional
    public void deleteAll() {
        postDetailsRepository.deleteAllInBatch();
    }
}
