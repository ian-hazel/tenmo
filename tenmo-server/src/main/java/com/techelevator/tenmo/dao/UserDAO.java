package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.User;

import java.util.List;

public interface UserDAO {

    List<User> findAll();

    User findByUsername(String username);

    // TODO: BUG: should not return void
    int findIdByUsername(String username);

    boolean create(String username, String password);
}
