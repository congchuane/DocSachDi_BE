package com.docsachdi.services;

import com.docsachdi.entities.Review;
import com.docsachdi.entities.Book;
import com.docsachdi.repositories.BookRepository;
import com.docsachdi.repositories.ReviewRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class ReviewService {

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private BookRepository bookRepository;

    public Review addReview(Long bookId, String username, String review, int rating) {
        Book book = bookRepository.findById(bookId).orElse(null);
        if (book != null) {
            Review newReview = new Review();
            newReview.setUsername(username);
            newReview.setReview(review);
            newReview.setRating(rating);
            newReview.setBook(book);

            double currentRating = book.getRating();
            int reviewCount = book.getReviews().size();
            double newRating =  (currentRating * reviewCount + rating) / (reviewCount + 1);
            book.setRating(newRating);

            book.getReviews().add(newReview);
            bookRepository.save(book);

            return reviewRepository.save(newReview);
        }
        return null;
    }
}
