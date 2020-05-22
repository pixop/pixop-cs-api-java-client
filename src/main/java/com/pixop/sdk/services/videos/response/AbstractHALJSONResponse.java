package com.pixop.sdk.services.videos.response;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author  Paul Cook
 * @version
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public abstract class AbstractHALJSONResponse<T extends Serializable> implements java.io.Serializable {

    private static final long serialVersionUID = 531296459146925666L;

    @JsonProperty("pageSize")
    private final int pageSize;

    @JsonProperty("page")
    private final int pageNumber;

    @JsonProperty("_links")
    private final NavigationLinks navigationLinks;

    @JsonProperty("_embedded")
    private final T contents;

    @JsonCreator
    public AbstractHALJSONResponse(@JsonProperty("pageSize")final int pageSize,
                                   @JsonProperty("page")final int pageNumber,
                                   @JsonProperty("_links")final NavigationLinks navigationLinks,
                                   @JsonProperty("_embedded")final T contents) {
        this.pageSize = pageSize;
        this.pageNumber = pageNumber;
        this.navigationLinks = navigationLinks;
        this.contents = contents;
    }

    public int getPageSize() {
        return this.pageSize;
    }

    public int getPageNumber() {
        return this.pageNumber;
    }

    public NavigationLinks getNavigationLinks() {
        return this.navigationLinks;
    }

    public T getContents() {
        return this.contents;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("HAL-JSON-NAVIGATION-HEADERS :::\n");
        sb.append("   page-size=").append(this.pageSize).append('\n');
        sb.append("   page-number=").append(this.pageNumber).append('\n');
        if (this.navigationLinks != null) {
            if (this.navigationLinks.self != null)
                sb.append("   link-to-self=").append(this.navigationLinks.self.href).append('\n');
            if (this.navigationLinks.nextPage != null)
                sb.append("   link-to-next=").append(this.navigationLinks.nextPage.href).append('\n');
            if (this.navigationLinks.previousPage != null)
                sb.append("   link-to-previous=").append(this.navigationLinks.previousPage.href).append('\n');
            if (this.navigationLinks.firstPage != null)
                sb.append("   link-to-first=").append(this.navigationLinks.firstPage.href).append('\n');
            if (this.navigationLinks.lastPage != null)
                sb.append("   link-to-last=").append(this.navigationLinks.lastPage.href).append('\n');
        }
        return sb.toString();
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static final class NavigationLinks implements java.io.Serializable {

        private static final long serialVersionUID = -7520073475237248817L;

        @JsonProperty("self")
        private final Link self;
        @JsonProperty("next")
        private final Link nextPage;
        @JsonProperty("prev")
        private final Link previousPage;
        @JsonProperty("first")
        private final Link firstPage;
        @JsonProperty("last")
        private final Link lastPage;

        @JsonCreator
        public NavigationLinks(@JsonProperty("self")final Link self,
                               @JsonProperty("next")final Link nextPage,
                               @JsonProperty("prev")final Link previousPage,
                               @JsonProperty("first")final Link firstPage,
                               @JsonProperty("last")final Link lastPage) {
            this.self = self;
            this.nextPage = nextPage;
            this.previousPage = previousPage;
            this.firstPage = firstPage;
            this.lastPage = lastPage;
        }

        public Link getSelf() {
            return this.self;
        }

        public Link getNextPage() {
            return this.nextPage;
        }

        public Link getPreviousPage() {
            return this.previousPage;
        }

        public Link getFirstPage() {
            return this.firstPage;
        }

        public Link getLastPage() {
            return this.lastPage;
        }

    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static final class EntityLinks implements java.io.Serializable {

        private static final long serialVersionUID = 6301450972554884599L;

        @JsonProperty("self")
        private final Link self;

        @JsonCreator
        public EntityLinks(@JsonProperty("self")final Link self) {
            this.self = self;
        }

        public Link getSelf() {
            return this.self;
        }

    }

    public static final class Link implements java.io.Serializable {

        private static final long serialVersionUID = -7452634451047024613L;

        @JsonProperty("href")
        private final String href;

        @JsonCreator
        public Link(@JsonProperty("href")final String href) {
            this.href = href;
        }

        public String getHref() {
            return this.href;
        }

        @Override
        public String toString() {
            return this.href;
        }

    }

}