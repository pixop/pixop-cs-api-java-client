package com.pixop.sdk.services.videos.iterator;
/*
 * ListVideosIterator.java
 *
 * walks through 1 or more pages of list video response from the API
 */

import java.util.Iterator;

import com.pixop.sdk.services.videos.VideosServiceClient;
import com.pixop.sdk.services.videos.VideosServiceClient.VideosClientException;
import com.pixop.sdk.services.videos.request.PaginationParameters;
import com.pixop.sdk.services.videos.response.ListVideosResponse;

/**
 * @author  Paul Cook
 * @version
 */
public final class ListVideosIterator implements Iterable<ListVideosResponse.VideoSummary> {

    private final VideosServiceClient videosServiceClient;
    private final String jwtTokenString;
    private final String projectId;

    public ListVideosIterator(final VideosServiceClient videosServiceClient,
                              final String jwtTokenString,
                              final String projectId) {
        this.videosServiceClient = videosServiceClient;
        this.jwtTokenString = jwtTokenString;
        this.projectId = projectId;
    }

    @Override
    public Iterator<ListVideosResponse.VideoSummary> iterator() {
        return new VideoIterator(this.videosServiceClient,
                                 this.jwtTokenString,
                                 this.projectId);
    }

    public static final class VideoIterator implements Iterator<ListVideosResponse.VideoSummary> {

        private final VideosServiceClient videosServiceClient;
        private final String jwtTokenString;
        private final String projectId;

        private ListVideosResponse apiResponse;
        private Iterator<ListVideosResponse.VideoSummary> pageIterator;

        private VideoIterator(final VideosServiceClient videosServiceClient,
                              final String jwtTokenString,
                              final String projectId) {
            this.videosServiceClient = videosServiceClient;
            this.jwtTokenString = jwtTokenString;
            this.projectId = projectId;
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
        public ListVideosResponse.VideoSummary next() {
            if (this.apiResponse == null)
                getFirstPage();

            if (this.pageIterator.hasNext())
                return this.pageIterator.next();

            if (getNextPage())
                return this.pageIterator.next();

            return null;
        }

        private void getFirstPage() throws VideoIteratorException {
            try {
                if (this.projectId != null)
                    this.apiResponse = this.videosServiceClient.listVideosInProject(this.projectId,
                                                                                    this.jwtTokenString);
                else
                    this.apiResponse = this.videosServiceClient.listVideos(this.jwtTokenString);
            } catch (final VideosClientException e) {
                throw new VideoIteratorException("Failed to retrieve a page of results from the video service api ...", e);
            }
            this.pageIterator = this.apiResponse.getContents().getVideos().iterator();
        }

        private boolean getNextPage() throws VideoIteratorException {
            if (this.apiResponse.getNavigationLinks().getNextPage() == null)
                return false;
            final PaginationParameters paginationParameters = new PaginationParameters.Builder()
                                                                                      .withPageSize(this.apiResponse.getPageSize())
                                                                                      .withPageNumber(this.apiResponse.getPageNumber() + 1)
                                                                                      .build();
            try {
                if (this.projectId != null)
                    this.apiResponse = this.videosServiceClient.listVideosInProject(this.projectId,
                                                                                    this.jwtTokenString,
                                                                                    paginationParameters);
                else
                    this.apiResponse = this.videosServiceClient.listVideos(this.jwtTokenString,
                                                                           paginationParameters);
            } catch (final VideosClientException e) {
                throw new VideoIteratorException("Failed to retrieve a page of results from the video service api ...", e);
            }
            this.pageIterator = this.apiResponse.getContents().getVideos().iterator();
            return this.apiResponse.getContents().getVideos().size() > 0;
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }

    }

    public static final class VideoIteratorException extends RuntimeException {

        private static final long serialVersionUID = 5982958832652324718L;

        public VideoIteratorException(final String msg) {
            super(msg);
        }

        public VideoIteratorException(final String msg, final Throwable t) {
            super(msg, t);
        }

    }

}