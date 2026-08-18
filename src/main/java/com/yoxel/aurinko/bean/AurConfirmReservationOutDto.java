package com.yoxel.aurinko.bean;

import com.google.api.client.json.GenericJson;
import com.google.api.client.util.Key;
import lombok.Data;

@Data
public class AurConfirmReservationOutDto extends GenericJson {
    @Key
    String id;

    @Key
    String rescheduleToken;
}
