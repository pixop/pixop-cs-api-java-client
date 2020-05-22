package com.pixop.sdk.services.videos.request;
/*
 * ProcessVideoRequest.java
 *
 * page size and page number for paginated navigation
 */

import java.util.HashMap;
import java.util.Map;

/**
 * @author  Paul Cook
 * @version
 */
public final class PaginationParameters implements java.io.Serializable {

    private static final long serialVersionUID = 5250347319997998043L;

    private final int pageNumber;
    private final int pageSize;

    private PaginationParameters(final int pageNumber,
                                 final int pageSize) {
        this.pageNumber = pageNumber;
        this.pageSize = pageSize;
    }

    @Override
    public String toString() {
        return "PAGINATION-PARAMETERS :: PAGE-NUMBER [ " + this.pageNumber + " ] :: PAGE-SIZE [ " + this.pageSize + " ] ";
    }

    public static final class Builder {

        private int pageNumber = 1;
        private int pageSize = 10;

        public PaginationParameters build() {
            return new PaginationParameters(this.pageNumber,
                                            this.pageSize);
        }

        public Builder withPageNumber(final int pageNumber) {
            this.pageNumber = pageNumber;
            return this;
        }

        public Builder withPageSize(final int pageSize) {
            this.pageSize = pageSize;
            return this;
        }

    }

    public Map<String, String> toQueryParametersMap() {
        final Map<String, String> params = new HashMap<>();
        toQueryParameters(params);
        return params;
    }

    public void toQueryParameters(final Map<String, String> params) {
        params.put("page", "" + this.pageNumber);
        params.put("page-size", "" + this.pageSize);
    }

}