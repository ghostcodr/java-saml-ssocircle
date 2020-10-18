package com.samlssocircle.demo;

import com.onelogin.saml2.Auth;
import com.onelogin.saml2.settings.Saml2Settings;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

public class MetaDataHandler extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            PrintWriter out = response.getWriter();
            Auth auth = new Auth();
            Saml2Settings settings = auth.getSettings();
            settings.setSPValidationOnly(true);
            List<String> errors = settings.checkSettings();

            if (errors.isEmpty()) {
                String metadata = settings.getSPMetadata();
                out.println(metadata);
            } else {
                response.setContentType("text/html; charset=UTF-8");

                for (String error : errors) {
                    out.println("<p>"+error+"</p>");
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
