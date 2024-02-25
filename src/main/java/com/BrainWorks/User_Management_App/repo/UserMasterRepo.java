package com.BrainWorks.User_Management_App.repo;

import com.BrainWorks.User_Management_App.entity.UserMaster;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserMasterRepo extends JpaRepository<UserMaster,Integer> {

   public UserMaster findByEmail(String email);
}
