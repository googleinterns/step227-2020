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
import com.google.sps.data.UserImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

@SuppressWarnings("serial")
public class ProfileImage extends HttpServlet {
  /** Store user's image. */
  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
    // get the file chosen by the user
    try {
      Part filePart = request.getPart("avatar");

      // get the InputStream to store the file somewhere
      InputStream fileInputStream = filePart.getInputStream();

      UserImage.uploadObject(
          "theglobetrotter-step-2020", filePart.getSubmittedFileName(), fileInputStream);
    } catch (Exception e) {
      System.out.println("Catch error while saving profile image");
      System.out.println(e);
    }

    response.sendRedirect("/profile.html");
  }
}
