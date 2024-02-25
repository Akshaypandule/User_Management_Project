package com.BrainWorks.User_Management_App.rest;

import com.BrainWorks.User_Management_App.binding.ActivateAccount;
import com.BrainWorks.User_Management_App.binding.Login;
import com.BrainWorks.User_Management_App.binding.User;
import com.BrainWorks.User_Management_App.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("user")
public class UserRestController {

    @Autowired
    private UserService userService;

    @PostMapping("/user")
    public ResponseEntity<String> createUser(@RequestBody User user){
        boolean saveUser = userService.saveUser(user);
        if(saveUser)
        {
            return new ResponseEntity<>("Registration Success", HttpStatus.CREATED);
        }
        else
        {
            return new ResponseEntity<>("Registration Failed",HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @PostMapping("/activate")
    public ResponseEntity<String> ActivateAccount(@RequestBody ActivateAccount activateAccount)
    {
        boolean accountActivated = userService.activateAccount(activateAccount);
        if(accountActivated)
        {
            return new ResponseEntity<>("Account Activated",HttpStatus.OK);
        }
        else {
            return new ResponseEntity<>("Invalid Temporary Password",HttpStatus.BAD_REQUEST);        }
    }

    @GetMapping("/user")
    public ResponseEntity<List<User>> getAllUser(){
        List<User> allUser = userService.getAllUser();
        return new ResponseEntity<>(allUser,HttpStatus.OK);
    }
    @GetMapping("/user/{userId}")
    public ResponseEntity<User> getSingleUser(@PathVariable Integer userId){
        User userById = userService.getUserById(userId);
        return new ResponseEntity<>(userById,HttpStatus.OK);
    }
    @DeleteMapping("/user/{userId}")
    public ResponseEntity<String> deleteUserById(@PathVariable Integer userId){
        boolean deleteUser = userService.deleteUser(userId);
        if(deleteUser)
        {
            return new ResponseEntity<>("User Delete Successfully",HttpStatus.OK);
        }
        else
        {
            return new ResponseEntity<>("Failed to delete user",HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @GetMapping("status/{id}/{status}")
    public ResponseEntity<String> statusChange(@PathVariable Integer id,@PathVariable String status){
        boolean isChanged = userService.changeStatus(id, status);
        if(isChanged)
        {
            return new ResponseEntity<>("Status Changed",HttpStatus.OK);
        }
        else {
            return new ResponseEntity<>("Failed to Change",HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @PostMapping("/login")
    public ResponseEntity<String> userLogin(@RequestBody Login login)
    {
        String status = userService.login(login);
        return new ResponseEntity<>(status,HttpStatus.OK);
    }
    @GetMapping("/forgotpwd/{email}")
    public ResponseEntity<String> forgetPwd(@PathVariable  String email)
    {
        String status = userService.forgotPassword(email);
        return new ResponseEntity<>(status,HttpStatus.OK);
    }

}
