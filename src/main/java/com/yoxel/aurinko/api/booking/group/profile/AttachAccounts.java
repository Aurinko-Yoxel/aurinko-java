package com.yoxel.aurinko.api.booking.group.profile;

import com.yoxel.aurinko.apis.CreateSupport;
import com.yoxel.aurinko.apis.ListSupport_OffsetBased;
import com.yoxel.aurinko.bean.AurBookingAttachAccountsDto;
import com.yoxel.aurinko.bean.AurBookingAttachedAccountsDto;
import com.yoxel.aurinko.bean.AurStatus;
import com.yoxel.aurinko.http.HttpApiSupport;
import com.yoxel.aurinko.http.HttpImpl;

public class AttachAccounts extends HttpApiSupport implements
        CreateSupport<AurBookingAttachAccountsDto, Long, AurStatus>,
        ListSupport_OffsetBased<AurBookingAttachedAccountsDto, Long, AurBookingAttachedAccountsDto.Page> {

    private final String parentBasePath;

    public AttachAccounts(HttpImpl httpImpl,
                          String parentBasePath) {
        super(httpImpl);
        this.parentBasePath = parentBasePath;
    }

    @Override
    protected String basePath() {
        return parentBasePath;
    }

    @Override
    public String entityPath() {
        return "/attachAccounts";
    }

    @Override
    public Class<AurStatus> entitySaveResultClass() {
        return AurStatus.class;
    }

    @Override
    public Class<AurBookingAttachedAccountsDto.Page> entityPageClass() {
        return AurBookingAttachedAccountsDto.Page.class;
    }
}
