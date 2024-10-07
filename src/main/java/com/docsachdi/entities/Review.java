package com.docsachdi.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;

@Entity
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String username;
    private String review;
    private int rating;

    @ManyToOne
    @JoinColumn(name = "book_id")
    @JsonBackReference
    private Book book;

    public Long getId(){
        return id;
    }

    public void setId(Long id){
        this.id=id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getReview() {
        return review;
    }

    public void setReview(String review) {
        this.review=review;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public Book getBook(){
        return book;
    }

    public void setBook(Book book){
        this.book = book;
    }

    @Override
    public String toString() {
        return "Review{" +
                "id=" + id +
                ", book='" + book.getTitle() + '\'' +
                ", username='" + username + '\'' +
                ", review='" + review + '\'' +
                ", rating=" + rating +
                '}';
    }



}
