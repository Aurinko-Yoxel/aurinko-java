package com.yoxel.aurinko.extuser.impl;

import com.google.api.client.http.HttpResponseException;
import com.google.api.client.util.Key;

import com.yoxel.aurinko.AurinkoService;
import com.yoxel.aurinko.HttpErrors;
import com.yoxel.aurinko.apis.QueryParams;
import com.yoxel.aurinko.extuser.AurExtUser;
import com.yoxel.aurinko.extuser.AurExtUserInfoProvider;
import com.yoxel.commons.xstream.IOXStream;
import com.yoxel.commons.xstream.XStream;
import com.yoxel.persist.util.Strings;

import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Value;

import static com.yoxel.aurinko.apis.QueryParams.qp;

/**
 *
 */
@RequiredArgsConstructor
public class TeamworkUserInfoProvider implements AurExtUserInfoProvider {

  public static final AurExtUserInfoProvider.Factory FACTORY = TeamworkUserInfoProvider::new;

  private static final DateTimeFormatter paramPattern = DateTimeFormat.forPattern("yyyyMMddHHmmss");
  private final AurinkoService aurSvc;

  private AurExtUser cachedMe;

  @Override
  public AurExtUser getMyInfo() throws IOException {

    if (cachedMe == null) {
      cachedMe = loadUser("/me.json");
    }

    return cachedMe;
  }

  @Override
  public AurExtUser lookupUser(@NonNull String xid) throws IOException {
    try {
      return loadUser("/people/" + xid + ".json");
    } catch (HttpResponseException ex) {
      if (HttpErrors.isNotFound404(ex)) {
        return null;
      }
      throw ex;
    }
  }

  @Override
  public XStream<AurExtUser, IOException> loadUsers(Date modifiedSince) throws IOException {
    final var me = getMyInfo();

    return IOXStream
        .iterateUntil(
            loadUsersPage(me.getOrgId(), modifiedSince, 1),
            prev -> loadUsersPage(me.getOrgId(), modifiedSince, prev.page + 1),
            pg -> pg.page >= pg.totalPages
        )
        .flatMap(pg -> IOXStream.ofAll(pg.value))
        .map(TeamworkPerson::toAurExtUser);
  }

  private TeamworkPage<List<TeamworkPerson>> loadUsersPage(
      String companyId,
      Date modifiedSince,
      int page
  ) throws IOException {
    final var updatedAfterDate =
        modifiedSince == null ? null :
        paramPattern.print(
            new DateTime(modifiedSince)
                .withZone(DateTimeZone.UTC)
                .toLocalDateTime()
        );

    final var response = aurSvc.direct
        .httpGet(
            "/companies/" + companyId + "/people.json",
            QueryParams.of(
                qp("fullProfile", "1"),
                qp("page", page),
                qp("updatedAfterDate", updatedAfterDate)
            )
        );

    final var totalPages =
        Integer.parseInt(response.getHeaders().getFirstHeaderStringValue("X-Pages"));

    final var people = response.parseAs(TeamworkPeopleResponse.class).people;

    return new TeamworkPage<>(people, page, totalPages);
  }

  private AurExtUser loadUser(String path) throws IOException {
    return aurSvc.direct
        .httpGet(path, QueryParams.of("fullProfile", "1"))
        .parseAs(TeamworkPersonResponse.class)
        .getPerson()
        .toAurExtUser();
  }

  @Data
  public static class TeamworkPerson {

    @Key
    private String id;

    @Key("user-name")
    private String username;

    @Key("first-name")
    private String firstName;

    @Key("last-name")
    private String lastName;

    @Key("email-address")
    private String emailAddress;

    @Key("company-id")
    private String companyId;

    @Key
    private TeamworkPersonLocalization localization;

    @Key("last-changed-on")
    private String lastChangedOn;

    AurExtUser toAurExtUser() {
      final var fstName = Strings.nullEmptyCleanTrim(firstName);
      final var lstName = Strings.nullEmptyCleanTrim(lastName);

      final var fullName =
          fstName == null ? lstName
                          : lstName == null ? fstName
                                            : fstName + " " + lstName;

      return new AurExtUser(
          Strings.nullEmptyCleanTrim(id),
          Strings.nullEmptyCleanTrim(emailAddress),
          fullName,
          Strings.nullEmptyCleanTrim(companyId),
          Strings.nullEmptyCleanTrim(username),
          Strings.nullEmptyCleanTrim(localization.timezoneJavaRefCode),
          StringUtils.isBlank(lastChangedOn) ? null
                                             : new Timestamp(DateTime.parse(lastChangedOn).getMillis())
      );
    }
  }

  @Data
  public static class TeamworkPersonLocalization {

    @Key
    private String timezoneJavaRefCode;
  }

  @Data
  public static class TeamworkPersonResponse {

    @Key("person")
    private TeamworkPerson person;

  }

  @Data
  public static class TeamworkPeopleResponse {

    @Key("people")
    private List<TeamworkPerson> people;
  }

  @Value
  public static class TeamworkPage<T> {

    T value;
    int page;
    int totalPages;
  }
}
