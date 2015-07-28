package com.hortonworks.example.util;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.net.URLCodec;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.Principal;

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

public class Util {

  private static URLCodec CODEC = new URLCodec();

  public static String urlEncode( String s ) {
    try {
      return CODEC.encode( s );
    } catch( EncoderException e ) {
      throw new RuntimeException( e );
    }
  }

  public static String urlDecode( String s ) {
    try {
      return CODEC.decode( s );
    } catch( DecoderException e ) {
      throw new RuntimeException( s );
    }
  }

  public static String getFirstOrNull( String[] array ) {
    if( array == null || array.length < 1 ) {
      return null;
    } else {
      return array[0];
    }
  }

  public static Cookie getCookie( HttpServletRequest request, String name ) {
    for( Cookie cookie : request.getCookies() ) {
      if( cookie.getName().equalsIgnoreCase( name ) ) {
        return cookie;
      }
    }
    return null;
  }

  public static String getCookieInfo( HttpServletRequest request, String name ) {
    String info = null;
    Cookie cookie = getCookie( request, name );
    if( cookie != null ) {
      info = String.format("[value=%s,domain=%s,path=%s,expiry=%d]", cookie.getValue(), cookie.getDomain(), cookie.getPath(), cookie.getMaxAge() );
    }
    return info;
  }

  public static String getCookieValue( HttpServletRequest request, String name ) {
    String value = null;
    Cookie cookie = getCookie( request, name );
    if( cookie != null ) {
      value = cookie.getValue();
    }
    return value;
  }

  public static String getRequestUrl( HttpServletRequest request ) {
    StringBuffer url = request.getRequestURL();
    String query = request.getQueryString();
    if( query != null ) {
      url.append( "?" );
      url.append( query );
    }
    String s = url.toString();
    return s;
  }

  public static String getUrlEncodedRequestUrl( HttpServletRequest request ) {
    String s = getRequestUrl( request );
    String e = urlEncode( s );
    return e;
  }

  public static String getAuthToken( HttpServletRequest request ) {
    String token = getCookieValue( request, "X-Auth-Token" );
    if( !isAuthTokenValid( token ) ) {
      token = null;
    }
    return token;
  }

  public static boolean isAuthTokenValid( String token ) {
    return token != null;
  }

  public static void sendLoginRedirect( HttpServletRequest request, HttpServletResponse response ) throws IOException {
    response.sendRedirect( "http://localhost:2222/login/?referrer=" + getUrlEncodedRequestUrl( request ) );
  }

  public static String getPrincipalName( HttpServletRequest request ) {
    String n = null;
    Principal p = request.getUserPrincipal();
    if( p != null ) {
      n = p.getName();
    }
    return n;
  }
}
