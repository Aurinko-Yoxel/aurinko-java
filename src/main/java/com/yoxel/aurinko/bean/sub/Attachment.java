package com.yoxel.aurinko.bean.sub;


import com.google.api.client.json.GenericJson;
import com.google.api.client.util.Key;
import lombok.Data;

@Data
public class Attachment extends GenericJson {
    @Key
    private String attachmentId;

    @Key
    private String fileName;

    @Key
    private String mimeType;

    @Key
    private int size;
}
