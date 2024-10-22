package com.yoxel.aurinko.bean.sub;


import com.google.api.client.json.GenericJson;
import com.google.api.client.util.Key;
import lombok.Data;

@Data
public class Attachment extends GenericJson {
    @Key
    private String id;

    @Key
    private boolean inline;

    @Key
    private String name;

    @Key
    private int size;

    @Key
    private String mimeType;

    @Key
    private String contentId;

    @Key
    private String content;
}
