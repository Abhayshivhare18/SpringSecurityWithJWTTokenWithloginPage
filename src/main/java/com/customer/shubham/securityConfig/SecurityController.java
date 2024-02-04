package com.customer.shubham.securityConfig;

import com.customer.shubham.dto.TokenRequestDto;
import com.customer.shubham.serviceimpl.SecurityServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class SecurityController {



    private AuthenticationManager authenticationManager;

    private SecurityServiceImpl securityService;


    @Autowired
      public SecurityController(final AuthenticationManager authenticationManager,
                                final SecurityServiceImpl securityService) {
          this.authenticationManager = authenticationManager;
                  this.securityService = securityService;
      }

    @PostMapping(value = {"/getAuthToken"})
    public Map<String, String> getAuthToken(@RequestBody TokenRequestDto requestDto) throws Exception {
     try {
         authenticationManager.authenticate(
                 new UsernamePasswordAuthenticationToken(requestDto.getUsername(), requestDto.getPassword())
         );
     }
        catch (Exception e){

         throw  new Exception("Invalid username/password");
        }
        Map<String, String> map = new HashMap<>();
        String[] token = securityService.getAuthorizationHeader(requestDto).trim().split(" ");
        map.put("authToken", token[1]);
        return map;
    }
}
