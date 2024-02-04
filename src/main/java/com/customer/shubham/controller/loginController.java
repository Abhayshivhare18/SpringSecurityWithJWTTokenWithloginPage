package com.customer.shubham.controller;

import com.customer.shubham.dto.TokenRequestDto;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;


@Controller
public class loginController {




    @RequestMapping(method = RequestMethod.GET,value ="/dashBoard")
    public String defaultAfterLogin() {

        //model.addAttribute("downloadImageUrl",);
        return "dashBoard";
    }
}
