package com.pixop.sdk.services.videos.model;
/*
 * {
 *  "cast": "A curious hawksbill sea turtle",
 *  "notes": "Stockvideo-ID: 13945376",
 *  "genres": "Nature, Science, Underwater",
 *  "writer": "Mother nature",
 *  "country": "Sweden",
 *  "director": "Kristian Pettersson",
 *  "language": "None spoken",
 *  "producer": "Kristian Pettersson"
 * }
 */

import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * @author  Paul Cook
 * @version
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class VideoAttributes implements java.io.Serializable {

    private static final long serialVersionUID = 5489292996527754799L;

    private String cast;
    private String notes;
    private String genres;
    private String writer;
    private String country;
    private String director;
    private String language;
    private String producer;

    public VideoAttributes() {
    }

    public final String getCast() {
        return this.cast;
    }

    public final void setCast(final String cast) {
        this.cast = cast;
    }

    public final String getNotes() {
        return this.notes;
    }

    public final void setNotes(final String notes) {
        this.notes = notes;
    }

    public final String getGenres() {
        return this.genres;
    }

    public final void setGenres(final String genres) {
        this.genres = genres;
    }

    public final String getWriter() {
        return this.writer;
    }

    public final void setWriter(final String writer) {
        this.writer = writer;
    }

    public final String getCountry() {
        return this.country;
    }

    public final void setCountry(final String country) {
        this.country = country;
    }

    public final String getDirector() {
        return this.director;
    }

    public final void setDirector(final String director) {
        this.director = director;
    }

    public final String getLanguage() {
        return this.language;
    }

    public final void setLanguage(final String language) {
        this.language = language;
    }

    public final String getProducer() {
        return this.producer;
    }

    public final void setProducer(final String producer) {
        this.producer = producer;
    }

}