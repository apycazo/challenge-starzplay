package com.github.apycazo.starzplay.data;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Content
{
    private Long bitrate;
    private Long duration;
    private String format;
    private Long height;
    private String language;
    private Long width;
    private String id;
    private String guid;
    private List<String> assetTypeIds = null;
    private List<String> assetTypes = null;
    private String downloadUrl;
    private List<Object> releases = null;
    private String serverId;
    private String streamingUrl;
    private String protectionScheme;
}
