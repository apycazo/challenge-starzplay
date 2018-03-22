package com.github.apycazo.starzplay.service;

import com.github.apycazo.starzplay.config.AppConfig;
import com.github.apycazo.starzplay.data.MovieCatalog;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
public class FilterRestController
{
    private AppConfig appConfig;
    private MovieCatalogClient client;
    private Map<String, MediaFilter> mediaFilters;

    public FilterRestController(
            AppConfig appConfig,
            MovieCatalogClient client,
            List<MediaFilter> filters)
    {
        this.appConfig = appConfig;
        this.client = client;
        this.mediaFilters = new HashMap<>();
        filters.forEach(filter -> mediaFilters.put(filter.name(), filter));
    }

    public void setMovieCatalogClient (MovieCatalogClient client)
    {
        this.client = client;
    }

    @GetMapping("${app.mapping}")
    public MovieCatalog getFilteredResults (@RequestParam String filter, HttpServletRequest httpServletRequest)
    {
        if (!appConfig.getAllowedFilters().contains(filter) || !mediaFilters.containsKey(filter)) {
            throw new IllegalArgumentException("Filter '" + filter + "' is not available or invalid");
        } else {
            MovieCatalog catalog = client.getCatalog();
            return mediaFilters.get(filter).filter(catalog, httpServletRequest.getParameterMap());
        }
    }
}
