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
package com.hortonworks.example.ambari;

import com.hortonworks.example.util.Util;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class AmbariSsoFilter extends GenericFilterBean {

  public void doHttpFilter( HttpServletRequest request, HttpServletResponse response, FilterChain chain ) throws IOException, ServletException {
    String xAuthToken = Util.getAuthToken( request );
    if( xAuthToken != null ) {
      List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
      authorities.add( new SimpleGrantedAuthority( "ROLE_ADMIN" ) );
      Authentication authentication = new PreAuthenticatedAuthenticationToken( xAuthToken, null, authorities );
      SecurityContextHolder.getContext().setAuthentication( authentication );
    } else {
      SecurityContextHolder.getContext().setAuthentication( null );
    }
    chain.doFilter( request, response );
  }

  @SuppressWarnings( "unchecked" )
  public void doFilter( ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain ) throws IOException, ServletException {
    doHttpFilter( (HttpServletRequest)servletRequest, (HttpServletResponse)servletResponse, filterChain );
  }
}
