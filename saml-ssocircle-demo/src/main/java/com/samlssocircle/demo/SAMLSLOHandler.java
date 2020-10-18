package com.samlssocircle.demo;

import com.onelogin.saml2.Auth;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

public class SAMLSLOHandler extends HttpServlet {
    private String HTML = "<!DOCTYPE html>\n" +
            "<html>\n" +
            "<head>\n" +
            "\t <meta charset=\"utf-8\">\n" +
            "\t <meta http-equiv=\"X-UA-Compatible\" content=\"IE=edge\">\n" +
            "     <meta name=\"viewport\" content=\"width=device-width, initial-scale=1\">\n" +
            "\t <title>A Java SAML Toolkit by OneLogin demo</title>\n" +
            "\t <link rel=\"stylesheet\" href=\"//maxcdn.bootstrapcdn.com/bootstrap/3.2.0/css/bootstrap.min.css\">\n" +
            "\n" +
            "     <!-- HTML5 Shim and Respond.js IE8 support of HTML5 elements and media queries -->\n" +
            "     <!-- WARNING: Respond.js doesn't work if you view the page via file:// -->\n" +
            "     <!--[if lt IE 9]>\n" +
            "       <script src=\"https://oss.maxcdn.com/html5shiv/3.7.2/html5shiv.min.js\"></script>\n" +
            "       <script src=\"https://oss.maxcdn.com/respond/1.4.2/respond.min.js\"></script>\n" +
            "     <![endif]-->\n" +
            "</head>\n" +
            "<body>\n" +
            "\t<div class=\"container\">\n" +
            "    \t<h1>A Java SAML Web Profile with SSO Circle demo</h1>\n" +
            "    \t<b>Logout</b>";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PrintWriter out = response.getWriter();
        out.println(HTML);
        try {
            Auth auth = new Auth(request, response);
            auth.processSLO();
            List<String> errors = auth.getErrors();

            if (errors.isEmpty()) {
                out.println("<p>Sucessfully logged out</p>");
                out.println("<a href=\"/saml-ssocircle-demo/saml-ssocircle-login\" class=\"btn btn-primary\">Login</a>");
            } else {
                out.println("<p>");
                for (String error : errors) {
                    out.println(" " + error + ".");
                }
                out.println("</p>");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        out.println("\t</div>\n" +
                "</body>\n" +
                "</html>");
    }
}
