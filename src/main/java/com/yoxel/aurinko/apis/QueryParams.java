package com.yoxel.aurinko.apis;

import com.google.common.collect.ImmutableList;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.stream.Collectors;

import lombok.AccessLevel;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Value;

/**
 *
 */

public interface QueryParams {

  static QueryParam qp(String name, Object value) {
    return new QueryParam(name, value);
  }

  QueryParams EMPTY = new QueryParamsList(ImmutableList.of());

  static QueryParams of(Iterable<QueryParam> params) {

    final ImmutableList.Builder<QueryParam> b = new ImmutableList.Builder<>();
    for (QueryParam param : params) {
      if (param.value != null) {
        b.add(param);
      }
    }
    final ImmutableList<QueryParam> paramsList = b.build();

    if (paramsList.isEmpty()) {
      return EMPTY;
    }
    return new QueryParamsList(paramsList);

  }

  static QueryParams of(String name, Object value) {
    return of(new QueryParam(name, value));
  }

  static QueryParams of(QueryParam... params) {
    return of(Arrays.asList(params));
  }

  @Value
  class QueryParam {

    @NonNull
    String name;
    Object value;
  }

  boolean isEmpty();

  String toString();

  default String toUrlString() {
    if (isEmpty()) {
      return "";
    } else {
      return "?" + toString();
    }
  }

  default QueryParams combine(QueryParams other) {
    if (other.isEmpty()) {
      return this;
    }

    if (this.isEmpty()) {
      return other;
    }

    return new QueryParamsCombined(this, other);
  }


  default QueryParams addAll(QueryParam... params) {
    if (params.length == 0) {
      return this;
    }

    return combine(QueryParams.of(params));
  }

  default QueryParams add(QueryParam param) {
    if (param.value == null) {
      return this;
    }
    return addAll(param);
  }

  default QueryParams add(String name, String value) {
    if (value == null) {
      return this;
    }
    return add(new QueryParam(name, value));
  }

  @RequiredArgsConstructor(access = AccessLevel.PRIVATE)
  class QueryParamsList implements QueryParams {

    private final ImmutableList<QueryParam> params;

    @Override
    public boolean isEmpty() {
      return params.isEmpty();
    }

    public String toString() {
      return params.stream()
          .map(qp -> URLEncoder.encode(qp.name, StandardCharsets.UTF_8) + "=" +
                     URLEncoder.encode(qp.value.toString(), StandardCharsets.UTF_8))
          .collect(Collectors.joining("&"));
    }
  }

  @RequiredArgsConstructor
  class QueryParamsCombined implements QueryParams {

    private final QueryParams qp1;
    private final QueryParams qp2;

    @Override
    public boolean isEmpty() {
      return false;
    }

    @Override
    public String toString() {
      return qp1.toString() + "&" + qp2.toString();
    }
  }
}
