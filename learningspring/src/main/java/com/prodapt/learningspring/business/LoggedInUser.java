package com.prodapt.learningspring.business;

import com.prodapt.learningspring.entity.User;

import lombok.Data;

@Data
public class LoggedInUser {
    private User loggedInUser;
}

