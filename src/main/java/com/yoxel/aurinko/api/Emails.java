package com.yoxel.aurinko.api;

import com.google.api.client.googleapis.util.Utils;
import com.google.api.client.http.json.JsonHttpContent;
import com.yoxel.aurinko.apis.EntitySupport_TokenBased;
import com.yoxel.aurinko.apis.SyncSupport;
import com.yoxel.aurinko.bean.*;
import com.yoxel.aurinko.http.HttpImpl;

import java.io.IOException;

/**
 * Email API: /email/messages
 */
public class Emails extends EntitySupport_TokenBased<AurEmail, String, AurEmailsPage, AurEmail>
        implements SyncSupport<AurEmail, AurEmailsPage> {

    private final HttpImpl httpImpl;
    public final EmailFolders emailFolders;
    public final EmailDrafts emailDrafts;

    public Emails(HttpImpl httpImpl) {
        super("/email/messages", AurEmail.class, AurEmailsPage.class, AurEmail.class, httpImpl);
        this.httpImpl = httpImpl;
        this.emailFolders = new EmailFolders(httpImpl);
        this.emailDrafts = new EmailDrafts(httpImpl);
    }

    public AurContent getAttachment(String msgId, String attachmentId) throws IOException {
        return httpGet("/email/messages/" + msgId + "/attachments/" + attachmentId)
                .parseAs(AurContent.class);
    }

    @Override
    public String syncRootPath() {
        return "/email";
    }

    public String rawMessage(String id) throws IOException {
        return httpGet(entityPath() + "/" + normalizeId(id) + "/raw")
                .parseAsString();
    }

    public AurStatus updateStatus(String id, AurEmailStatus status) throws IOException {
        return httpPost(
                entityPath() + "/" + normalizeId(id) + "/status",
                new JsonHttpContent(Utils.getDefaultJsonFactory(), status)
        ).parseAs(AurStatus.class);
    }

    public AurEmailSendResponse reply(String id, AurEmail reply) throws IOException {
        return httpPost(
                entityPath() + "/" + normalizeId(id) + "/reply",
                new JsonHttpContent(Utils.getDefaultJsonFactory(), reply)
        )
                .parseAs(AurEmailSendResponse.class);
    }

    public EmailConvo conversation(String id) {
        return new EmailConvo(id, httpImpl);
    }

    public EmailTracking tracking() {
        return new EmailTracking(httpImpl);
    }
}
