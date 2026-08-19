package com.yoxel.aurinko.api.booking.account;

import com.yoxel.aurinko.api.booking.account.profile.Profiles;
import com.yoxel.aurinko.bean.AurWeekWorkScheduleResponse;
import com.yoxel.aurinko.http.HttpApiSupport;
import com.yoxel.aurinko.http.HttpImpl;

import java.io.IOException;

public class Account extends HttpApiSupport {

    private final String parentBasePath;
    public final Profiles profiles;
    public final Reservations reservations;

    public Account(HttpImpl httpImpl, String parentBasePath) {
        super(httpImpl);
        this.parentBasePath = parentBasePath;
        this.profiles = new Profiles(httpImpl, basePath());
        this.reservations = new Reservations(httpImpl, basePath());
    }

    @Override
    protected String basePath() {
        return parentBasePath + "/account";
    }

    public AurWeekWorkScheduleResponse workHours() throws IOException {
        return httpGet("/workHours")
                .parseAs(AurWeekWorkScheduleResponse.class);
    }
}
