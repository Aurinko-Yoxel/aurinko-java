package com.yoxel.aurinko.api;

import com.yoxel.aurinko.apis.ListSupport_TokenBased;
import com.yoxel.aurinko.bean.AurEmail;
import com.yoxel.aurinko.bean.AurEmailsPage;
import com.yoxel.aurinko.http.HttpApiSupport;
import com.yoxel.aurinko.http.HttpImpl;

/**
 * Email conversations API: /email/conversations/:id
 */
public class EmailConvo extends HttpApiSupport implements ListSupport_TokenBased<AurEmail, String, AurEmailsPage> {

    private final String convoId;

    EmailConvo(String convoId,
               HttpImpl httpImpl) {
        super(httpImpl);
        this.convoId = convoId;
    }

    @Override
    public String entityPath() {
        return "/email/conversations/" + convoId;
    }

    @Override
    public Class<AurEmailsPage> entityPageClass() {
        return AurEmailsPage.class;
    }
}
