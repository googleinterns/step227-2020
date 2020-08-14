// Copyright 2020 Google LLC
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     https://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package com.google.sps.servlets;

import com.google.appengine.api.datastore.*;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.google.gson.Gson;
import com.google.sps.data.User;
import java.io.IOException;
import java.util.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/** Responsible for storing user info. */
@WebServlet("/user-info")
public class ProfileInfoServlet extends HttpServlet {

  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
    UserService userService = UserServiceFactory.getUserService();
    // Check if user is logged in.
    if (!userService.isUserLoggedIn()) {
      System.out.println("ERROR:You are not logged in!");
      response.sendRedirect("/index.html");
      return;
    }
    // Get the input from the form.
    String fname = getParameter(request, "first -name", "Not set");
    String lname = getParameter(request, "last-name", "Not set");
    String nickname = getParameter(request, "nickname", "Anonym");
    boolean notifications;
    if ((getParameter(request, "radio", "mute")) == "mute") {
      notifications = false;
    } else {
      notifications = true;
    }

    // Get user's email.
    String email = userService.getCurrentUser().getEmail();

    // Store the comments as entities.
    Entity userEntity = new Entity("User", email);
    userEntity.setProperty("firstName", fname);
    userEntity.setProperty("lastName", lname);
    userEntity.setProperty("nickname", nickname);
    userEntity.setProperty("notifications", notifications);

    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    datastore.put(userEntity);

    // Redirect back to the HTML page - comments section.
    response.sendRedirect("/profile.html");
  }

  /**
   * Return the request parameter, or the default value if the parameter was not specified by the
   * client.
   */
  private String getParameter(HttpServletRequest request, String name, String defaultValue) {
    String value = request.getParameter(name);
    if (value == null || value.isEmpty()) {
      return defaultValue;
    }
    return value;
  }

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    UserService userService = UserServiceFactory.getUserService();

    Key userKey = KeyFactory.createKey("User", userService.getCurrentUser().getEmail());
    User currentUser;

    try {
      Entity userEntity = datastore.get(userKey);
      currentUser =
          new User(
              (String) userEntity.getProperty("firstName"),
              (String) userEntity.getProperty("lastName"),
              (String) userEntity.getProperty("nickname"),
              (boolean) userEntity.getProperty("notifications"));
    } catch (Exception e) {
      currentUser = new User("Set first name...", "Set last name...", "Set nickname...", false);
    }

    Gson gson = new Gson();

    // Respond with the user details.
    response.setContentType("application/json;");
    response.getWriter().println(gson.toJson(currentUser));
  }
}