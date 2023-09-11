package com.prodapt.learningspring.controller;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
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

import com.prodapt.learningspring.controller.binding.AddPostForm;
import com.prodapt.learningspring.controller.exception.ResourceNotFoundException;
import com.prodapt.learningspring.dto.PostDTO;
import com.prodapt.learningspring.entity.LikeRecord;
import com.prodapt.learningspring.entity.Notification;
import com.prodapt.learningspring.entity.Comment;
import com.prodapt.learningspring.entity.LikeId;
import com.prodapt.learningspring.entity.Post;
import com.prodapt.learningspring.entity.User;
import com.prodapt.learningspring.model.RegistrationForm;
import com.prodapt.learningspring.repository.CommentRepository;
import com.prodapt.learningspring.repository.LikeCRUDRepository;
import com.prodapt.learningspring.repository.PostRepository;
import com.prodapt.learningspring.repository.UserRepository;
import com.prodapt.learningspring.service.CommentService;
import com.prodapt.learningspring.service.DomainUserService;
import com.prodapt.learningspring.service.NotificationService;
import com.prodapt.learningspring.service.PostService;

import jakarta.servlet.ServletException;

@Controller
@RequestMapping("/forum")
public class ForumController {
  
  @Autowired
  private UserRepository userRepository;
  
  @Autowired
  private DomainUserService domainUserService;
  
  @Autowired
  private LikeCRUDRepository likeCRUDRepository;

  @Autowired
  private PostService postService;

  @Autowired
  private PostRepository postRepository;
  
  @Autowired
  private CommentService commentService;

  @Autowired
  private NotificationService notificationService;

  @Autowired
  private CommentRepository commentRepository;
  
  @GetMapping("/post/form")
  public String getPostForm(Model model, @AuthenticationPrincipal UserDetails userDetails) {
    AddPostForm postForm = new AddPostForm();
    User author = domainUserService.getByName(userDetails.getUsername()).get();
    postForm.setUserId(author.getId());
    model.addAttribute("postForm", postForm);
    return "forum/postForm";
  }

  @GetMapping("/post/all")
	public String getallPosts(Model model, Principal principal, @RequestParam(name="limit", defaultValue = "5") int limit) {
    List<PostDTO> posts = postService.findAllLimitBy(limit);
    model.addAttribute("isLoggedIn", principal != null);
    if(principal != null){
      model.addAttribute("username", principal.getName());
    }
		model.addAttribute("posts", posts);
		return "forum/allPosts";
	}
  
  @PostMapping("/post/add")
  public String addNewPost(@ModelAttribute("postForm") AddPostForm postForm, BindingResult bindingResult, RedirectAttributes attr) throws ServletException {
    if (bindingResult.hasErrors()) {
      System.out.println(bindingResult.getFieldErrors());
      attr.addFlashAttribute("org.springframework.validation.BindingResult.post", bindingResult);
      attr.addFlashAttribute("post", postForm);
      return "redirect:/forum/post/form";
    }
    Optional<User> user = userRepository.findById(postForm.getUserId());
    if (user.isEmpty()) {
      throw new ServletException("Something went seriously wrong and we couldn't find the user in the DB");
    }
    Post post = new Post();
    post.setAuthor(user.get());
    post.setTitle(postForm.getTitle());
    post.setContent(postForm.getContent());
    postRepository.save(post);

    notificationService.createNotification(user.get(), post, "POST", "You added a post (" + postForm.getTitle() + ").");
    
    return String.format("redirect:/forum/post/%d", post.getId());
  }
  
  @GetMapping("/post/{id}")
  public String postDetail(@PathVariable int id, Model model, Principal principal, 
                           @RequestParam(name = "limit", defaultValue = "5") int limit) 
                           throws ResourceNotFoundException {
    
    model.addAttribute("isLoggedIn", principal != null);
    if(principal != null){
      model.addAttribute("username", principal.getName());
    }
    PostDTO post = postService.findById(id);
    model.addAttribute("post", post);
    model.addAttribute("comments", commentService.findAllByPostIdLimitBy(id, limit));
    return "forum/postDetail";
  }
  
  @PostMapping("/post/{id}/like")
  public String postLike(@PathVariable int id, RedirectAttributes attr, @AuthenticationPrincipal UserDetails userDetails) {
    LikeId likeId = new LikeId();
    User user = domainUserService.getByName(userDetails.getUsername()).get();
    Post post = postRepository.findById(id).get();
    likeId.setUser(userRepository.findByName(user.getName()).get());
    likeId.setPost(post);
    LikeRecord like = new LikeRecord();
    like.setLikeId(likeId);
    likeCRUDRepository.save(like);
    if(userRepository.findByName(userDetails.getUsername()).get().equals(post.getAuthor())){
      notificationService.createNotification(postRepository.findById(id).get().getAuthor(), postRepository.findById(id).get(), "LIKE", "you, liked your post (" + postRepository.findById(id).get().getTitle() + ").");
    }
    else{
      notificationService.createNotification(postRepository.findById(id).get().getAuthor(), postRepository.findById(id).get(), "LIKE", userDetails.getUsername() + " liked your post (" + postRepository.findById(id).get().getTitle() + ").");
    }
    return String.format("redirect:/forum/post/%d", id);
  }


  @PostMapping("/post/{id}/comment")
  public String addComment(@PathVariable int id, @RequestParam String content, 
                           @AuthenticationPrincipal UserDetails userDetails){
    Post post = postRepository.findById(id).get();
    int postId = post.getId();
    Comment comment = new Comment();
    comment.setUser(domainUserService.getByName(userDetails.getUsername()).get());
    comment.setPost(post);
    comment.setContent(content);
    commentRepository.save(comment);
    if(userRepository.findByName(userDetails.getUsername()).get().equals(post.getAuthor())){
      notificationService.createNotification(postRepository.findById(postId).get().getAuthor(), post, "COMMENT",  "You, commented on your post (" + post.getTitle() + ").");
    }
    else{
      notificationService.createNotification(postRepository.findById(postId).get().getAuthor(), post, "COMMENT", userDetails.getUsername() + ", commented on your post (" + post.getTitle() + ").");
    }
    return String.format("redirect:/forum/post/%d", id);
  }

  @PostMapping("/post/{id}/reply/{parentId}")
  public String addComment(@PathVariable int id, @PathVariable int parentId, 
                           @RequestParam String content, @AuthenticationPrincipal UserDetails userDetails){
    Post post = postRepository.findById(id).get();
    Comment comment = new Comment();
    comment.setUser(domainUserService.getByName(userDetails.getUsername()).get());
    comment.setPost(post);
    comment.setParent(commentRepository.findById(parentId).get());
    comment.setContent(content);
    commentRepository.save(comment);
    if(userRepository.findByName(userDetails.getUsername()).get().equals(post.getAuthor())){
      notificationService.createNotification(commentRepository.findById(parentId).get().getUser(), post, "REPLY", "You, replied on your comment (" + commentRepository.findById(parentId).get().getContent() + ").");
     }
    else{
      notificationService.createNotification(commentRepository.findById(parentId).get().getUser(), post, "REPLY", userDetails.getUsername() + " replied on your comment (" + commentRepository.findById(parentId).get().getContent() + ").");
    }
    return String.format("redirect:/forum/post/%d", id);
  }



  @GetMapping("/register")
  public String getRegistrationForm(Model model) {
    if (!model.containsAttribute("registrationForm")) {
      model.addAttribute("registrationForm", new RegistrationForm());
    }
    return "forum/register";
  }

  @PostMapping("/register")
  public String register(@ModelAttribute("registrationForm") RegistrationForm registrationForm, 
  BindingResult bindingResult, 
  RedirectAttributes attr) {
    if (bindingResult.hasErrors()) {
      attr.addFlashAttribute("org.springframework.validation.BindingResult.registrationForm", bindingResult);
      attr.addFlashAttribute("registrationForm", registrationForm);
      return "redirect:/register";
    }
    if (!registrationForm.isValid()) {
      attr.addFlashAttribute("message", "Passwords must match");
      attr.addFlashAttribute("registrationForm", registrationForm);
      return "redirect:/register";
    }
    System.out.println(domainUserService.save(registrationForm.getUsername(), registrationForm.getPassword()));
    attr.addFlashAttribute("result", "Registration success!");
    return "redirect:/login";
  }

  @GetMapping("/notifications")
  public String notificationPage( Model model, @AuthenticationPrincipal UserDetails userDetails, Principal principal) throws ResourceNotFoundException{
   // List<Notification> notificationList = notificationRepository.findAll();

   model.addAttribute("isLoggedIn", principal != null);
    if(principal != null){
      model.addAttribute("username", principal.getName());
    }

   User user = userRepository.findByName(userDetails.getUsername()).orElseThrow(() -> new ResourceNotFoundException("User not found"));
    System.out.println("inside get of notification");

    List<Notification> notificationList = notificationService.getNotificationsForUser(user);
    System.out.println(notificationList.toString());
    model.addAttribute("notificationList", notificationList);
    return "forum/notification";
  }

  @PostMapping("/notification/{notificationId}")
  public String handleNotificationForm(@PathVariable("notificationId") int postId) {
      System.out.println("Received notification ID: " + postId);
      System.out.println("----------------------------------------");
      return String.format("redirect:/forum/post/%d", postId); 
  }

  @GetMapping("/search")
  public String search(Model model, Principal principal, @RequestParam(name = "search", defaultValue = "") String text){
    model.addAttribute("isLoggedIn", principal != null);
    if(principal != null){
      model.addAttribute("username", principal.getName());
    }
    List<PostDTO> posts = postService.searchPostsByPattern(text.toLowerCase());
    System.out.println(posts);
    model.addAttribute("posts", posts);
    return "forum/allPosts";
  }


  
}
