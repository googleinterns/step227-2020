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

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

import com.google.appengine.api.datastore.*;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.users.UserService;
import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;
import com.google.appengine.tools.development.testing.LocalUserServiceTestConfig;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public final class LoginServletTest {
  private LocalServiceTestHelper helper;

  @Mock HttpServletRequest request;

  @Mock HttpServletResponse response;

  @Mock UserService userService;

  @Before
  public void setUp() {
    LocalUserServiceTestConfig localUserServices = new LocalUserServiceTestConfig();
    LocalDatastoreServiceTestConfig localDatastore = new LocalDatastoreServiceTestConfig();
    helper = new LocalServiceTestHelper(localDatastore, localUserServices);
    helper.setEnvIsLoggedIn(false);
    helper.setUp();
  }

  @After
  public void tearDown() {
    helper.tearDown();
  }

  /** Tests that no user is created if user is not logged in. */
  @Test
  public void testNoUserIsCreated() throws IOException {
    when(response.getWriter()).thenReturn(new PrintWriter(System.out));
    DatastoreService ds = DatastoreServiceFactory.getDatastoreService();

    LoginServlet loginServlet = spy(new LoginServlet());
    loginServlet.doGet(request, response);

    Query userQuery = new Query("User");
    assertEquals(0, ds.prepare(userQuery).countEntities(FetchOptions.Builder.withDefaults()));
  }
}