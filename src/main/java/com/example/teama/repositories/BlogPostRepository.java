package com.example.teama.repositories;

import com.example.teama.models.BlogPost;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BlogPostRepository extends JpaRepository<BlogPost, Integer> {
    @Override
    @Cacheable(cacheNames = "blogPosts", key = "#id")
    Optional<BlogPost> findById(Integer id);

    @Override
    @CachePut(cacheNames = "blogPosts", key = "#result.id")
    <S extends BlogPost> S save(S entity);

    @Override
    @CacheEvict(cacheNames = "blogPosts", key = "#id")
    void deleteById(Integer id);

    @Override
    @Cacheable(cacheNames = "blogPosts")
    List<BlogPost> findAll();

    @Override
    @CacheEvict(cacheNames = "blogPosts", allEntries = true)
    void deleteAll();
}
