package com.prodapt.learningspring.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.prodapt.learningspring.dto.CommentDTO;
import com.prodapt.learningspring.entity.Comment;
import com.prodapt.learningspring.entity.Tag;
import com.prodapt.learningspring.entity.User;
import com.prodapt.learningspring.repository.CommentRepository;
import com.prodapt.learningspring.repository.TagRepository;
import com.prodapt.learningspring.repository.UserRepository;

@Service
public class CommentService {

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TagRepository tagRepository;

    private Map<Integer, List<Comment>> commentTree;
    private Map<Integer, Integer> level;

    public List<CommentDTO> findAllByPostIdLimitBy(int id, int limit){
        return preOrder((List<Comment>) commentRepository.findAllByPostIdLimitBy(id, limit));
    }

    public List<CommentDTO> preOrder(List<Comment> comments){
        commentTree = new HashMap<>();
        level = new HashMap<>();
        List<Comment> preOrderedComments = new ArrayList<>();
        for(Comment comment: comments) {
            commentTree.put(comment.getId(), new ArrayList<>());
            if(comment.getParent() != null){
                commentTree.get(comment.getParent().getId()).add(comment);
            }
        }
        for(Comment comment: comments) {
            if(comment.getParent() == null){
                level.put(comment.getId(), 0);
                preOrderedComments.addAll(dfs(comment, 1));
            }
        }
        List<CommentDTO> commentDTOs = new ArrayList<>();
        for(Comment comment: preOrderedComments){
            CommentDTO c = new CommentDTO();
            c.setId(comment.getId());
            c.setUser(comment.getUser());
            c.setPost(comment.getPost());
            c.setContent(comment.getContent());
            c.setParent(comment.getParent());
            c.setCreatedAt(comment.getCreatedAt());
            c.setUpdatedAt(comment.getUpdatedAt());
            c.setLevel(level.get(comment.getId()));
            commentDTOs.add(c);
        }
        return commentDTOs;
    }

    public List<Comment> dfs(Comment comment, int lvl){
        level.put(comment.getId(), lvl);
        List<Comment> comments = new ArrayList<>();
        comments.add(comment);
        for(Comment c: commentTree.get(comment.getId())){
            comments.addAll(dfs(c, lvl + 1));
        }
        return comments;
    }

    public void saveTags(Comment comment) {
        String[] words = comment.getContent().split(" ");
        for(String word: words) {
            if(word.substring(0, 1).equals("@")) {
                String username = word.substring(1);
                if(userRepository.findByName(username).isPresent()) {
                    User user = userRepository.findByName(username).get();
                    Tag tag = new Tag();
                    tag.setComment(comment);
                    tag.setUser(user);
                    tagRepository.save(tag);
                }
            }
        }
    }
}