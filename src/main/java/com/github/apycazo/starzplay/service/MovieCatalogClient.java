package com.github.apycazo.starzplay.service;

import com.github.apycazo.starzplay.data.MovieCatalog;
import feign.Headers;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(value = "MovieCatalogClient", url = "${app.catalogURL}")
@ConditionalOnProperty(name = "app.client.catalog.enabled", matchIfMissing = true, havingValue = "true")
public interface MovieCatalogClient
{
    @RequestMapping(value = "", method = RequestMethod.GET)
    @Headers("Accept: application/json") // only to show how to send headers
    MovieCatalog getCatalog();
}
