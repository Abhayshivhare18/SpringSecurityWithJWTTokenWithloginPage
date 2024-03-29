package com.customer.shubham.Filter;

import com.customer.shubham.serviceimpl.CustomUserDetailService;
import com.customer.shubham.serviceimpl.SecurityServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class JwtFilter extends OncePerRequestFilter {

    @Autowired
     private SecurityServiceImpl securityService;

    @Autowired
    private CustomUserDetailService userDetailService;


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
    String AuthorizationHeader = request.getHeader("Authorization");
     String token=null;
     String username=null;

    if(AuthorizationHeader!=null && AuthorizationHeader.startsWith("Bearer ")){
       token=AuthorizationHeader.substring(7);
        username=securityService.validaUserTokenToken(token);
    }
    if(username!=null && SecurityContextHolder.getContext().getAuthentication()==null){
        UserDetails userDetails =userDetailService.loadUserByUsername(username);
        if(securityService.validateToken(token,userDetails)){
            UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                    new UsernamePasswordAuthenticationToken(userDetails,null,userDetails.getAuthorities());
            usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource()
                    .buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
        }

    }
    filterChain.doFilter(request,response);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getServletPath();
        return path.equals("*/login") || path.equals("/someOtherPath"); // Add URLs you want to exclude
    }
}
