package com.tgb.service;

import com.fus.cocoon.service.ModelService;
import com.tgb.entity.User;
public interface UserService extends ModelService<User> {
	public void deleteUser(String  userId);
}
