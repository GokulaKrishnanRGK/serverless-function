package com.neu.csye6225.cloud.functions.entity;

import java.sql.Timestamp;

public class Verify {

  private Long id;
  private String token;
  private boolean isVerified;
  private Timestamp generatedTime;
  private String username;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getToken() {
    return token;
  }

  public void setToken(String token) {
    this.token = token;
  }

  public boolean isVerified() {
    return isVerified;
  }

  public void setVerified(boolean verified) {
    isVerified = verified;
  }

  public Timestamp getGeneratedTime() {
    return generatedTime;
  }

  public void setGeneratedTime(Timestamp generatedTime) {
    this.generatedTime = generatedTime;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }
}
