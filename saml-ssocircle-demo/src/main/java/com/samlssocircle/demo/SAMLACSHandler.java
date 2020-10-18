package com.samlssocircle.demo;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.onelogin.saml2.Auth;
import com.onelogin.saml2.exception.SettingsException;
import com.onelogin.saml2.servlet.ServletUtils;
import org.apache.commons.lang3.StringUtils;

public class SAMLACSHandler extends HttpServlet {
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
            "    \t<h1>A Java SAML Web Profile with SSO Circle demo</h1>";

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Auth auth = null;
        try {
            auth = new Auth(request, response);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            auth.processResponse();
        } catch (Exception e) {
            e.printStackTrace();
        }
        PrintWriter out = response.getWriter();
        out.println(HTML);
        HttpSession session = request.getSession();
        if (!auth.isAuthenticated()) {
            out.println("<div class=\"alert alert-danger\" role=\"alert\">Not authenticated</div>");
        }
        List<String> errors = auth.getErrors();

        if (!errors.isEmpty()) {
            out.println("<p>" + StringUtils.join(errors, ", ") + "</p>");
            if (auth.isDebugActive()) {
                String errorReason = auth.getLastErrorReason();
                if (errorReason != null && !errorReason.isEmpty()) {
                    out.println("<p>" + auth.getLastErrorReason() + "</p>");
                }
            }
            out.println("<a href=\"/saml-ssocircle-demo/saml-ssocircle-login\" class=\"btn btn-primary\">Login</a>");
        } else {
            Map<String, List<String>> attributes = auth.getAttributes();
            String nameId = auth.getNameId();
            String nameIdFormat = auth.getNameIdFormat();
            String sessionIndex = auth.getSessionIndex();
            String nameidNameQualifier = auth.getNameIdNameQualifier();
            String nameidSPNameQualifier = auth.getNameIdSPNameQualifier();

            session.setAttribute("attributes", attributes);
            session.setAttribute("nameId", nameId);
            session.setAttribute("nameIdFormat", nameIdFormat);
            session.setAttribute("sessionIndex", sessionIndex);
            session.setAttribute("nameidNameQualifier", nameidNameQualifier);
            session.setAttribute("nameidSPNameQualifier", nameidSPNameQualifier);


            String relayState = request.getParameter("RelayState");

            String selfRoutedURLNoQuery = ServletUtils.getSelfRoutedURLNoQuery(request);
            System.out.println(relayState+ " ##################### " + selfRoutedURLNoQuery);
            if (relayState != null && !relayState.isEmpty() && !relayState.equals(selfRoutedURLNoQuery) &&
                    !relayState.contains("/saml-ssocircle-login")) { // We don't want to be redirected to login.jsp neither
                response.sendRedirect(request.getParameter("RelayState"));
            } else {
                if (attributes.isEmpty()) {
                    out.println("<div class=\"alert alert-danger\" role=\"alert\">You don't have any attributes</div>");
                } else {
                    out.println("<table class=\"table table-striped\">\n" +
                            "\t      \t\t\t\t<thead>\n" +
                            "\t      \t\t\t\t\t<tr>\n" +
                            "\t        \t\t\t\t\t<th>Name</th>\n" +
                            "\t        \t\t\t\t\t<th>Values</th>\n" +
                            "\t        \t\t\t\t</tr>\n" +
                            "\t      \t\t\t\t</thead>\n" +
                            "\t      \t\t\t\t<tbody>");
                    Collection<String> keys = attributes.keySet();
                    for(String name :keys){
                        out.println("<tr><td>" + name + "</td><td>");
                        List<String> values = attributes.get(name);
                        for(String value :values) {
                            out.println("<li>" + value + "</li>");
                        }

                        out.println("</td></tr>");
                    }
                    out.println("\t</tbody>\n" +
                            "\t\t\t\t\t</table>");
                }
                out.println("<a href=\"attrs.jsp\" class=\"btn btn-primary\">See user data stored at session</a>\n" +
                        "\t\t\t\t<a href=\"/saml-ssocircle-demo/saml-ssocircle-logout\" class=\"btn btn-primary\">Logout</a>");
            }
        }
        out.println("</div>\n" +
                "</body>\n" +
                "</html>");
    }
}
