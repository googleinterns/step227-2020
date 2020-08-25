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
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.google.gson.Gson;
import com.google.sps.data.Route;
import com.google.sps.data.User;
import java.io.IOException;
import java.util.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@SuppressWarnings("serial")
@WebServlet("/user-routes")
public class ProfileRoutes extends HttpServlet {
  private String getParameter(HttpServletRequest request, String name, String defaultValue) {
    String value = request.getParameter(name);
    if (value == null || value.isEmpty()) {
      return defaultValue;
    }
    return value;
  }

  /** Return all routes connected with the user. */
  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    UserService userService = UserServiceFactory.getUserService();

    List<Route> connectedRoutes = new ArrayList<>();
    User currentUser;

    if (userService.isUserLoggedIn()) {
      String userId = userService.getCurrentUser().getUserId();
      Key userKey = KeyFactory.createKey("User", userId);

      try {
        // Get all connected routes.
        Query query = new Query("RouteUserLink").setAncestor(userKey);
        List<Entity> results = datastore.prepare(query).asList(FetchOptions.Builder.withDefaults());

        Entity connection;
        Route newRoute;
        for (int i = 0; i < results.size(); i++) {
          connection =
              datastore.get(
                  KeyFactory.createKey("Route", (Long) results.get(i).getProperty("routeId")));
          newRoute =
              new Route(
                  0L,
                  (String) connection.getProperty("routeName"),
                  (boolean) connection.getProperty("isPublic"),
                  (Long) connection.getProperty("startHour"),
                  (Long) connection.getProperty("startMinute"));
          connectedRoutes.add(newRoute);
        }

      } catch (Exception e) {
        // TODO: return useful exception.
      }
    }
    Gson gson = new Gson();
    String json = gson.toJson(connectedRoutes);

    // Return response to the request.
    response.setContentType("application/json;");
    response.getWriter().println(json);
  }
}
