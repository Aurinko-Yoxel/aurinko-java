package com.yoxel.aurinko.bean;

import com.google.api.client.util.DateTime;
import com.google.api.client.util.Key;
import com.yoxel.aurinko.bean.AurIdEntity;
import com.yoxel.aurinko.bean.sub.Attachment;
import com.yoxel.aurinko.bean.sub.EmailAddress;
import com.yoxel.aurinko.bean.sub.InetHeader;
import lombok.Data;

import java.util.List;

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
    private List<String> sysLabels;

    @Key
    private List<String> sysClassifications;

    @Key
    private EmailAddress from;

    @Key
    private List<EmailAddress> to;

    @Key
    private List<EmailAddress> cc;

    @Key
    private List<EmailAddress> bcc;

    @Key
    private List<EmailAddress> replyTo;

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
    private List<InetHeader> internetHeaders;

    @Key
    private List<Attachment> attachments;

    @Key
    private List<String> omitted;

}
