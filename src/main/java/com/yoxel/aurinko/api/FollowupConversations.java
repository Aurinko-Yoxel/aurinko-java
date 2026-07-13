package com.yoxel.aurinko.api;

import com.google.api.client.googleapis.util.Utils;
import com.google.api.client.http.json.JsonHttpContent;
import com.yoxel.aurinko.apis.ListSupport_OffsetBased;
import com.yoxel.aurinko.apis.ReadSupport;
import com.yoxel.aurinko.bean.AurConvoDripStatus;
import com.yoxel.aurinko.bean.AurDripRequest;
import com.yoxel.aurinko.http.HttpApiSupport;
import com.yoxel.aurinko.http.HttpImpl;

import java.io.IOException;

public class FollowupConversations extends HttpApiSupport
    implements ListSupport_OffsetBased<AurConvoDripStatus, String, AurConvoDripStatus.Page>,
        ReadSupport<AurConvoDripStatus, String> {
    public FollowupConversations(HttpImpl httpImpl) {
        super(httpImpl);
    }

    @Override
    public Class<AurConvoDripStatus.Page> entityPageClass() {
        return AurConvoDripStatus.Page.class;
    }

    @Override
    public Class<AurConvoDripStatus> entityClass() {
        return AurConvoDripStatus.class;
    }

    @Override
    public String entityPath() {
        return "/followup/conversations";
    }

    public AurConvoDripStatus update(String id, AurDripRequest status) throws IOException {
        return httpPut(
                entityPath() + "/" + normalizeId(id),
                new JsonHttpContent(Utils.getDefaultJsonFactory(), status)
        ).parseAs(AurConvoDripStatus.class);
    }
}
