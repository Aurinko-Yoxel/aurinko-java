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
import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import static com.yoxel.aurinko.apis.QueryParams.qp;

/**
 *
 */
@RequiredArgsConstructor
public class TeamworkUserInfoProvider implements AurExtUserInfoProvider {

  public static final AurExtUserInfoProvider.Factory FACTORY = TeamworkUserInfoProvider::new;

  private static final DateTimeFormatter paramPattern = DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ss'Z'");

  private static final int PAGE_SIZE = 50;

  private final AurinkoService aurSvc;

  // for now we don't use it because Teamwork sends us wrong values in /signup methods on staging syncs.
  private final String wrongExtOrgId;
  private final String extUserId;

  private AurExtUser cachedMe;

  @Override
  public AurExtUser getMyInfo() throws IOException {

    if (cachedMe == null) {
      cachedMe = lookupUser(extUserId);
    }

    return cachedMe;
  }

  @Override
  public AurExtUser lookupUser(@NonNull String xid) throws IOException {
    try {
      return aurSvc.direct
          .httpGet("/projects/api/v3/people/" + extUserId + ".json")
          .parseAs(TeamworkPersonResponse.class)
          .getPerson()
          .toAurExtUser();
    } catch (HttpResponseException ex) {
//      if (HttpErrors.isNotFound404(ex)) {
//        return null;
//      }

      throw ex;
    }
  }

  @Override
  public XStream<AurExtUser, IOException> loadUsers(Date modifiedSince) throws IOException {
    final var updatedAfterDate = modifiedSince == null ? null :
        paramPattern.print(new DateTime(modifiedSince).withZone(DateTimeZone.UTC));

    final var me = getMyInfo();

    return IOXStream
        .iterateUntil(
            loadUsersPage(updatedAfterDate, me.getOrgId(), 1),
            // pageOffset = requested "page" - 1, so next page = pageOffset + 2
            prev -> loadUsersPage(updatedAfterDate, me.getOrgId(), prev.meta.page.pageOffset + 2),
            pg -> !pg.meta.page.hasMore
        )
        .flatMap(pg -> IOXStream.ofAll(pg.people))
        .map(TeamworkPerson::toAurExtUser);
  }

  private TeamworkPeopleResponse loadUsersPage(String updatedAfterStr, String extOrgId, int page) throws IOException {

    return aurSvc.direct
        .httpGet(
            "/projects/api/v3/people.json",
            QueryParams.of(
                qp("companyIds", extOrgId),
                qp("page", page),
                qp("pageSize", TeamworkUserInfoProvider.PAGE_SIZE),
                qp("updatedAfter", updatedAfterStr)
            )
        )
        .parseAs(TeamworkPeopleResponse.class);
  }

  @Data
  public static class TeamworkPerson {

    @Key
    private Long id;

    @Key
    private String firstName;

    @Key
    private String lastName;

    @Key
    private String email;

    @Key
    private Long companyId;

    @Key
    private String timezone;

    AurExtUser toAurExtUser() {
      final var fstName = Strings.nullEmptyCleanTrim(firstName);
      final var lstName = Strings.nullEmptyCleanTrim(lastName);

      final var fullName =
          fstName == null ? lstName
              : lstName == null ? fstName
              : fstName + " " + lstName;

      return new AurExtUser(
          Strings.nullEmptyCleanTrim(id.toString()),
          Strings.nullEmptyCleanTrim(email),
          fullName,
          Strings.nullEmptyCleanTrim(companyId.toString()),
          Strings.nullEmptyCleanTrim(email),
          null, // we don't know yet what will come from teamwork, so leaving it null for now
          null
      );
    }
  }

  @Data
  public static class TeamworkPersonResponse {

    @Key
    private TeamworkPerson person;
  }

  @Data
  public static class TeamworkPeopleResponse {

    @Key
    private List<TeamworkPerson> people;

    @Key
    private TeamworkResponseMeta meta;
  }

  @Data
  public static class TeamworkResponseMeta {
    @Key
    public TeamworkPageInfo page;
  }

  @Data
  public static class TeamworkPageInfo {
    @Key
    private int pageOffset;

    @Key
    private int pageSize;

    @Key
    private int count;

    @Key
    private boolean hasMore;
  }
}
