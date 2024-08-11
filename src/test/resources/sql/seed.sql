SET FOREIGN_KEY_CHECKS = 0;
TRUNCATE TABLE comments;
TRUNCATE TABLE blog_posts;
TRUNCATE TABLE authors;
SET FOREIGN_KEY_CHECKS = 1;

INSERT INTO authors (id, name, email)
VALUES (1, 'John Doe', 'john.doe@example.com'),
       (2, 'Jane Smith', 'jane.smith@example.com');

INSERT INTO blog_posts (id, title, content, author_id)
VALUES (1, 'First Blog Post', 'This is the content of the first blog post.', 1),
       (2, 'Second Blog Post', 'This is the content of the second blog post.', 2);

INSERT INTO comments (id, blog_post_id, author_id, content)
VALUES (1, 1, 2, 'This is a comment by Jane Smith on the first blog post.'),
       (2, 2, 1, 'This is a comment by John Doe on the second blog post.');