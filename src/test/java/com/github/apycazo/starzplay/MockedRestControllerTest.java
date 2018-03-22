package com.github.apycazo.starzplay;

import com.github.apycazo.starzplay.config.AppConfig;
import com.github.apycazo.starzplay.data.MovieCatalog;
import com.github.apycazo.starzplay.service.CensorFilter;
import com.github.apycazo.starzplay.service.FilterRestController;
import com.github.apycazo.starzplay.service.MovieCatalogClient;
import com.github.apycazo.starzplay.utils.ExplorationResult;
import com.github.apycazo.starzplay.utils.TestUtils;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class MockedRestControllerTest
{
    @Mock
    private MovieCatalogClient movieCatalogClient;

    @Autowired
    private TestRestTemplate testRestTemplate;
    @Autowired
    private AppConfig appConfig;
    @Autowired
    private FilterRestController filterRestController;
    @Autowired
    private CensorFilter censorFilter;

    private static TestUtils testUtils = new TestUtils();

    @Before
    public void init () throws Exception {

        log.info("Init");

        MovieCatalog defaultCatalog = testUtils.parseFile(TestUtils.FILE_BASIC);
        Mockito.doReturn(defaultCatalog).when(movieCatalogClient).getCatalog();
        filterRestController.setMovieCatalogClient(movieCatalogClient);
    }

    /**
     * Explores the catalog recording its findings.
     * @param movieCatalog
     * @return the results of the exploration
     */
    private ExplorationResult explore (MovieCatalog movieCatalog)
    {
        ExplorationResult result = new ExplorationResult();
        movieCatalog.getEntries().forEach(entry ->
                entry.getMedia().forEach(media -> {
                    if (TestUtils.UNCENSORED_MEDIA_GUID.equalsIgnoreCase(media.getGuid())) {
                        result.containsGui31MFR.set(true);
                    } else if (TestUtils.CENSORED_MEDIA_GUID.equalsIgnoreCase(media.getGuid())) {
                        result.containsGui31C.set(true);
                    }
                    if (censorFilter.isMediaCensored(media)) {
                        result.censoredMediaCount.incrementAndGet();
                    } else {
                        result.uncensoredMediaCount.incrementAndGet();
                    }
                })
        );

        return result;
    }

    @Test
    public void test_with_bad_filter_value ()
    {
        URI uri = UriComponentsBuilder
                .fromUriString(appConfig.getMapping())
                .queryParam("filter", "bad-filter-name")
                .build()
                .toUri();

        ResponseEntity response = testRestTemplate.getForEntity(uri, String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    public void test_without_filter_param ()
    {
        ResponseEntity response = testRestTemplate.getForEntity(appConfig.getMapping(), MovieCatalog.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    public void test_with_filter_but_without_level ()
    {
        URI uri = UriComponentsBuilder
                .fromUriString(appConfig.getMapping())
                .queryParam("filter", censorFilter.name())
                .build()
                .toUri();

        MovieCatalog catalog = testRestTemplate.getForObject(uri, MovieCatalog.class);

        ExplorationResult result = explore(catalog);
        assertThat(result.uncensoredMediaCount.get()).isEqualTo(4);
        assertThat(result.censoredMediaCount.get()).isEqualTo(1);
        assertThat(result.containsGui31MFR.get()).isTrue();
        assertThat(result.containsGui31C.get()).isTrue();
    }

    @Test
    public void test_censored ()
    {
        URI uri = UriComponentsBuilder
                .fromUriString(appConfig.getMapping())
                .queryParam("filter", "censoring")
                .queryParam("level", "censored")
                .build()
                .toUri();

        MovieCatalog catalog = testRestTemplate.getForObject(uri, MovieCatalog.class);

        ExplorationResult result = explore(catalog);
        assertThat(result.uncensoredMediaCount.get()).isEqualTo(3);
        assertThat(result.censoredMediaCount.get()).isEqualTo(1);
        assertThat(result.containsGui31MFR.get()).isFalse();
        assertThat(result.containsGui31C.get()).isTrue();
    }

    @Test
    public void test_uncensored ()
    {
        URI uri = UriComponentsBuilder
                .fromUriString(appConfig.getMapping())
                .queryParam("filter", "censoring")
                .queryParam("level", "uncensored")
                .build()
                .toUri();

        MovieCatalog catalog = testRestTemplate.getForObject(uri, MovieCatalog.class);

        ExplorationResult result = explore(catalog);
        assertThat(result.uncensoredMediaCount.get()).isEqualTo(4);
        assertThat(result.censoredMediaCount.get()).isEqualTo(0);
        assertThat(result.containsGui31MFR.get()).isTrue();
        assertThat(result.containsGui31C.get()).isFalse();
    }
}
