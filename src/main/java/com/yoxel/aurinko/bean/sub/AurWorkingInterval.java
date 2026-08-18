package com.yoxel.aurinko.bean.sub;

import com.google.api.client.json.GenericJson;
import com.google.api.client.util.Key;
import lombok.Data;

import java.time.LocalTime;

@Data
public class AurWorkingInterval extends GenericJson {
    @Key
    LocalTime start;

    @Key
    LocalTime end;
}
