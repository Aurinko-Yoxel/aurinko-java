package com.yoxel.aurinko.bean;

import com.google.api.client.util.DateTime;
import com.google.api.client.util.Key;
import com.yoxel.aurinko.bean.AurIdEntity;
import com.yoxel.aurinko.bean.sub.Attachment;
import com.yoxel.aurinko.bean.sub.EmailAddress;
import com.yoxel.aurinko.bean.sub.InetHeader;
import lombok.Data;

@Data
public class AurEmail extends AurIdEntity {
    
    @Key
    private String subject;

    @Key
    private String threadId;

    @Key
    private DateTime sentAt;

    @Key
    private DateTime receivedAt;

    @Key
    private String internetMessageId;

    @Key
    private String[] sysLabels;

    @Key
    private String[] sysClassifications;

    @Key
    private EmailAddress from;

    @Key
    private EmailAddress[] to;

    @Key
    private EmailAddress[] cc;

    @Key
    private EmailAddress[] bcc;

    @Key
    private EmailAddress[] replyTo;

    @Key
    private boolean hasAttachments;

    @Key
    private String body;

    @Key
    private String inReplyTo;

    @Key
    private String references;

    @Key
    private String threadIndex;

    @Key
    private InetHeader[] internetHeaders;

    @Key
    private Attachment[] attachments;

    @Key
    private String[] omitted;

}
