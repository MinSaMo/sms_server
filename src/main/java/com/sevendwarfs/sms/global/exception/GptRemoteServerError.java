package com.sevendwarfs.sms.global.exception;

public class GptRemoteServerError extends RuntimeException{

  public static GptRemoteServerError NOT_STABLE = new GptRemoteServerError("GPT API server is not stable");

  public GptRemoteServerError(String message) {
    super(message);
  }
}
