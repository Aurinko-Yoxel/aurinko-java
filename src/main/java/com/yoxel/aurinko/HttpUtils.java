package com.yoxel.aurinko;

import com.google.api.client.http.HttpResponseException;

import java.io.IOException;

/**
 *
 */
public class HttpUtils {

  public static boolean isBadRequest400(IOException e) {
    if (HttpResponseException.class.isInstance(e)) {
      if (((HttpResponseException) e).getStatusCode() == 400) {
        return true;
      }
    }

    return false;
  }

  public static boolean isForbidden403(IOException e) {
    if (HttpResponseException.class.isInstance(e)) {
      if (((HttpResponseException) e).getStatusCode() == 403) {
        return true;
      }
    }

    return false;
  }

  public static boolean isNotFound404(IOException e) {
    if (HttpResponseException.class.isInstance(e)) {
      if (((HttpResponseException) e).getStatusCode() == 404) {
        return true;
      }
    }

    return false;
  }

  public static boolean isGone410(IOException e) {
    if (HttpResponseException.class.isInstance(e)) {
      if (((HttpResponseException) e).getStatusCode() == 410) {
        return true;
      }
    }

    return false;
  }
}
