package com.github.apycazo.starzplay.data;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Media
{
    private String id;
    private String title;
    private String guid;
    private String ownerId;
    private Long availableDate;
    private Long expirationDate;
    private List<Object> ratings = null;
    private List<Content> content = null;
    private String restrictionId;
    private String availabilityState;
}
