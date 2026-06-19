package com.yoxel.aurinko.api;

import com.yoxel.aurinko.apis.EntitySupport_TokenBased;
import com.yoxel.aurinko.apis.SyncSupport;
import com.yoxel.aurinko.bean.AurContent;
import com.yoxel.aurinko.bean.AurEmail;
import com.yoxel.aurinko.bean.AurEmailsPage;
import com.yoxel.aurinko.http.HttpImpl;

import java.io.IOException;

/**
 * Email API: /email/messages
 */
public class Emails extends EntitySupport_TokenBased<AurEmail, String, AurEmailsPage, AurEmail>
        implements SyncSupport<AurEmail, AurEmailsPage> {

    private final HttpImpl httpImpl;

    public Emails(HttpImpl httpImpl) {
        super("/email/messages", AurEmail.class, AurEmailsPage.class, AurEmail.class, httpImpl);
        this.httpImpl = httpImpl;
    }

    public AurContent getAttachment(String msgId, String attachmentId) throws IOException {
        return httpGet("/email/messages/" + msgId + "/attachments/" + attachmentId)
                .parseAs(AurContent.class);
    }

    @Override
    public String syncRootPath() {
        return "/email";
    }

    public EmailConvo conversation(String id) {
        return new EmailConvo(id, httpImpl);
    }

    public EmailTracking tracking() {
        return new EmailTracking(httpImpl);
    }
}
