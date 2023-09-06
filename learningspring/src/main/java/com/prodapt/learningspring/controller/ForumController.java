package com.prodapt.learningspring.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.prodapt.learningspring.Service.CommentService;
import com.prodapt.learningspring.business.LoggedInUser;
import com.prodapt.learningspring.business.NeedsAuth;
import com.prodapt.learningspring.controller.binding.AddPostForm;
import com.prodapt.learningspring.controller.exception.ResourceNotFoundException;
import com.prodapt.learningspring.entity.LikeRecord;
import com.prodapt.learningspring.entity.Comment;
import com.prodapt.learningspring.entity.LikeId;
import com.prodapt.learningspring.entity.Post;
import com.prodapt.learningspring.entity.User;
import com.prodapt.learningspring.repository.CommentRepository;
import com.prodapt.learningspring.repository.LikeCRUDRepository;
import com.prodapt.learningspring.repository.PostRepository;
import com.prodapt.learningspring.repository.UserRepository;

import jakarta.annotation.PostConstruct;
import jakarta.servlet.ServletException;

@Controller
@RequestMapping("/forum")
public class ForumController {

  @Autowired
  private LoggedInUser loggedInUser;

  @Autowired
  private CommentService commentService;
  
  @Autowired
  private UserRepository userRepository;
  
  @Autowired
  private PostRepository postRepository;

  @Autowired
  private CommentRepository commentRepository;
  
  @Autowired
  private LikeCRUDRepository likeCRUDRepository;
  
  private List<User> userList;
  
  @PostConstruct
  public void init() {
    userList = new ArrayList<>();
  }
  
  @GetMapping("/post/form")
  public String getPostForm(Model model) {
    if(this.loggedInUser.getLoggedInUser() == null){
      return "redirect:/loginpage";
    }
    model.addAttribute("postForm", new AddPostForm());
    // userRepository.findAll().forEach(user -> userList.add(user));
    // model.addAttribute("userList", userList);
    // model.addAttribute("authorid", 0);
    return "forum/postForm";
  }
  
  @PostMapping("/post/add")
  @NeedsAuth(loginPage = "/loginpage")
  public String addNewPost(@ModelAttribute("postForm") AddPostForm postForm, BindingResult bindingResult, RedirectAttributes attr) throws ServletException {
    if (bindingResult.hasErrors()) {
      System.out.println(bindingResult.getFieldErrors());
      attr.addFlashAttribute("org.springframework.validation.BindingResult.post", bindingResult);
      attr.addFlashAttribute("post", postForm);
      return "redirect:/forum/post/form";
    }
    Optional<User> user = userRepository.findById(this.loggedInUser.getLoggedInUser().getId());
    if (user.isEmpty()) {
      throw new ServletException("Something went seriously wrong and we couldn't find the user in the DB");
    }
    Post post = new Post();
    post.setAuthor(user.get());
    post.setContent(postForm.getContent());
    postRepository.save(post);
    
    return String.format("redirect:/forum/post/%d", post.getId());
  }
  
  @GetMapping("/post/{id}")
  @NeedsAuth(loginPage = "/loginpage")
  public String postDetail(@PathVariable int id, Model model) throws ResourceNotFoundException {
    Optional<Post> post = postRepository.findById(id);
    if (post.isEmpty()) {
      throw new ResourceNotFoundException("No post with the requested ID");
    }
    model.addAttribute("post", post.get());
    model.addAttribute("userList", userList);
    int numLikes = likeCRUDRepository.countByLikeIdPost(post.get());
    model.addAttribute("likeCount", numLikes);
    List<Comment> comments = new ArrayList<>();
    commentRepository.findAll().forEach(comments::add);
    comments = commentService.preOrder(comments);
    model.addAttribute("comments", comments);
    return "forum/postDetail";
  }
  
  @PostMapping("/post/{id}/like")
  @NeedsAuth(loginPage = "/loginpage")
  public String postLike(@PathVariable int id, RedirectAttributes attr) {
    LikeId likeId = new LikeId();
    likeId.setUser(userRepository.findById(this.loggedInUser.getLoggedInUser().getId()).get());
    likeId.setPost(postRepository.findById(id).get());
    LikeRecord like = new LikeRecord();
    like.setLikeId(likeId);
    likeCRUDRepository.save(like);
    return String.format("redirect:/forum/post/%d", id);
  }

  @PostMapping("/post/{id}/comment")
  @NeedsAuth(loginPage = "/loginpage")
  public String addComment(@PathVariable int id, @RequestParam String content){
    Comment comment = new Comment();
    comment.setUser(userRepository.findById(this.loggedInUser.getLoggedInUser().getId()).get());
    comment.setPost(postRepository.findById(id).get());
    comment.setContent(content);
    commentRepository.save(comment);
    return String.format("redirect:/forum/post/%d", id);
  }

  @PostMapping("/post/{id}/reply/{parentId}")
  @NeedsAuth(loginPage = "/loginpage")
  public String replyComment(@PathVariable int id, @PathVariable int parentId, @RequestParam String content){
    Comment comment = new Comment();
    comment.setUser(userRepository.findById(this.loggedInUser.getLoggedInUser().getId()).get());
    comment.setPost(postRepository.findById(id).get());
    comment.setParent(commentRepository.findById(parentId).get());
    comment.setLevel(commentRepository.findById(parentId).get().getLevel() + 1);
    comment.setContent(content);
    commentRepository.save(comment);
    return String.format("redirect:/forum/post/%d", id);
  }
  
}
