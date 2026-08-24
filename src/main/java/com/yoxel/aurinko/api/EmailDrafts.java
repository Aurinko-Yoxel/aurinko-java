package com.yoxel.aurinko.api;

import com.google.api.client.googleapis.util.Utils;
import com.google.api.client.http.json.JsonHttpContent;
import com.yoxel.aurinko.apis.CreateSupport;
import com.yoxel.aurinko.apis.DeleteSupport;
import com.yoxel.aurinko.apis.QueryParams;
import com.yoxel.aurinko.apis.ReadSupport;
import com.yoxel.aurinko.bean.*;
import com.yoxel.aurinko.http.HttpApiSupport;
import com.yoxel.aurinko.http.HttpImpl;

import java.io.IOException;

public class EmailDrafts extends HttpApiSupport
        implements CreateSupport<AurEmail, String, AurEmailDraftResponse>,
        ReadSupport<AurEmailDraft, String>,
        DeleteSupport<String> {

    public EmailDrafts(HttpImpl httpImpl) {
        super(httpImpl);
    }

    @Override
    public Class<AurEmailDraft> entityClass() {
        return AurEmailDraft.class;
    }

    @Override
    public Class<AurEmailDraftResponse> entitySaveResultClass() {
        return AurEmailDraftResponse.class;
    }

    @Override
    public String entityPath() {
        return "/email/drafts";
    }

    private String entityPath(String id) {
        return entityPath() + "/" + normalizeId(id);
    }

    public AurEmailDraftResponse update(String id, AurEmail entity, QueryParams params) throws IOException {
        return httpPut(
                entityPath(id),
                params,
                new JsonHttpContent(Utils.getDefaultJsonFactory(), entity)

        ).parseAs(AurEmailDraftResponse.class);
    }

    private String sendPath(String id) {
        return entityPath(id) + "/send";
    }

    public AurEmailDraftSendResponse send(String id, QueryParams params) throws IOException {
        return httpPost(sendPath(id), params)
                .parseAs(AurEmailDraftSendResponse.class);
    }

    public AurEmailDraftSendResponse send(String id, QueryParams params, AurDraftSendDetails body) throws IOException {
        return httpPost(
                sendPath(id),
                params,
                new JsonHttpContent(Utils.getDefaultJsonFactory(), body)
        ).parseAs(AurEmailDraftSendResponse.class);
    }
}
