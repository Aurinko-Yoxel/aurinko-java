package com.yoxel.aurinko;

import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpResponseException;

import com.yoxel.aurinko.bean.AurHttpError;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;

@Getter
public class AurinkoHttpException extends HttpResponseException {

  private static final int MAX_DETAIL_LEN = 200;

  private final String requestMethod;
  private final String requestPath;
  private final String errorCode;
  private final String requestId;

  private AurinkoHttpException(Builder builder, String requestMethod, String requestPath,
                              AurHttpError error) {
    super(builder);

    this.requestMethod = requestMethod;
    this.requestPath = requestPath;
    this.errorCode = error == null ? null : error.detailCode();
    this.requestId = error == null ? null : error.getRequestId();
  }

  public static AurinkoHttpException of(HttpRequest request, HttpResponseException ex) {
    final String method = request.getRequestMethod();
    final String path = request.getUrl() == null ? null : request.getUrl().getRawPath();
    final AurHttpError error = AurHttpError.fromException(ex);

    final String summary = summarize(method, path, ex, error);
    final String content = ex.getContent();

    final Builder builder = new Builder(ex.getStatusCode(), ex.getStatusMessage(), ex.getHeaders())
        .setContent(content)
        .setMessage(StringUtils.isBlank(content) ? summary : summary + "\n" + content);

    final AurinkoHttpException enriched =
        new AurinkoHttpException(builder, method, path, error);

    enriched.setStackTrace(ex.getStackTrace());

    return enriched;
  }

  private static String summarize(String method, String path, HttpResponseException ex,
                                  AurHttpError error) {
    final StringBuilder sb = new StringBuilder(256);

    sb.append(ex.getStatusCode());
    if (StringUtils.isNotBlank(ex.getStatusMessage())) {
      sb.append(' ').append(ex.getStatusMessage());
    }

    if (method != null || path != null) {
      sb.append(" on ").append(StringUtils.trimToEmpty(method));
      if (path != null) {
        sb.append(method == null ? "" : " ").append(path);
      }
    }

    if (error == null) {
      return sb.toString();
    }

    final String detail = StringUtils.abbreviate(
        StringUtils.normalizeSpace(error.detailMessage()), MAX_DETAIL_LEN);
    if (StringUtils.isNotBlank(detail)) {
      sb.append(" - ").append(detail);
    }

    final List<String> tags = new ArrayList<>(2);
    if (StringUtils.isNotBlank(error.detailCode())) {
      tags.add(error.detailCode());
    }

    if (StringUtils.isNotBlank(error.getRequestId())) {
      tags.add("request " + error.getRequestId());
    }

    if (!tags.isEmpty()) {
      sb.append(" [").append(String.join(", ", tags)).append(']');
    }

    return sb.toString();
  }
}
