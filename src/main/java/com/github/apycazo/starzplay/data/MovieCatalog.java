package com.github.apycazo.starzplay.data;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MovieCatalog
{
    private Object $xmlns;
    private long startIndex;
    private long itemsPerPage;
    private long entryCount;
    private String title;
    private List<MovieEntry> entries;
}
