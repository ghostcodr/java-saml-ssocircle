package com.samlssocircle.demo;

import com.onelogin.saml2.Auth;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class LoginHandler extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            Auth auth = new Auth(request, response);
            if (request.getParameter("attrs") == null) {
                auth.login();
            } else {
                auth.login(request.getContextPath() + "/attrs.jsp");
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
