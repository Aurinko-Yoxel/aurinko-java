package com.yoxel.aurinko.bean.sub;

import com.google.api.client.json.GenericJson;
import com.google.api.client.util.Key;
import lombok.Data;

import java.util.List;

@Data
public class AurEmailSendError extends GenericJson {

    @Key
    List<String> failedSteps;

    @Key
    String errorMessage;
}
