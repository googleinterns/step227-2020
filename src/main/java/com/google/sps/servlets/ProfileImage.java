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
import com.google.sps.data.UserImage;
import com.google.sps.data.UserAccessType;
import java.io.IOException;
import java.util.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.File;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

import javax.servlet.ServletException;
import javax.servlet.http.Part;

@SuppressWarnings("serial")
@WebServlet("/profile-image")
public class ProfileImage extends HttpServlet {
  /** Store user's image. */
  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
    //get the file chosen by the user
    try {
      Part filePart = request.getPart("avatar");

      //get the InputStream to store the file somewhere
      InputStream fileInputStream = filePart.getInputStream();
      
      //for example, you can copy the uploaded file to the server
      //note that you probably don't want to do this in real life!
      //upload it to a file host like S3 or GCS instead
      File fileToSave = new File("WebContent/uploaded-files/" + filePart.getSubmittedFileName());
      Files.copy(fileInputStream, fileToSave.toPath(), StandardCopyOption.REPLACE_EXISTING);
      
      //get the URL of the uploaded file
      String fileUrl = "http://localhost:8080/uploaded-files/" + filePart.getSubmittedFileName();
      
      //You can get other form data too
      String name = request.getParameter("name");

      UserImage.uploadObject("theglobetrotter-step-2020", "image-1", fileUrl);
    } catch(Exception e) {
      System.out.println("While saving profile image");
      System.out.println(e);
    }

    response.sendRedirect("/profile.html");
  }
}
