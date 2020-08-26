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
import java.io.IOException;
import java.util.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@SuppressWarnings("serial")
@WebServlet("/user-routes")
public class ProfileRoutes extends HttpServlet {
  static class RouteWithType {
    Route route;
    Long type;

    public RouteWithType(Route route, Long type) {
      this.route = route;
      this.type = type;
    }
  }

  /** Return all routes connected with the user. */
  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    UserService userService = UserServiceFactory.getUserService();

    List<RouteWithType> connectedRoutes = new ArrayList<>();

    if (userService.isUserLoggedIn()) {
      String userId = userService.getCurrentUser().getUserId();
      Key userKey = KeyFactory.createKey("User", userId);

      try {
        // Get all connected routes.
        Query query = new Query("RouteUserLink").setAncestor(userKey);
        List<Entity> results = datastore.prepare(query).asList(FetchOptions.Builder.withDefaults());

        List<Key> routesKeys = new ArrayList<Key>();
        for (int i = 0; i < results.size(); i++) {
          routesKeys.add(
              KeyFactory.createKey("Route", (Long) results.get(i).getProperty("routeId")));
        }

        Map<Key, Entity> routesList = datastore.get(routesKeys);

        Route newRoute;
        RouteWithType newRouteWithType;
        int i = 0;
        for (Entity connection : routesList.values()) {
          newRoute =
              new Route(
                  connection.getKey().getId(),
                  (String) connection.getProperty("routeName"),
                  (boolean) connection.getProperty("isPublic"),
                  (boolean) connection.getProperty("isCompleted"),
                  (Long) connection.getProperty("startHour"),
                  (Long) connection.getProperty("startMinute"),
                  (double) connection.getProperty("rating"));
          newRouteWithType = new RouteWithType(newRoute, (Long) results.get(i).getProperty("type"));
          connectedRoutes.add(newRouteWithType);
          i++;
        }

      } catch (Exception e) {
        // TODO(#14): Catch more specific exceptions.
      }
    }
    Gson gson = new Gson();
    String json = gson.toJson(connectedRoutes);

    // Return response to the request.
    response.setContentType("application/json;");
    response.getWriter().println(json);
  }
}
