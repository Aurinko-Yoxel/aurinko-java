package com.yoxel.aurinko.api;

import com.yoxel.aurinko.apis.EntitySupport_TokenBased;
import com.yoxel.aurinko.apis.SyncSupport;
import com.yoxel.aurinko.bean.*;
import com.yoxel.aurinko.http.HttpImpl;
import com.yoxel.commons.xstream.XStream;

import java.io.IOException;

/**
 * Drive API: /drives/:id/items
 */
public class DriveItems
        extends EntitySupport_TokenBased<AurDriveItem, String, AurDriveItemsPage, AurDriveItemSaveResult>
        implements SyncSupport<AurDriveItem, AurDriveItemsPage> {

    private final String driveId;
    private final HttpImpl httpImpl;

    public DriveItems(String driveId, HttpImpl httpImpl) {
        this(driveId, "", httpImpl);
    }

    private DriveItems(String driveId, String postfix, HttpImpl httpImpl) {
        super("/drives/" + driveId + "/items" + postfix,
                AurDriveItem.class, AurDriveItemsPage.class, AurDriveItemSaveResult.class, httpImpl);
        this.driveId = driveId;;
        this.httpImpl = httpImpl;
    }

    @Override
    public String syncRootPath() {
        return "/drives/" + driveId;
    }

    public XStream<AurDriveItem, IOException> streamDriveItems() throws IOException {

        return new DriveItems(driveId, "", httpImpl).streamPaged();
    }
}
