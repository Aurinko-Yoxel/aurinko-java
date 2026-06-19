package com.yoxel.aurinko.api;

import com.yoxel.aurinko.apis.EntitySupport_TokenBased;
import com.yoxel.aurinko.bean.AurDrive;
import com.yoxel.aurinko.bean.AurDrivesPage;
import com.yoxel.aurinko.http.HttpImpl;

/**
 * Drive API: /drives
 */
public class Drives extends EntitySupport_TokenBased<AurDrive, String, AurDrivesPage, AurDrive> {

    private final HttpImpl httpImpl;

    public Drives(HttpImpl httpImpl) {
        super("/drive", AurDrive.class, AurDrivesPage.class, AurDrive.class, httpImpl);
        this.httpImpl = httpImpl;
    }

    @Override
    public String normalizeId(String id) {
        return id == null ? "default" : id;
    }

    public DriveItems driveEntries(String driveId) {
        return new DriveItems(normalizeId(driveId), httpImpl);
    }
}
