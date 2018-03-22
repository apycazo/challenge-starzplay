package com.github.apycazo.starzplay.service;

import com.github.apycazo.starzplay.config.AppConfig;
import com.github.apycazo.starzplay.data.Media;
import com.github.apycazo.starzplay.data.MovieCatalog;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
public class CensorFilter implements MediaFilter
{
    private RestTemplate restTemplate;
    private AppConfig appConfig;
    private MovieCatalogClient movieCatalogClient;

    public CensorFilter(RestTemplate restTemplate, AppConfig appConfig, MovieCatalogClient movieCatalogClient)
    {
        this.restTemplate = restTemplate;
        this.appConfig = appConfig;
        this.movieCatalogClient = movieCatalogClient;
    }

    @Override
    public String name()
    {
        return "censoring";
    }

    @Override
    public MovieCatalog filter(MovieCatalog catalog, Map<String, String[]> parameters)
    {
        try {
            String level = parameters.get("level")[0];
            if (StringUtils.isEmpty(level)) {
                return catalog;
            } else {
                log.info("Catalog size: " + catalog.getEntries().size());

                catalog.getEntries().forEach(entry -> {
                    // filter only applies to Censored media
                    if ("censored".equalsIgnoreCase(entry.getPeg$contentClassification())) {
                        List<Media> medias = entry.getMedia().stream().filter(media -> {
                            boolean shouldBeAllowed;
                            switch (level.toLowerCase()) {
                                case "censored":
                                    shouldBeAllowed = isMediaCensored(media);
                                    break;
                                case "uncensored":
                                    shouldBeAllowed = !isMediaCensored(media);
                                    break;
                                default:
                                    shouldBeAllowed = true;
                            }

                            return shouldBeAllowed;
                        }).collect(Collectors.toList());

                        entry.setMedia(medias);
                    }
                });

                return catalog;
            }
        } catch (Exception e) {
            return catalog;
        }
    }

    public boolean isMediaCensored (Media media)
    {
        return media.getGuid().endsWith("C");
    }

}
