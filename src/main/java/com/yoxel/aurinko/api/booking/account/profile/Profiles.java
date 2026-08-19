package com.yoxel.aurinko.api.booking.account.profile;

import com.google.api.client.googleapis.util.Utils;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.json.JsonHttpContent;
import com.yoxel.aurinko.apis.*;
import com.yoxel.aurinko.bean.AurBookingInDto;
import com.yoxel.aurinko.bean.AurBookingOutDto;
import com.yoxel.aurinko.bean.AurBookingUpdateDto;
import com.yoxel.aurinko.bean.AurStatus;
import com.yoxel.aurinko.http.HttpApiSupport;
import com.yoxel.aurinko.http.HttpImpl;

import java.io.IOException;

public class Profiles extends HttpApiSupport implements
        CreateSupport<AurBookingInDto, Long, AurBookingOutDto>,
        ReadSupport<AurBookingOutDto, Long>,
        DeleteSupport<Long>,
        ListSupport_OffsetBased<AurBookingOutDto, Long, AurBookingOutDto.Page> {

    private final String parentBasePath;
    public final HttpImpl httpImpl;

    public Profiles(HttpImpl httpImpl, String parentBasePath) {
        super(httpImpl);
        this.parentBasePath = parentBasePath;
        this.httpImpl = httpImpl;
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

    @Override
    public String entityPath() {
        return "/profiles";
    }

    public AurStatus update(Long id, AurBookingUpdateDto entity, QueryParams params) throws IOException {
        final HttpRequest request = httpPatchPrepare(
                entityPath() + "/" + normalizeId(id),
                params,
                new JsonHttpContent(Utils.getDefaultJsonFactory(), entity)
        );

        return httpExecute(request).parseAs(AurStatus.class);
    }

    public Meeting meeting(Long profileId) {
        return new Meeting(httpImpl, basePath() + entityPath() + "/" + normalizeId(profileId));
    }
}
