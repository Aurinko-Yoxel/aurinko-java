package com.yoxel.aurinko.bean;

import com.google.api.client.util.Key;
import com.yoxel.aurinko.bean.sub.AurEmailSendError;
import lombok.Data;

@Data
public class AurEmailDraftSendResponse extends AurIdEntity {

    @Key
    String status;

    @Key
    String submittedMessageId;

    @Key
    String threadId;

    @Key
    Long trackingId;

    @Key
    String processingStatus;

    @Key
    AurEmailSendError processingError;

}
