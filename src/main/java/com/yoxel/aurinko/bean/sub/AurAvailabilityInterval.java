package com.yoxel.aurinko.bean.sub;

import com.google.api.client.json.GenericJson;
import com.google.api.client.util.Key;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class AurAvailabilityInterval extends GenericJson {
    @Key
    LocalDate dateStartInclusive;

    @Key
    LocalDate dateEndInclusive;

    @Key
    List<AurWorkingInterval> hours;
}
