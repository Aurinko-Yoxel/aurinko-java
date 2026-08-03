package com.yoxel.aurinko.bean;

import com.google.api.client.util.Key;
import com.yoxel.aurinko.bean.sub.AurEmailSendError;
import lombok.Data;

@Data
public class EmailDraftSendResponse extends AurIdEntity {

    @Key
    String status;

    @Key
    String submittedMessageId;

    @Key
    String threadId;

    @Key
    String trackingId;

    @Key
    String processingStatus;

    @Key
    AurEmailSendError processingError;

}
