package com.yoxel.aurinko.api;

import com.google.api.client.googleapis.util.Utils;
import com.google.api.client.http.json.JsonHttpContent;
import com.yoxel.aurinko.bean.AurFollowupConfig;
import com.yoxel.aurinko.http.HttpApiSupport;
import com.yoxel.aurinko.http.HttpImpl;

import java.io.IOException;

public class Followups extends HttpApiSupport {

    private final HttpImpl httpImpl;
    public Followups(HttpImpl httpImpl) {
        super(httpImpl);
        this.httpImpl = httpImpl;
    }

    @Override
    protected String basePath() {
        return "/followup";
    }

    public AurFollowupConfig getConfig() throws IOException {
        return httpGet("/config")
                .parseAs(AurFollowupConfig.class);
    }

    public AurFollowupConfig updateConfig(AurFollowupConfig config) throws IOException {
        return httpPut(
                "/config",
                new JsonHttpContent(Utils.getDefaultJsonFactory(), config)
        ).parseAs(AurFollowupConfig.class);
    }

    public FollowupRules rules() {
        return new FollowupRules(httpImpl);
    }

    public FollowupConversations conversations() {
        return new FollowupConversations(httpImpl);
    }
}
