package com.fish.controller;

import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fish.entity.User;
import com.fish.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

@Controller
@RequestMapping("/user")
public class UserController {

    @Resource
    private UserService userService;

    @PostMapping("/{id}")
    @ResponseBody
    public String selectUser(@RequestBody Map<String, String> param, @PathVariable("id") long userId) throws IOException {
        User user = this.userService.selectUser(userId);
        ObjectMapper mapper = new ObjectMapper();
        System.out.println("++++++++++++++++++++++++++" + param);
        System.out.println(param.get("username"));
        return JSON.toJSONString(user);
    }

}