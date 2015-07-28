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
package com.hortonworks.example.ui;

import com.hortonworks.example.util.Util;
import org.apache.hadoop.security.authentication.client.AuthenticationException;
import org.apache.hadoop.security.authentication.server.AltKerberosAuthenticationHandler;
import org.apache.hadoop.security.authentication.server.AuthenticationToken;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Properties;

public class HadoopSsoHandler extends AltKerberosAuthenticationHandler {

  @Override
  public void init(Properties config) throws ServletException {
    super.init( config );
  }

  @Override
  public AuthenticationToken alternateAuthenticate( HttpServletRequest request, HttpServletResponse response )
      throws IOException, AuthenticationException {
    String xAuthToken = Util.getAuthToken( request );
    if( xAuthToken == null ) {
      Util.sendLoginRedirect( request, response );
      return null;
    } else {
      return new AuthenticationToken( xAuthToken, xAuthToken, "keystone" );
    }
  }

}
