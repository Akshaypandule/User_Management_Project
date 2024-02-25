package com.BrainWorks.User_Management_App.binding;

import lombok.Data;

@Data
public class ActivateAccount {

    private String email;
    private String newPassword;
    private String tempPassword;
    private String comPassword;

}
