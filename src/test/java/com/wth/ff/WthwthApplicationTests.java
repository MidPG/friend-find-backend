package com.wth.ff;

import com.wth.ff.model.domain.User;
import com.wth.ff.service.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.DigestUtils;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;

@SpringBootTest
class WthwthApplicationTests {

	@Resource
	private UserService userService;
	
	
	@Test
	void contextLoads() {
		final String SATL = "wth";
		String newPassword = DigestUtils.md5DigestAsHex(("wth0123").getBytes());
		System.out.println(newPassword);

	}
	
	@Test
	public void testSearchUsersByTags() {

		List<String> tagNameList = Arrays.asList("java","python");
		List<User> userList = userService.searchUsersByTags(tagNameList);
		Assertions.assertNotNull(userList);
		System.out.println(userList);
	 }

}
