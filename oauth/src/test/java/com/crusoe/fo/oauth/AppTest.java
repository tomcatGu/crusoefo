package com.crusoe.fo.oauth;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.List;

import javax.transaction.Transactional;

import com.crusoe.fo.usercenter.entity.Department;
import com.crusoe.fo.usercenter.entity.Role;
import com.crusoe.fo.usercenter.entity.User;
import com.crusoe.fo.usercenter.repository.DepartmentRepository;
import com.crusoe.fo.usercenter.repository.UserRepository;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;


/**
 * Unit test for simple App.
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class AppTest {

    @Autowired
    UserRepository userRepository;
    @Autowired
    DepartmentRepository departmentRepository;

    @Test
    //@Transactional
    public void testUserRepository() {
        List<User> users=userRepository.findAll();
        assertEquals(0, users.size());

        //Department dept1=departmentRepository.findByName("root node");

        //assertEquals(0, dept1.getUserList().size());

        
        Department dept=new Department();
        dept.setName("root node");
        dept.setParent(null);
        departmentRepository.save(dept);


        Role role=new Role();
        role.setRolename("res1");
        User user=new User();
        user.setUsername("admin");
        user.setPassword(new BCryptPasswordEncoder().encode("123456"));
        user.getRoleList().add(role);
        user.setDepartment(dept);
        userRepository.save(user);
        assertTrue(user.getId()>0);



        


    }
}
