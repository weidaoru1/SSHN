package com.tgb.service.impl;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import com.fus.cocoon.service.ModelServiceImpl;
import com.tgb.entity.User;
import com.tgb.service.UserService;
@Service("userService")
@Scope("singleton")
public class UserServiceImpl extends ModelServiceImpl<User> implements UserService{
	public void deleteUser(String userId) {
	}
	
	
}
