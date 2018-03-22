package com.github.apycazo.starzplay.data;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MovieEntry
{
    private String id;
    private String guid;
    private long updated;
    private String title;
    private String description;
    private long added;
    private boolean approved;

    private List<Object> credits;

    private Object descriptionLocalized;
    private Object displayGenre;
    private Object editorialRating;

    private List<Object> imageMediaIds;
    private Object isAdult;
    private List<String> languages;
    private String longDescription;
    private Object longDescriptionLocalized;

    private String programType;
    private List<Object> ratings;
    private Object seriesEpisodeNumber;

    private Object seriesId;
    private String shortDescription;
    private Object shortDescriptionLocalized;

    private List<String> tagIds;
    private List<Object> tags;
    private Object thumbnails;

    private Object titleLocalized;
    private Object tvSeasonEpisodeNumber;
    private Object tvSeasonId;
    private Object tvSeasonNumber;

    private Integer year;

    private List<Media> media;

    private String peg$ISOcountryOfOrigin;
    private Integer peg$arAgeRating;
    private String peg$arContentRating;
    private String peg$availableInSection;
    private String peg$contentClassification;
    private String peg$contractName;
    private String peg$countryOfOrigin;
    private String peg$priorityLevel;
    private String peg$priorityLevelActionandAdventure;
    private String peg$priorityLevelArabic;
    private String peg$priorityLevelChildrenandFamily;
    private String peg$priorityLevelComedy;
    private String peg$priorityLevelDisney;
    private String peg$priorityLevelDisneyKids;
    private String peg$priorityLevelDrama;
    private String peg$priorityLevelKids;
    private String peg$priorityLevelKidsAction;
    private String peg$priorityLevelKidsFunandAdventure;
    private String peg$priorityLevelKidsMagicandDreams;
    private String peg$priorityLevelKidsPreschool;
    private String peg$priorityLevelRomance;
    private String peg$priorityLevelThriller;
    private String peg$productCode;
    private String peg$programMediaAvailability;





}
