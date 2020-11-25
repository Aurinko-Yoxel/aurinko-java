package com.yoxel.aurinko;

import com.google.api.client.http.HttpResponseException;

import java.io.IOException;

/**
 *
 */
public class HttpErrors {

  public static boolean isHttpError(IOException e, int status) {
    return e instanceof HttpResponseException &&
           ((HttpResponseException) e).getStatusCode() == status;
  }

  public static boolean isBadRequest400(IOException e) {
    return isHttpError(e, 400);
  }

  public static boolean isForbidden403(IOException e) {
    return isHttpError(e, 403);
  }

  public static boolean isNotFound404(IOException e) {
    return isHttpError(e, 404);
  }

  public static boolean isGone410(IOException e) {
    return isHttpError(e, 410);
  }
}
