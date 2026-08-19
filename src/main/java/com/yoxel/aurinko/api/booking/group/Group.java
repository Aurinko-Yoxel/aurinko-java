package com.yoxel.aurinko.api.booking.group;

import com.yoxel.aurinko.api.booking.group.profile.Profiles;
import com.yoxel.aurinko.http.HttpImpl;

public class Group {

    private final String parentBasePath;
    public final Profiles profiles;
    public final Reservations reservations;

    public Group(HttpImpl httpImpl, String parentBasePath) {
        this.parentBasePath = parentBasePath;
        String basePath = parentBasePath + "/group";
        this.profiles = new Profiles(httpImpl, basePath);
        this.reservations = new Reservations(httpImpl, basePath);
    }
}
