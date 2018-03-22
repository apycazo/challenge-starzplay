package com.github.apycazo.starzplay;

import com.github.apycazo.starzplay.data.MovieCatalog;
import com.github.apycazo.starzplay.service.CensorFilter;
import com.github.apycazo.starzplay.utils.ExplorationResult;
import com.github.apycazo.starzplay.utils.TestUtils;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
public class ServiceTest
{


    @Autowired
    private CensorFilter censorFilter;
    private TestUtils testUtils = new TestUtils();

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

    /**
     * Ensures the initial content matches the expected
     * @throws Exception
     */
    @Test
    public void test_initial_content () throws Exception
    {
        MovieCatalog catalog = testUtils.parseFile(TestUtils.FILE_BASIC);

        ExplorationResult result = explore(catalog);

        assertThat(result.uncensoredMediaCount.get()).isEqualTo(4);
        assertThat(result.censoredMediaCount.get()).isEqualTo(1);
        assertThat(result.containsGui31MFR.get()).isTrue();
        assertThat(result.containsGui31C.get()).isTrue();
    }

    /**
     * If level is not found, then the filter should do nothing
     * @throws Exception
     */
    @Test
    public void test_filter_without_defined_level () throws Exception
    {
        MovieCatalog catalog = testUtils.parseFile(TestUtils.FILE_BASIC);

        censorFilter.filter(catalog, Collections.emptyMap());
        ExplorationResult result = explore(catalog);

        assertThat(result.uncensoredMediaCount.get()).isEqualTo(4);
        assertThat(result.censoredMediaCount.get()).isEqualTo(1);
        assertThat(result.containsGui31MFR.get()).isTrue();
        assertThat(result.containsGui31C.get()).isTrue();
    }

    /**
     * If level is an empty string or null then the filter should do nothing
     * @throws Exception
     */
    @Test
    public void test_filter_with_empty_string_level () throws Exception
    {
        MovieCatalog catalog = testUtils.parseFile(TestUtils.FILE_BASIC);

        Map<String, String []> params = new HashMap<>();
        params.put("level", new String[] {});
        censorFilter.filter(catalog, params);

        ExplorationResult result = explore(catalog);

        assertThat(result.uncensoredMediaCount.get()).isEqualTo(4);
        assertThat(result.censoredMediaCount.get()).isEqualTo(1);
        assertThat(result.containsGui31MFR.get()).isTrue();
        assertThat(result.containsGui31C.get()).isTrue();
    }

    /**
     * If level is an empty string or null then the filter should do nothing
     * @throws Exception
     */
    @Test
    public void test_filter_with_null_string_level () throws Exception
    {
        MovieCatalog catalog = testUtils.parseFile(TestUtils.FILE_BASIC);

        Map<String, String []> params = new HashMap<>();
        params.put("level", new String[] { null });
        censorFilter.filter(catalog, params);

        ExplorationResult result = explore(catalog);

        assertThat(result.uncensoredMediaCount.get()).isEqualTo(4);
        assertThat(result.censoredMediaCount.get()).isEqualTo(1);
        assertThat(result.containsGui31MFR.get()).isTrue();
        assertThat(result.containsGui31C.get()).isTrue();
    }

    /**
     * If level is an empty string or null then the filter should do nothing
     * @throws Exception
     */
    @Test
    public void test_filter_with_null_param_value () throws Exception
    {
        MovieCatalog catalog = testUtils.parseFile(TestUtils.FILE_BASIC);

        Map<String, String []> params = new HashMap<>();
        params.put("level", null);
        censorFilter.filter(catalog, params);

        ExplorationResult result = explore(catalog);

        assertThat(result.uncensoredMediaCount.get()).isEqualTo(4);
        assertThat(result.censoredMediaCount.get()).isEqualTo(1);
        assertThat(result.containsGui31MFR.get()).isTrue();
        assertThat(result.containsGui31C.get()).isTrue();
    }

    /**
     * Removing one uncensored media (count should drop from 4 to 3), and media gui '3-1-MFR' should be removed.
     * @throws Exception
     */
    @Test
    public void test_filter_for_censored () throws Exception
    {
        String level = "Censored";

        MovieCatalog catalog = testUtils.parseFile(TestUtils.FILE_BASIC);
        Map<String, String []> params = new HashMap<>();
        params.put("level", new String [] { level });
        censorFilter.filter(catalog, params);

        ExplorationResult result = explore(catalog);

        assertThat(result.uncensoredMediaCount.get()).isEqualTo(3);
        assertThat(result.censoredMediaCount.get()).isEqualTo(1);
        assertThat(result.containsGui31MFR.get()).isFalse();
        assertThat(result.containsGui31C.get()).isTrue();
    }

    /**
     * Removing one uncensored media (count should drop from 4 to 3), and media gui '3-2-C' should be removed.
     * @throws Exception
     */
    @Test
    public void test_filter_for_uncensored () throws Exception
    {
        String level = "Uncensored";

        MovieCatalog catalog = testUtils.parseFile(TestUtils.FILE_BASIC);
        Map<String, String []> params = new HashMap<>();
        params.put("level", new String [] { level });
        censorFilter.filter(catalog, params);

        ExplorationResult result = explore(catalog);

        assertThat(result.uncensoredMediaCount.get()).isEqualTo(4);
        assertThat(result.censoredMediaCount.get()).isEqualTo(0);
        assertThat(result.containsGui31MFR.get()).isTrue();
        assertThat(result.containsGui31C.get()).isFalse();
    }

}
