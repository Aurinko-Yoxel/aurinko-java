package com.yoxel.aurinko.bean;

import com.google.api.client.util.Key;
import com.yoxel.aurinko.bean.sub.OccurrenceInfo;
import com.yoxel.aurinko.bean.sub.Recurrence;
import lombok.Data;

import java.util.List;

@Data
public class AurSeriesInfo extends AurIdEntity {

    @Key
    String globalId;

    @Key
    Recurrence recurrence;

    @Key
    List<OccurrenceInfo> modifiedOccurrences;

    @Key
    List<OccurrenceInfo> deletedOccurrences;
}
