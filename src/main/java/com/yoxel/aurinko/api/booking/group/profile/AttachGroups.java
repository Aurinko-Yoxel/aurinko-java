package com.yoxel.aurinko.api.booking.group.profile;

import com.yoxel.aurinko.apis.CreateSupport;
import com.yoxel.aurinko.apis.ListSupport_OffsetBased;
import com.yoxel.aurinko.bean.AurBookingAttachGroupsDto;
import com.yoxel.aurinko.bean.AurBookingAttachedDto;
import com.yoxel.aurinko.bean.AurStatus;
import com.yoxel.aurinko.http.HttpApiSupport;
import com.yoxel.aurinko.http.HttpImpl;

public class AttachGroups extends HttpApiSupport implements
        CreateSupport<AurBookingAttachGroupsDto, Long, AurStatus>,
        ListSupport_OffsetBased<AurBookingAttachedDto, Long, AurBookingAttachedDto.Page> {

    private final String parentBasePath;

    public AttachGroups(HttpImpl httpImpl, String parentBasePath) {
        super(httpImpl);
        this.parentBasePath = parentBasePath;
    }

    @Override
    protected String basePath() {
        return parentBasePath;
    }

    @Override
    public String entityPath() {
        return "/attachGroups";
    }

    @Override
    public Class<AurStatus> entitySaveResultClass() {
        return AurStatus.class;
    }

    @Override
    public Class<AurBookingAttachedDto.Page> entityPageClass() {
        return AurBookingAttachedDto.Page.class;
    }
}
