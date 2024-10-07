package com.docsachdi.entities;

import jakarta.persistence.*;

@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String username;
    private String password;
    private String email;
    private String role;
    private String bookWishlist;
    private String bookReadList;
    private String bookReadingList;


    public User() {}

    public User(String username, String password, String email, String role, String bookWishlist, String bookReadList, String bookReadingList) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.role = role;
        this.bookWishlist = bookWishlist;
        this.bookReadList = bookReadList;
        this.bookReadingList = bookReadingList;
    }

    public String getBookWishlist() {
        return bookWishlist;
    }

    public void setBookWishlist(String bookWishlist) {
        this.bookWishlist = bookWishlist;
    }

    public String getBookReadList() {
        return bookReadList;
    }

    public void setBookReadList(String bookReadList) {
        this.bookReadList = bookReadList;
    }

    public String getBookReadingList() {
        return bookReadingList;
    }

    public void setBookReadingList(String bookReadingList) {
        this.bookReadingList = bookReadingList;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() { return email; }

    public void setEmail(String email) { this.email = email; }

    public String getRole() {
        return role;
    }

    public void setRole(String role){
        this.role = role;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", password=" + password + '\'' +
                ", email='" + email + '\'' +
                ", role='" + role + '\'' +
                '}';
    }
}
