package com.docsachdi.services;

import com.docsachdi.entities.Book;
import com.docsachdi.entities.User;
import com.docsachdi.repositories.BookRepository;
import com.docsachdi.repositories.UserRepository;
import com.docsachdi.request.ListUserBookReq;
import com.docsachdi.request.UpdateStatusBookReq;
import com.docsachdi.response.ListUserBookRes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BookRepository bookRepository;

    public Set<Long> getSetBook(String bookIdsString)
    {
        if (bookIdsString == null || bookIdsString.trim().isEmpty()) {
            return new HashSet<>(); // Return empty set for null or empty input
        }
        try {
            return Arrays.stream(bookIdsString.split(","))
                    .map(String::trim) // Remove leading/trailing whitespace
                    .mapToLong(Long::parseLong) // Convert to Integer
                    .boxed() // Convert int to Integer object
                    .collect(Collectors.toSet());
        } catch (NumberFormatException e) {
            System.err.println("Invalid book ID string: " + bookIdsString);
            return new HashSet<>();
        }
    }

    public String convertIntSetToString(Set<Long> intSet) {
        if (intSet == null || intSet.isEmpty()) {
            return ""; // Return empty string for null or empty set
        }

        return intSet.stream()
                .map(String::valueOf) // Convert each Integer to String
                .collect(Collectors.joining(","));
    }



    public boolean updateStatusBook(UpdateStatusBookReq req) {
        Long uId = req.uId;
        Long bookId = req.bookId;
        Optional<User> user = userRepository.findById(uId);
        if(!user.isPresent()) {
            return false;
        }
        User u = user.get();
        Optional<Book> book = bookRepository.findById(bookId);
        if(!book.isPresent()) {
            return false;
        }
        Book b = book.get();
        Set<Long> setWish= getSetBook(u.getBookWishlist());
        Set<Long> setRead= getSetBook(u.getBookReadList());
        Set<Long> setReading= getSetBook(u.getBookReadingList());
        if(setWish.contains(req.bookId))
            setWish.remove(req.bookId);
        if(setReading.contains(req.bookId))
            setReading.remove(req.bookId);
        if(setRead.contains(req.bookId))
            setRead.remove(req.bookId);
        switch (req.status) {
            case 0:
                setWish.add(req.bookId);
                break;
            case 1:
                setReading.add(req.bookId);
                break;
            case 2:
                setRead.add(req.bookId);
                break;
            default:
                return false;
        }
        u.setBookWishlist(convertIntSetToString(setWish));
        u.setBookReadingList(convertIntSetToString(setReading));
        u.setBookReadList(convertIntSetToString(setRead));
        userRepository.save(u);
        return true;
    }

    public ListUserBookRes getListUserBook(ListUserBookReq req) {
        Long uId = req.uId;
        Optional<User> user = userRepository.findById(uId);
        if(!user.isPresent()) {
            return null;
        }
        User u = user.get();
        ListUserBookRes res = new ListUserBookRes();
        res.setuId(uId);
        res.setBookWishlist(getSetBook(u.getBookWishlist()));
        res.setBookReadingList(getSetBook(u.getBookReadingList()));
        res.setBookReadList(getSetBook(u.getBookReadList()));
        return res;
    }
}
