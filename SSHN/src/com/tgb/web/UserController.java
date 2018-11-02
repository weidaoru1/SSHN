package com.tgb.web;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fus.cocoon.base.action.BaseAction;
import com.fus.cocoon.dict.SessionLoginUser;
import com.fus.cocoon.service.ModelService;
import com.fus.cocoon.utils.WebContextUtils;
import com.fus.cocoon.utils.web.WebUtils;
import com.tgb.entity.User;
import com.tgb.service.UserService;
@SuppressWarnings("serial")
@Controller  
@RequestMapping("/user")
public class UserController extends BaseAction<User> {
	@Resource(name="modelService")
	private ModelService<User> modelService;
	@Resource(name="userService")
	private UserService userService;
	@RequestMapping(value = "/UserLogin")
	public @ResponseBody Map<String,Object> UserLogin(Model model) throws Exception{
		Map<String,Object> map = new HashMap<String, Object>();
		try {
//			String name=new String(super.getRequest().getParameter("userName").getBytes("ISO-8859-1"),"utf-8");
			String name = WebUtils.decodeURL(super.getRequest().getParameter("userName"), "utf-8");
			System.out.println("dsdsd:"+name);
//			super.getRequest().setCharacterEncoding("utf-8");
			User u = new User();
			u.setId("4028810165b3202f0165b320858a0000");
			u.setAddress("河北");
			u.setPassword("1234567890");
			u.setPhoneNumber("18276637756");
			u.setUserName("zhangsan");
//			Map<String,Object> maps = modelService.findSingleByConditionToMap("select usr.* from co_user_info usr where 1 = 1", u);
//			u = modelService.findSingleByCondition("select usr.* from co_user_info usr where 1 = 1", u);
//			List<User> ulist = modelService.findByCondition("select usr.* from co_user_info usr where 1 = 1", u);
//			System.out.println(ulist.get(0).getUserName());
//			System.out.println("USERNAME:"+map.get("USERNAME"));
//			System.out.println(u.getUserName());
//			modelService.saveOrUpdate(u);
//			userService.deleteUser("4028810165b2ba960165b2bb3fc30000");//usr
			super.getRequest().getSession().setAttribute(SessionLoginUser.SESSION_LOGIN_USER.name(), u);
			super.getRequest().getSession().setAttribute(SessionLoginUser.SESSION_LOGIN_REAL_NAME.name(), "张三");
			super.getRequest().getSession().setAttribute(SessionLoginUser.SESSION_LOGIN_USER_NAME.name(), "zhangsan");
//			modelService.saveOrUpdate(u);
//			userManager.saveUser(u);
			map.put("msg", u);
			map.put("userName", name);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return map;
	}
	@RequestMapping(value = "/addUer")
	public String addUer(Model model) throws Exception{
		try {
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "/addUser";
	}
	
}
