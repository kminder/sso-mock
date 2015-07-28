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

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.Principal;

public class EciSsoFilter implements Filter {

  public void doHttpFilter( HttpServletRequest request, HttpServletResponse response, FilterChain chain ) throws IOException, ServletException {
    final String xAuthToken = Util.getAuthToken( request );
    if( xAuthToken == null ) {
      Util.sendLoginRedirect( request, response );
    } else {
      request = new HttpServletRequestWrapper(request) {
        @Override
        public String getAuthType() {
          return "keystone";
        }

        @Override
        public String getRemoteUser() {
          return xAuthToken;
        }

        @Override
        public Principal getUserPrincipal() {
          return new Principal() {
            public String getName() {
              return xAuthToken;
            }
          };
        }
      };
      chain.doFilter( request, response );
    }
  }

  public void init( FilterConfig filterConfig ) throws ServletException {
  }

  public void destroy() {
  }

  @SuppressWarnings( "unchecked" )
  public void doFilter( ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain )
      throws IOException, ServletException {
    doHttpFilter( (HttpServletRequest)servletRequest, (HttpServletResponse)servletResponse, filterChain );
  }

}
