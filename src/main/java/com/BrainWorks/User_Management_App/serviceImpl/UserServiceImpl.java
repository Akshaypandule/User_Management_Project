package com.BrainWorks.User_Management_App.serviceImpl;

import com.BrainWorks.User_Management_App.binding.ActivateAccount;
import com.BrainWorks.User_Management_App.binding.Login;
import com.BrainWorks.User_Management_App.binding.User;
import com.BrainWorks.User_Management_App.constant.AppConstant;
import com.BrainWorks.User_Management_App.entity.UserMaster;
import com.BrainWorks.User_Management_App.props.AppProperties;
import com.BrainWorks.User_Management_App.repo.UserMasterRepo;
import com.BrainWorks.User_Management_App.service.UserService;
import com.BrainWorks.User_Management_App.utils.EmailUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.*;

@Service
public class UserServiceImpl implements UserService {

    private UserMasterRepo userMasterRepo;

    private AppProperties appProperties;

    @Autowired
    private EmailUtils emailUtils;

    Map<String,String> message;


    public UserServiceImpl(UserMasterRepo userMasterRepo,AppProperties appProperties){
        this.userMasterRepo=userMasterRepo;
        this.message=appProperties.getMessage();
    }
    @Override
    public boolean saveUser(User user) {

        UserMaster entity=new UserMaster();
        BeanUtils.copyProperties(user,entity);
        entity.setPassword(generateRandomPass());
        entity.setAccStatus("In-Active");
        UserMaster save = userMasterRepo.save(entity);
        String subject="Your Registration success";
        String fileName="REG-EMAIL-BODY.txt";
        String body=readEmailBody(entity.getFullName(),entity.getPassword(),fileName);
        emailUtils.sendEmail(user.getEmail(),subject,body);
        return save.getId()!=null;
    }

    @Override
    public boolean activateAccount(ActivateAccount activateAccount)
    {
        UserMaster entity=new UserMaster();
        entity.setEmail(activateAccount.getEmail());
        entity.setPassword(activateAccount.getTempPassword());
        Example<UserMaster> userMasterExample = Example.of(entity);

        // Select *from userMaster where email=?,password=?;
        List<UserMaster> all = userMasterRepo.findAll(userMasterExample);
        if(all.isEmpty()){
            return false;
        }
        else{
            UserMaster userMaster = all.get(0);
            userMaster.setPassword(activateAccount.getNewPassword());
            userMaster.setAccStatus("Active");
            userMasterRepo.save(userMaster);
            return true;
        }
    }

    @Override
    public User getUserById(Integer id) {

        Optional<UserMaster> byId = userMasterRepo.findById(id);
        if(byId.isPresent()){
            User user=new User();
            UserMaster userMaster = byId.get();
            BeanUtils.copyProperties(byId,user);
            return user;
        }
        return null;
    }

    @Override
    public List<User> getAllUser() {
        List<UserMaster> findAll = userMasterRepo.findAll();
        ArrayList<User> users = new ArrayList<>();
        for(UserMaster entity:findAll){
            User user=new User();
            BeanUtils.copyProperties(entity,user);
            users.add(user);
        }
        return users;
    }

    @Override
    public boolean deleteUser(Integer id) {
        try {
            userMasterRepo.deleteById(id);
            return true;
        }
        catch(Exception e){
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean changeStatus(Integer id, String accStatus) {
        Optional<UserMaster> byId = userMasterRepo.findById(id);
        if(byId.isPresent()){

            UserMaster userMaster = byId.get();
            userMaster.setAccStatus(accStatus);
            userMasterRepo.save(userMaster);
            return true;
        }
        return false;
    }

    @Override
    public String login(Login login) {
        
        UserMaster userMaster=new UserMaster();
        userMaster.setEmail(login.getEmail());
        userMaster.setPassword(login.getPassword());

        Example<UserMaster> of = Example.of(userMaster);

        List<UserMaster> findAll = userMasterRepo.findAll(of);

        if(findAll.isEmpty()){
            return message.get(AppConstant.invalid);
        }
        else{
            UserMaster userMaster1 = findAll.get(0);

            if(userMaster1.getAccStatus().equals("Active")){
                return message.get(AppConstant.Success);
            }
            else{
                return message.get(AppConstant.Not_Activated);
            }
        }
    }
    @Override
    public String forgotPassword(String email) {

        UserMaster byEmail = userMasterRepo.findByEmail(email);
        if(byEmail==null){
            return message.get(AppConstant.Invalid_Email);
        }

        String subject="Forgot Password";
        String filename="REG-EMAIL-BODY.txt";

        String body=readEmailBody(byEmail.getFullName(),byEmail.getPassword(),filename);

        boolean sendMail = emailUtils.sendEmail(email, subject, body);

        if(sendMail)
        {
            return "PassWord Send to your registered mail";
        }
        // TODO: Send Password to user to email;

        return null;
    }
    private String generateRandomPass(){
        String upperCase="ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String lowerCase="abcdefghijklmnopqrstuvwxyz";
        String number="0123456789";
        String alphaNumeric=upperCase+lowerCase+number;
        StringBuilder sb=new StringBuilder();
        Random random=new Random();
        int length=6;
        for(int i=0;i<=length;i++){
            int index = random.nextInt(alphaNumeric.length());
            char randomChar = alphaNumeric.charAt(index);
            StringBuilder password = sb.append(randomChar);
        }
        return sb.toString();

    }
    private String readEmailBody(String fullName,String pwd,String fileName){
       // String filename="REG-EMAIL_BODY.txt";
        String url="";
        String mailBody=null;

        try{
            FileReader file=new FileReader(fileName);  // Read the data char by char
            BufferedReader br=new BufferedReader(file); // Read the data line by line

            StringBuffer buffer=new StringBuffer();

            String line = br.readLine(); 
            while(line!=null){
                buffer.append(line);
                line=br.readLine();
            }
            br.close();
             mailBody = buffer.toString();
            mailBody = mailBody.replace("{FULLNAME}", fullName);
            mailBody=mailBody.replace("{TEMP-PWD}",pwd);
            mailBody=mailBody.replace("{URL}",url);
            mailBody=mailBody.replace("{pwd}",pwd);
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return mailBody;
    }
}
