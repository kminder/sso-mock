/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.hortonworks.example.eci;

import com.hortonworks.example.util.Util;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Writer;

public class EciLogin extends HttpServlet {

  protected void doGet( HttpServletRequest req, HttpServletResponse resp ) throws ServletException, IOException {
    String referrer = Util.getFirstOrNull( req.getParameterValues( "referrer" ) );
    Writer w = resp.getWriter();
    w.write( "<html>" );
    w.write( "<body>" );
    w.write( "SSO Login" );
    w.write( "<p/>" );
    w.write( "X-Auth-Token: " + Util.getCookieInfo( req, "X-Auth-Token" ) );
    w.write( "<p/>" );
    w.write( "Referrer: " + referrer );
    w.write( "<p/>" );
    w.write( "<form id=\"login\" action=\"/login\" method=\"POST\">" );
    w.write( "Username: <input type=\"text\" name=\"username\"><br>" );
    w.write( "Password: <input type=\"text\" name=\"password\"><br>" );
    w.write( "Referrer: <input type=\"text\" name=\"referrer\" size=\"64\"value=\"" + ( referrer == null ? "" : referrer ) + "\"/>" );
    w.write( "</form>" );
    w.write( "<button type=\"submit\" form=\"login\" value=\"Submit\">Login</button>" );
    w.write( "</body>" );
    w.write( "</html>" );
  }

  protected void doPost( HttpServletRequest req, HttpServletResponse resp ) throws ServletException, IOException {
    Cookie cookie = new Cookie( "X-Auth-Token", "" );
    cookie.setMaxAge( 0 );
    String username = getAuthenticatedUsername( req );
    String referrer = req.getParameter( "referrer" );
    if( username == null ) {
      resp.addCookie( cookie );
      if( referrer != null ) {
        resp.sendRedirect( "/login?referrer=" + Util.urlEncode( referrer ) );
      } else {
        resp.sendRedirect( "/login" );
      }
    } else {
      cookie.setValue( username );
      cookie.setMaxAge( 120 );
      resp.addCookie( cookie );
      if( referrer == null || referrer.isEmpty() ) {
        referrer = "/eci";
      }
      resp.sendRedirect( referrer );
    }
  }

  private String getAuthenticatedUsername( HttpServletRequest req ) {
    String username = req.getParameter( "username" );
    String password = req.getParameter( "password" );
    if( username == null || username.isEmpty() || password == null || !password.equals( username + "-password" ) ) {
      return null;
    } else {
      return username;
    }

  }

}
