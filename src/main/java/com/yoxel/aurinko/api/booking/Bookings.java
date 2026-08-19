package com.yoxel.aurinko.api.booking;

import com.yoxel.aurinko.api.booking.account.Account;
import com.yoxel.aurinko.api.booking.group.Group;
import com.yoxel.aurinko.http.HttpImpl;

public class Bookings {

    private final String basePath = "/book";
    private final HttpImpl httpImpl;
    public final Account account;
    public final Group group;

    public Bookings(HttpImpl httpImpl) {
        this.httpImpl = httpImpl;
        this.account = new Account(httpImpl, basePath);
        this.group = new Group(httpImpl, basePath);
    }

    public Availability availability(String aurinkoClientId, String name) {
        return new Availability(httpImpl, basePath + "/" + aurinkoClientId + "/" + name);
    }
}
