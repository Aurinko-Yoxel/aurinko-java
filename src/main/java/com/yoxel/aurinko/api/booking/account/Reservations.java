package com.yoxel.aurinko.api.booking.account;

import com.yoxel.aurinko.apis.DeleteSupport;
import com.yoxel.aurinko.bean.AurConfirmReservationOutDto;
import com.yoxel.aurinko.http.HttpApiSupport;
import com.yoxel.aurinko.http.HttpImpl;

import java.io.IOException;

public class Reservations extends HttpApiSupport implements DeleteSupport<Long> {

    private final String parentBasePath;

    public Reservations(HttpImpl httpImpl, String parentBasePath) {
        super(httpImpl);
        this.parentBasePath = parentBasePath;
    }

    @Override
    protected String basePath() {
        return parentBasePath;
    }

    @Override
    public String entityPath() {
        return "/reservations";
    }

    public AurConfirmReservationOutDto confirm(Long id) throws IOException {
        return httpPost(entityPath() + "/" + normalizeId(id) + "/confirm")
                .parseAs(AurConfirmReservationOutDto.class);
    }
}
