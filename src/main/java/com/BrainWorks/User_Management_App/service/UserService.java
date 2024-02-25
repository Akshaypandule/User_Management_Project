package com.BrainWorks.User_Management_App.service;

import com.BrainWorks.User_Management_App.binding.ActivateAccount;
import com.BrainWorks.User_Management_App.binding.Login;
import com.BrainWorks.User_Management_App.binding.User;

import java.util.List;

public interface UserService {

    public boolean saveUser(User user);
    public boolean activateAccount(ActivateAccount activateAccount);
    public User getUserById(Integer id);
    public List<User> getAllUser();
    public boolean deleteUser(Integer id);
    public boolean changeStatus(Integer id,String accStatus);
    public String login(Login login);
    public String forgotPassword(String email);
}
