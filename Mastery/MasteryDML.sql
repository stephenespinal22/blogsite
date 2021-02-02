USE Blog;

-- INSERT INTO HashTags (hashTagName) VALUES ("hashhhh");
SELECT hashTagId, hashTagName FROM HashTags;
-- UPDATE HashTags SET hashTagName = ? WHERE hashTagId = ?
-- DELETE FROM HashTags WHERE hashTagId = ?

-- INSERT INTO BlogPosts (blogTitle,blogContent,userId) VALUES ('sd','sd',3);
SELECT blogId, blogTitle, blogContent, isPublished, BlogPosts.userId, `username` FROM BlogPosts JOIN `user` as Users ON Users.`id` = BlogPosts.userId;

SELECT blogId, blogPreview, blogTitle, blogContent, isPublished, postDate, featuredImagePath, BlogPosts.userId, `username`, ht.hashTagName FROM BlogPosts"
            + " JOIN `user` as Users ON Users.`id` = BlogPosts.userId JOIN BlogPostsHashTags as bpht ON BlogPosts.blogId = bpht.blogId "
            + "JOIN HashTags as ht ON ht.hashTagId = bpht.hashTagId WHERE ht.hashTagName = 'a' AND WHERE isPublished = true