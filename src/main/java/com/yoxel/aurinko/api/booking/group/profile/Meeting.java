package com.yoxel.aurinko.api.booking.group.profile;

import com.yoxel.aurinko.apis.CreateSupport;
import com.yoxel.aurinko.apis.QueryParams;
import com.yoxel.aurinko.bean.AurBookingTimesOutDto;
import com.yoxel.aurinko.bean.AurCreateMeetingDto;
import com.yoxel.aurinko.bean.AurCreateMeetingResponse;
import com.yoxel.aurinko.http.HttpApiSupport;
import com.yoxel.aurinko.http.HttpImpl;

import java.io.IOException;

public class Meeting extends HttpApiSupport implements
        CreateSupport<AurCreateMeetingDto, Long, AurCreateMeetingResponse> {

    private final String parentBasePath;

    public Meeting(HttpImpl httpImpl, String parentBasePath) {
        super(httpImpl);
        this.parentBasePath = parentBasePath;
    }

    @Override
    protected String basePath() {
        return parentBasePath;
    }

    @Override
    public String entityPath() {
        return "/meeting";
    }

    @Override
    public Class<AurCreateMeetingResponse> entitySaveResultClass() {
        return AurCreateMeetingResponse.class;
    }

    public AurBookingTimesOutDto loadPage(QueryParams query, String pageToken) throws IOException {
        return httpGet(
                entityPath(),
                query.add("pageToken", pageToken)
        ).parseAs(AurBookingTimesOutDto.class);
    }
}
