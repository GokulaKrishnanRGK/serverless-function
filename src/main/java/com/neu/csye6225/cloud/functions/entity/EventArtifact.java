package com.neu.csye6225.cloud.functions.entity;

public class EventArtifact {

  private String username;
  private String token;

  public EventArtifact(String username, String token) {
    this.username = username;
    this.token = token;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getToken() {
    return token;
  }

  public void setToken(String token) {
    this.token = token;
  }

  @Override
  public String toString() {
    return String.format("{Username: %s, Token: %s}", this.username, this.token);
  }
}
