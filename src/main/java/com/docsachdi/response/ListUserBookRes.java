package com.docsachdi.response;

import java.util.Set;

public class ListUserBookRes {
    Long uId;
    Set<Long> bookWishlist;
    Set<Long> bookReadList;
    Set<Long> bookReadingList;

    public Long getuId() {
        return uId;
    }

    public void setuId(Long uId) {
        this.uId = uId;
    }

    public Set<Long> getBookWishlist() {
        return bookWishlist;
    }

    public void setBookWishlist(Set<Long> bookWishlist) {
        this.bookWishlist = bookWishlist;
    }

    public Set<Long> getBookReadList() {
        return bookReadList;
    }

    public void setBookReadList(Set<Long> bookReadList) {
        this.bookReadList = bookReadList;
    }

    public Set<Long> getBookReadingList() {
        return bookReadingList;
    }

    public void setBookReadingList(Set<Long> bookReadingList) {
        this.bookReadingList = bookReadingList;
    }
}
