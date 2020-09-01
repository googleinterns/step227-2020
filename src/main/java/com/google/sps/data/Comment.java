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

package com.google.sps.data;

/** Represents a comment written by a user. */
public final class Comment {

  private final String commentText;
  private final String nickname;

  public Comment(String commentText, String nickname) {
    this.commentText = commentText;
    this.nickname = nickname;
  }

  public String getCommentText() {
    return commentText;
  }

  public String getNickname() {
    return nickname;
  }
}
