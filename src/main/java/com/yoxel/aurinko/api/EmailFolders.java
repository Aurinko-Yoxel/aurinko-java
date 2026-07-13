package com.yoxel.aurinko.api;

import com.google.api.client.googleapis.util.Utils;
import com.google.api.client.http.json.JsonHttpContent;
import com.yoxel.aurinko.apis.CreateSupport;
import com.yoxel.aurinko.apis.ListSupport_TokenBased;
import com.yoxel.aurinko.apis.QueryParams;
import com.yoxel.aurinko.apis.ReadSupport;
import com.yoxel.aurinko.bean.*;
import com.yoxel.aurinko.http.HttpApiSupport;
import com.yoxel.aurinko.http.HttpImpl;
import com.yoxel.commons.xstream.XStream;

import java.io.IOException;

/**
 * Email Folder API: /email/folders
 */
public class EmailFolders extends HttpApiSupport
        implements ListSupport_TokenBased<AurEmailFolderInfo, String, AurEmailFolderInfosPage>,
        CreateSupport<AurEmailFolderInfo, String, AurEmailFolderInfo>,
        ReadSupport<AurEmailFolderInfo, String> {

    private final HttpImpl httpImpl;

    public EmailFolders(HttpImpl httpImpl) {
        super(httpImpl);
        this.httpImpl = httpImpl;
    }

    @Override
    public Class<AurEmailFolderInfosPage> entityPageClass() {
        return AurEmailFolderInfosPage.class;
    }

    @Override
    public String entityPath() {
        return "/email/folders";
    }

    @Override
    public Class<AurEmailFolderInfo> entitySaveResultClass() {
        return AurEmailFolderInfo.class;
    }

    @Override
    public Class<AurEmailFolderInfo> entityClass() {
        return AurEmailFolderInfo.class;
    }

    private String wellKnownPath() {
        return entityPath() + "/wellKnown";
    }

    public AurWellKnownFolders wellKnown() throws IOException {
        return httpGet(wellKnownPath())
                .parseAs(AurWellKnownFolders.class);
    }

    public AurWellKnownFolders updateWellKnown(AurWellKnownFolders wellKnown) throws IOException {
        return httpPatch(
                wellKnownPath(),
                new JsonHttpContent(Utils.getDefaultJsonFactory(), wellKnown)
        )
                .parseAs(AurWellKnownFolders.class);
    }

    private static class EmailMessages extends HttpApiSupport implements
            ListSupport_TokenBased<AurEmail, String, AurEmailsPage> {

        private final String folderId;

        EmailMessages(HttpImpl httpImpl, String folderId) {
            super(httpImpl);
            this.folderId = folderId;
        }

        @Override
        public Class<AurEmailsPage> entityPageClass() {
            return AurEmailsPage.class;
        }

        @Override
        public String entityPath() {
            return entityPath() + "/" + folderId + "/messages";
        }
    }

    public XStream<AurEmail, IOException> messages(String id, QueryParams params) throws IOException {
        return new EmailMessages(httpImpl, id)
                .streamPaged(params);
    }
}
