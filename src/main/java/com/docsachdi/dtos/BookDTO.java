package com.docsachdi.dtos;

import java.util.List;
import java.util.Set;

public class BookDTO {
    private Long id;
    private String title;
    private Set<AuthorDTO> authors;
    private List<String> authorNames;
    private Set<TagDTO> tags;
    private List<String> tagNames;
    private Double rating;
    private String description;
    private String coverPath;

    public BookDTO() {
    }

    public BookDTO(String title, List<String> authorNames, Set<AuthorDTO> authors, List<String> tagNames, Set<TagDTO> tags, Double rating, String description, String coverPath) {
        this.title = title;
        this.authorNames = authorNames;
        this.authors = authors;
        this.tagNames = tagNames;
        this.tags = tags;
        this.rating = rating;
        this.description = description;
        this.coverPath = coverPath;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCoverPath() {
        return coverPath;
    }

    public void setCoverPath(String coverPath) {
        this.coverPath = coverPath;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<String> getAuthorNames() {
        return authorNames;
    }

    public void setAuthorNames(List<String> authorNames) {
        this.authorNames = authorNames;
    }

    public Set<AuthorDTO> getAuthors() {
        return authors;
    }

    public void setAuthors(Set<AuthorDTO> authors) {
        this.authors = authors;
    }

    public List<String> getTagNames() {
        return tagNames;
    }

    public void setTagNames(List<String> tagNames) {
        this.tagNames = tagNames;
    }

    public Set<TagDTO> getTags() {
        return tags;
    }

    public void setTags(Set<TagDTO> tags) {
        this.tags = tags;
    }

    public Double getRating() {
        return rating;
    }

    public void setRating(Double rating) {
        this.rating = rating;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
