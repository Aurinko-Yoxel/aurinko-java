package com.yoxel.aurinko.api.booking;

import com.google.api.client.googleapis.util.Utils;
import com.google.api.client.http.json.JsonHttpContent;
import com.yoxel.aurinko.apis.QueryParams;
import com.yoxel.aurinko.bean.AurBookingTimesOutDto;
import com.yoxel.aurinko.bean.AurCreateMeetingDto;
import com.yoxel.aurinko.bean.AurCreateMeetingResponse;
import com.yoxel.aurinko.http.HttpApiSupport;
import com.yoxel.aurinko.http.HttpImpl;

import java.io.IOException;

public class Availability extends HttpApiSupport {

    private final String parentBasePath;

    public Availability(HttpImpl httpImpl, String parentBasePath) {
        super(httpImpl);
        this.parentBasePath = parentBasePath;
    }

    @Override
    protected String basePath() {
        return parentBasePath;
    }

    private String entityPath() {
        return "/meeting";
    }

    public AurBookingTimesOutDto getMeetingTimes(QueryParams query, String pageToken) throws IOException {
        return httpGet(
                entityPath(),
                query.add("pageToken", pageToken)
        ).parseAs(AurBookingTimesOutDto.class);
    }

    public AurCreateMeetingResponse createMeeting(QueryParams query, AurCreateMeetingDto entity) throws IOException {
        return httpPost(
                entityPath(),
                query,
                new JsonHttpContent(Utils.getDefaultJsonFactory(), entity)
        ).parseAs(AurCreateMeetingResponse.class);
    }

}
