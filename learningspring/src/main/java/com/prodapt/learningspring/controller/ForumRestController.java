package com.prodapt.learningspring.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.prodapt.learningspring.controller.exception.ResourceNotFoundException;
import com.prodapt.learningspring.dto.CommentDTO;
import com.prodapt.learningspring.dto.PostDTO;
import com.prodapt.learningspring.entity.Comment;
import com.prodapt.learningspring.entity.Post;
import com.prodapt.learningspring.entity.User;
import com.prodapt.learningspring.repository.CommentRepository;
import com.prodapt.learningspring.repository.PostRepository;
import com.prodapt.learningspring.repository.UserRepository;
import com.prodapt.learningspring.service.CommentService;
import com.prodapt.learningspring.service.PostService;
import com.prodapt.learningspring.service.UserService;

@RestController
@RequestMapping("api/forum")
@CrossOrigin
public class ForumRestController {

    @Autowired
    private PostService postService;

    @Autowired
    private CommentService commentService;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody User user) {
        try {
            if (userService.existsByName(user.getName())) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Username already exists");
            }
            user.setPassword(user.getPassword());
            userService.create(user);
            return ResponseEntity.ok("User registered successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Registration failed: " + e.getMessage());
        }
    }

    @GetMapping("/post/{id}")
    public PostDTO postDetail(@PathVariable int id, Model model,
            @RequestParam(name = "limit", defaultValue = "5") int limit)
            throws ResourceNotFoundException {
        PostDTO post = postService.findById(id);
        return post;
    }

    @GetMapping("/post/{id}/comments")
    @ResponseBody
    public List<CommentDTO> getComments(@PathVariable int id) {
        System.out.println(id);
        return postService.findById(id).getComments();
    }

    @PostMapping("/reply")
    public void postComment(@RequestBody Map<String, String> reqBody) {
        System.out.println(reqBody);
        String username = reqBody.get("username");
        int postId = Integer.valueOf(reqBody.get("postId"));
        int parentId = Integer.valueOf(reqBody.get("parentId"));
        String content = reqBody.get("content");

        User user = userService.getByName(username).get();
        Comment parent = null;
        if(parentId != 0)
            parent = commentRepository.findById(parentId).get();
        Post post = postRepository.findById(postId).get();

        Comment c = new Comment();
        c.setContent(content);
        c.setParent(parent);
        c.setPost(post);
        c.setUser(user);

        commentRepository.save(c);
        commentService.saveTags(c);
    }

    @GetMapping("/users")
    public List<User> getUsers() {
        return (List<User>)userRepository.findAll();
    }

}
