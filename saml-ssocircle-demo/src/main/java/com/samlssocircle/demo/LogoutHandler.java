package com.samlssocircle.demo;

import com.onelogin.saml2.Auth;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class LogoutHandler extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            Auth auth = new Auth(request, response);
            HttpSession session = request.getSession();
            String nameId = null;
            if (session.getAttribute("nameId") != null) {
                nameId = session.getAttribute("nameId").toString();
            }
            String nameIdFormat = null;
            if (session.getAttribute("nameIdFormat") != null) {
                nameIdFormat = session.getAttribute("nameIdFormat").toString();
            }
            String nameidNameQualifier = null;
            if (session.getAttribute("nameidNameQualifier") != null) {
                nameidNameQualifier = session.getAttribute("nameidNameQualifier").toString();
            }
            String nameidSPNameQualifier = null;
            if (session.getAttribute("nameidSPNameQualifier") != null) {
                nameidSPNameQualifier = session.getAttribute("nameidSPNameQualifier").toString();
            }
            String sessionIndex = null;
            if (session.getAttribute("sessionIndex") != null) {
                sessionIndex = session.getAttribute("sessionIndex").toString();
            }
            auth.logout(null, nameId, sessionIndex, nameIdFormat, nameidNameQualifier, nameidSPNameQualifier);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
