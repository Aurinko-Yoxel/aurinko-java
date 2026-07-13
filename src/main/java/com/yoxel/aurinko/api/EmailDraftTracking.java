package com.yoxel.aurinko.api;

import com.google.api.client.googleapis.util.Utils;
import com.google.api.client.http.json.JsonHttpContent;
import com.yoxel.aurinko.apis.QueryParams;
import com.yoxel.aurinko.apis.UpdateSupport;
import com.yoxel.aurinko.bean.*;
import com.yoxel.aurinko.http.HttpApiSupport;
import com.yoxel.aurinko.http.HttpImpl;

import java.io.IOException;

public class EmailDraftTracking extends HttpApiSupport
    implements UpdateSupport<AurEmailDraftTrackingUpdate, String, AurStatus> {

    public EmailDraftTracking(HttpImpl httpImpl) {
        super(httpImpl);
    }

    @Override
    public Class<AurStatus> entitySaveResultClass() {
        return AurStatus.class;
    }

    @Override
    public String entityPath() {
        return "/email/draftTracking";
    }

    public AurEmailDraftCreateTrackingResponse create(QueryParams params, AurRewriteHtml rewrite) throws IOException {
        return httpPost(
                entityPath() + "/create",
                params,
                new JsonHttpContent(Utils.getDefaultJsonFactory(), rewrite)
        ).parseAs(AurEmailDraftCreateTrackingResponse.class);
    }

    public AurStatus markSent(String id, AurConvertDraft convert) throws IOException {
        return httpPatch(
                entityPath() + "/" + id + "/convert",
                new JsonHttpContent(Utils.getDefaultJsonFactory(), convert)
        ).parseAs(AurStatus.class);
    }
}
