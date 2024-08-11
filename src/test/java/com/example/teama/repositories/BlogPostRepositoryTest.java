package com.example.teama.repositories;

import com.example.teama.models.BlogPost;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Import;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.assertj.core.api.Assertions.assertThat;


@ExtendWith(SpringExtension.class)
@DataJpaTest
@Sql(scripts = {"classpath:/sql/seed.sql"})
@ContextConfiguration(classes = {TestCacheConfiguration.class})
@Import(com.example.teama.TestcontainersConfiguration.class)
@Testcontainers
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
class BlogPostRepositoryTest {

    @Autowired
    private BlogPostRepository blogPostRepository;
    @Autowired
    private AuthorRepository authorRepository;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Autowired
    private CacheManager cacheManager;

    @Autowired
    private TestEntityManager testEntityManager;

    @BeforeEach
    void setUp() {
        clearCache();
    }

    private void clearCache() {
        redisTemplate.keys("blogPosts*").forEach(key -> redisTemplate.delete(key));
        cacheManager.getCache("blogPosts").clear();
        testEntityManager.flush();
        testEntityManager.clear();
    }

    @AfterEach
    void tearDown() {
        clearCache();
    }

    @Test
    @DisplayName("it stores retrieved BlogPosts in the cache")
    public void itStoresRetrievedItems() {
        blogPostRepository.findById(2);
        assertThat(redisTemplate.keys("blogPosts*")).contains("blogPosts::2");
    }

    @Test
    @DisplayName("it uses the cache to retrieve BlogPost")
    public void itUsesTheCacheForRetrievingBlogPosts() {
        BlogPost retrievedBlogPost = blogPostRepository.findById(2).get();
        var currentContent = retrievedBlogPost.getContent();

        var blogPost = testEntityManager.find(BlogPost.class, 2);
        blogPost.setContent("new content");
        testEntityManager.persistAndFlush(blogPost);

        retrievedBlogPost = blogPostRepository.findById(2).get();
        assertThat(retrievedBlogPost.getContent()).isEqualTo(currentContent);
    }

    @Test
    @DisplayName("it repopulates the cache when a blog post is updated")
    public void itRepopulatesTheCache() {
        BlogPost retrievedBlogPost = blogPostRepository.findById(2).get();

        var blogPost = testEntityManager.find(BlogPost.class, 2);
        blogPost.setContent("new content");
        testEntityManager.persistAndFlush(blogPost);

        clearCache();

        retrievedBlogPost = blogPostRepository.findById(2).get();
        assertThat(retrievedBlogPost.getContent()).isEqualTo("new content");
    }

    @Test
    @DisplayName("it removes BlogPost from cache after deletion")
    public void itRemovesBlogPostFromCacheAfterDeletion() {
        blogPostRepository.findById(2);
        blogPostRepository.deleteById(2);
        assertThat(redisTemplate.keys("blogPosts*")).doesNotContain("blogPosts::2");
    }

    @Test
    @DisplayName("it caches all BlogPosts after retrieval")
    public void itCachesAllBlogPostsAfterRetrieval() {
        blogPostRepository.findAll();
        assertThat(redisTemplate.keys("blogPosts*")).isNotEmpty();
    }

    @Test
    @DisplayName("it clears the cache when all BlogPosts are deleted")
    public void itClearsCacheWhenAllBlogPostsAreDeleted() {
        blogPostRepository.findAll();
        blogPostRepository.deleteAll();
        assertThat(redisTemplate.keys("blogPosts*")).isEmpty();
    }

    @Test
    @DisplayName("it updates the cache when a BlogPost is partially updated")
    public void itUpdatesCacheWhenPartiallyUpdated() {
        BlogPost retrievedBlogPost = blogPostRepository.findById(2).get();
        retrievedBlogPost.setTitle("Updated Title");
        blogPostRepository.save(retrievedBlogPost);

        clearCache();

        retrievedBlogPost = blogPostRepository.findById(2).get();
        assertThat(retrievedBlogPost.getTitle()).isEqualTo("Updated Title");
    }

}