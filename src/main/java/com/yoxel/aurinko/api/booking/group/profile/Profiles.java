package com.yoxel.aurinko.api.booking.group.profile;

import com.google.api.client.googleapis.util.Utils;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.json.JsonHttpContent;
import com.yoxel.aurinko.apis.*;
import com.yoxel.aurinko.bean.*;
import com.yoxel.aurinko.http.HttpApiSupport;
import com.yoxel.aurinko.http.HttpImpl;

import java.io.IOException;

public class Profiles extends HttpApiSupport implements
        CreateSupport<AurBookingInDto, Long, AurBookingOutDto>,
        ReadSupport<AurBookingOutDto, Long>,
        DeleteSupport<Long>,
        ListSupport_OffsetBased<AurBookingOutDto, Long, AurBookingOutDto.Page> {

    private final String parentBasePath;
    private final HttpImpl httpImpl;

    public Profiles(HttpImpl httpImpl, String parentBasePath) {
        super(httpImpl);
        this.parentBasePath = parentBasePath;
        this.httpImpl = httpImpl;
    }

    @Override
    public String entityPath() {
        return "/profiles";
    }

    @Override
    protected String basePath() {
        return parentBasePath;
    }

    @Override
    public Class<AurBookingOutDto> entitySaveResultClass() {
        return AurBookingOutDto.class;
    }

    @Override
    public Class<AurBookingOutDto> entityClass() {
        return AurBookingOutDto.class;
    }

    @Override
    public Class<AurBookingOutDto.Page> entityPageClass() {
        return AurBookingOutDto.Page.class;
    }

    String entityPathId(Long id) {
        return entityPath() + "/" + normalizeId(id);
    }

    public AurStatus update(Long id, AurBookingUpdateDto entity, QueryParams params) throws IOException {
        final HttpRequest request = httpPatchPrepare(
                entityPathId(id),
                params,
                new JsonHttpContent(Utils.getDefaultJsonFactory(), entity)
        );

        return httpExecute(request).parseAs(AurStatus.class);
    }

    public AttachGroups attachGroups(Long id) {
        return new AttachGroups(httpImpl, entityPathId(id));
    }

    public AurStatus detachGroups(Long id, AurBookingDetachGroupsDto entity) throws IOException {
        return httpPost(
                entityPathId(id) + "/detachGroups",
                QueryParams.EMPTY,
                new JsonHttpContent(Utils.getDefaultJsonFactory(), entity)
        ).parseAs(AurStatus.class);
    }

    public AttachAccounts attachAccounts(Long id) {
        return new AttachAccounts(httpImpl, entityPathId(id));
    }

    public AurStatus detachAccounts(Long id, AurBookingDetachGroupAccountsDto entity) throws IOException {
        return httpPost(
                entityPathId(id) + "/detachAccounts",
                QueryParams.EMPTY,
                new JsonHttpContent(Utils.getDefaultJsonFactory(), entity)
        ).parseAs(AurStatus.class);
    }

    public AurBookingAvailableProfilesOutDto dynamicAvailability(QueryParams params, AurBookingAvailableProfilesInDto entity) throws IOException {
        return httpPost(
                entityPath() + "/dynamic/availability",
                params,
                new JsonHttpContent(Utils.getDefaultJsonFactory(), entity)
        )
                .parseAs(AurBookingAvailableProfilesOutDto.class);
    }

    public Meeting meeting(Long id) {
        return new Meeting(httpImpl, entityPathId(id));
    }
}

