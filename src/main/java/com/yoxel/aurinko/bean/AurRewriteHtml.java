package com.yoxel.aurinko.bean;

import com.google.api.client.json.GenericJson;
import com.google.api.client.util.Key;
import com.yoxel.aurinko.bean.sub.AurOutEmailTrack;
import lombok.Data;

@Data
public class AurRewriteHtml extends GenericJson {

    @Key
    String htmlText;

    @Key
    AurOutEmailTrack tracking;
}
