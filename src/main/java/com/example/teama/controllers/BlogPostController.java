package com.example.teama.controllers;

import com.example.teama.models.BlogPost;
import com.example.teama.repositories.BlogPostRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Controller
@RequestMapping("/blog-posts")
@RequiredArgsConstructor
@Slf4j
public class BlogPostController {
    private final BlogPostRepository blogPostRepository;
    private final RedisTemplate<String, Object> redisTemplate;


    @GetMapping
    public String index(Model model) {
        logCacheContents();

        List<BlogPost> blogPosts = blogPostRepository.findAll();
        model.addAttribute("blogPosts", blogPosts);
        return "views/blog_posts/index";
    }

    @GetMapping("/new")
    public String _new(Model model) {
        model.addAttribute("blogPost", new BlogPost());
        return "views/blog_posts/new";
    }

    @PostMapping
    public String create(@ModelAttribute BlogPost blogPost) {
        blogPostRepository.save(blogPost);
        return "redirect:/blog-posts";
    }

    @GetMapping("/{id}")
    public String show(@PathVariable("id") Integer id, Model model) {
        Optional<BlogPost> blogPost = blogPostRepository.findById(id);
        if (blogPost.isPresent()) {
            model.addAttribute("blogPost", blogPost.get());
            return "views/blog_posts/show";
        } else {
            return "redirect:/blog-posts";
        }
    }

    @GetMapping("/{id}/edit")
    public String edit(@PathVariable("id") Integer id, Model model) {
        Optional<BlogPost> blogPost = blogPostRepository.findById(id);
        if (blogPost.isPresent()) {
            model.addAttribute("blogPost", blogPost.get());
            return "views/blog_posts/edit";
        } else {
            return "redirect:/blog-posts";
        }
    }

    @PostMapping("/{id}")
    public String update(@PathVariable("id") Integer id, @ModelAttribute BlogPost blogPost) {
        blogPost.setId(id);
        blogPostRepository.save(blogPost);
        return "redirect:/blog-posts";
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable("id") Integer id) {
        blogPostRepository.deleteById(id);
        return "redirect:/blog-posts";
    }

    private void logCacheContents() {
        Set<String> keys = redisTemplate.keys("blogPosts*");
        if (keys != null) {
            log.info("Cache contains the following keys: {}", keys);
        }
    }
}
