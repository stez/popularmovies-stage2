package it.and.stez78.popularmovies.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by stefano on 19/02/18.
 */

public class MovieSearchResponse<T> {

    @SerializedName("page")
    @Expose
    private Long page;
    @SerializedName("total_results")
    @Expose
    private Long totalResults;
    @SerializedName("total_pages")
    @Expose
    private Long totalPages;
    @SerializedName("results")
    @Expose
    private List<T> results = null;

    public Long getPage() {
        return page;
    }

    public void setPage(Long page) {
        this.page = page;
    }

    public Long getTotalResults() {
        return totalResults;
    }

    public void setTotalResults(Long totalResults) {
        this.totalResults = totalResults;
    }

    public Long getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(Long totalPages) {
        this.totalPages = totalPages;
    }

    public List<T> getResults() {
        return results;
    }

    public void setResults(List<T> results) {
        this.results = results;
    }

}
