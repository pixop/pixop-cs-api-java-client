package com.pixop.sdk.services.videos.iterator;
/*
 * ListProjectsIterator.java
 *
 * walks through 1 or more pages of list project response from the API
 */

import java.util.Iterator;

import com.pixop.sdk.services.videos.VideosServiceClient;
import com.pixop.sdk.services.videos.VideosServiceClient.VideosClientException;
import com.pixop.sdk.services.videos.request.PaginationParameters;
import com.pixop.sdk.services.videos.response.ListProjectsResponse;

/**
 * @author  Paul Cook
 * @version
 */
public final class ListProjectsIterator implements Iterable<ListProjectsResponse.ProjectSummary> {

    private final VideosServiceClient videosServiceClient;
    private final String jwtTokenString;

    public ListProjectsIterator(final VideosServiceClient videosServiceClient,
                                final String jwtTokenString) {
        this.videosServiceClient = videosServiceClient;
        this.jwtTokenString = jwtTokenString;
    }

    @Override
    public Iterator<ListProjectsResponse.ProjectSummary> iterator() {
        return new ProjectIterator(this.videosServiceClient,
                                   this.jwtTokenString);
    }

    public static final class ProjectIterator implements Iterator<ListProjectsResponse.ProjectSummary> {

        private final VideosServiceClient videosServiceClient;
        private final String jwtTokenString;

        private ListProjectsResponse apiResponse;
        private Iterator<ListProjectsResponse.ProjectSummary> pageIterator;

        private ProjectIterator(final VideosServiceClient videosServiceClient,
                                final String jwtTokenString) {
            this.videosServiceClient = videosServiceClient;
            this.jwtTokenString = jwtTokenString;
        }

        @Override
        public boolean hasNext() {
            if (this.apiResponse == null)
                getFirstPage();

            if (this.pageIterator.hasNext())
                return true;

            if (getNextPage())
                return true;

            return false;
        }

        @Override
        public ListProjectsResponse.ProjectSummary next() {
            if (this.apiResponse == null)
                getFirstPage();

            if (this.pageIterator.hasNext())
                return this.pageIterator.next();

            if (getNextPage())
                return this.pageIterator.next();

            return null;
        }

        private void getFirstPage() throws ProjectIteratorException {
            try {
                this.apiResponse = this.videosServiceClient.listProjects(this.jwtTokenString);
            } catch (final VideosClientException e) {
                throw new ProjectIteratorException("Failed to retrieve a page of results from the video service api ...", e);
            }
            this.pageIterator = this.apiResponse.getContents().getProjects().iterator();
        }

        private boolean getNextPage() throws ProjectIteratorException {
            if (this.apiResponse.getNavigationLinks().getNextPage() == null)
                return false;
            final PaginationParameters paginationParameters = new PaginationParameters.Builder()
                                                                                      .withPageSize(this.apiResponse.getPageSize())
                                                                                      .withPageNumber(this.apiResponse.getPageNumber() + 1)
                                                                                      .build();
            try {
                this.apiResponse = this.videosServiceClient.listProjects(this.jwtTokenString,
                                                                         paginationParameters);
            } catch (final VideosClientException e) {
                throw new ProjectIteratorException("Failed to retrieve a page of results from the video service api ...", e);
            }
            this.pageIterator = this.apiResponse.getContents().getProjects().iterator();
            return this.apiResponse.getContents().getProjects().size() > 0;
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }

    }

    public static final class ProjectIteratorException extends RuntimeException {

        private static final long serialVersionUID = -2429173763199061386L;

        public ProjectIteratorException(final String msg) {
            super(msg);
        }

        public ProjectIteratorException(final String msg, final Throwable t) {
            super(msg, t);
        }

    }

}