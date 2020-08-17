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

/** Google maps's markers's class. */
public class Marker {
  private double lat;
  private double lng;
  private Long id;

  public Marker(double newLat, double newLng, Long newId) {
    lat = newLat;
    lng = newLng;
    id = newId;
  }

  public Marker() {
    lat = 0.0;
    lng = 0.0;
    id = 0L;
  }

  public double getLat() {
    return lat;
  }

  public double getLng() {
    return lng;
  }

  public Long getId() {
    return id;
  }

  public void setLat(double newLat) {
    lat = newLat;
  }

  public void setLng(double newLng) {
    lng = newLng;
  }

  public void setId(Long newId) {
    id = newId;
  }
}
