package com.yoxel.aurinko.bean.sub;

import com.google.api.client.json.GenericJson;
import com.google.api.client.util.Key;
import lombok.Data;

import java.util.List;

@Data
public class AurAvailabilityIntervals extends GenericJson {
    @Key
    List<AurAvailabilityInterval> intervals;
}
