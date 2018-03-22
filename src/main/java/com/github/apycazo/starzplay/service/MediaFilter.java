package com.github.apycazo.starzplay.service;

import com.github.apycazo.starzplay.data.MovieCatalog;

import java.util.Map;

public interface MediaFilter
{
    String name();
    MovieCatalog filter (MovieCatalog catalog, Map<String, String[]> parameters);
}
