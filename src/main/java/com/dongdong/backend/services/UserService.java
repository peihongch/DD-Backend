package com.dongdong.backend.services;

import com.dongdong.backend.Repository.UserRepository;
import com.dongdong.backend.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public Long signInByEmail(String email, String password){
        User user=new User();
        user.setEmail(email);
        user.setPassword(password);
        User res=userRepository.save(user);
        return res.getUserId();
    }

    public Long signInByPhone(String phone, String password){
        User user=new User();
        user.setPhone(phone);
        user.setPassword(password);
        User res=userRepository.save(user);
        return res.getUserId();
    }

    public User login(String userId,String password){
        try{
            Long id=Long.parseLong(userId);
            Optional<User> opt=userRepository.findById(id);
            User user=opt.get();
            if(user.getPassword().equals(password)){
                return user;
            }else{
                return null;
            }
        }catch (NumberFormatException e){
            e.printStackTrace();
            return null;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    public User getUser(String userId){
        try{
            Long id=Long.parseLong(userId);
            Optional<User> opt=userRepository.findById(id);
            User user=opt.get();;
            return user;
        }catch (NumberFormatException e){
            e.printStackTrace();
            return null;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    public List<User> searchUser(String username){
        try{
            List<User> res=userRepository.searchUser("%"+username+"%");
            return res;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }

    }
}