package com.docsachdi.controllers;

import com.docsachdi.dtos.BookDTO;
import com.docsachdi.services.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/books")
public class BookController {

    @Autowired
    private BookService bookService;

    @GetMapping
    public ResponseEntity<List<BookDTO>> getAllBooks() {
        List<BookDTO> books = bookService.getAllBooks();
        return ResponseEntity.status(HttpStatus.OK).body(books);
    }

    @GetMapping("/newest")
    public List<BookDTO> getNewestBooks() {
        return bookService.getNewestBooks();
    }

    @PostMapping
    public ResponseEntity<BookDTO> addBook(
            @RequestParam("title") String title,
            @RequestParam("description") String description,
            @RequestParam("file") MultipartFile file,
            @RequestParam("authors") String authorsJson,
            @RequestParam("tags") String tagsJson) throws IOException {

        BookDTO bookDTO = new BookDTO();
        bookDTO.setTitle(title);
        bookDTO.setDescription(description);

        String coverPath = bookService.saveBookCover(file);
        bookDTO.setCoverPath(coverPath);

        List<String> authorNames = bookService.parseAuthors(authorsJson);
        List<String> tagNames = bookService.parseTags(tagsJson);

        BookDTO savedBook = bookService.saveBookWithAuthorsAndTags(bookDTO, authorNames, tagNames);

        return ResponseEntity.status(HttpStatus.CREATED).body(savedBook);
    }

    @GetMapping("/{bookId}")
    public ResponseEntity<BookDTO> getBook(@PathVariable Long bookId) {
        Optional<BookDTO> bookOptional = bookService.getBookById(bookId);
        return bookOptional.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PatchMapping("/{bookId}")
    public ResponseEntity<BookDTO> editBook(
            @PathVariable Long bookId,
            @RequestParam(value = "title", required = false) String title,
            @RequestParam(value = "description", required = false) String description,
            @RequestParam(value = "file", required = false) MultipartFile file,
            @RequestParam(value = "authors", required = false) String authorsJson,
            @RequestParam(value = "tags", required = false) String tagsJson) throws IOException {

        BookDTO bookDTO = new BookDTO();

        // Chỉ thiết lập giá trị nếu có tham số được truyền vào
        if (title != null) {
            bookDTO.setTitle(title);
        }
        if (description != null) {
            bookDTO.setDescription(description);
        }

        // Xử lý file nếu có ảnh được upload
        if (file != null) {
            String coverPath = bookService.saveBookCover(file);
            bookDTO.setCoverPath(coverPath);
        }

        // Xử lý authors và tags nếu có
        List<String> authorNames = authorsJson != null ? bookService.parseAuthors(authorsJson) : null;
        List<String> tagNames = tagsJson != null ? bookService.parseTags(tagsJson) : null;

        BookDTO updatedBook = bookService.editBook(bookId, bookDTO, authorNames, tagNames);

        return ResponseEntity.ok(updatedBook);
    }

    @DeleteMapping("/{bookId}")
    public ResponseEntity<Void> deleteBook(@PathVariable Long bookId) {
        bookService.deleteBook(bookId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/search")
    public ResponseEntity<List<BookDTO>> searchBooks(@RequestParam String query) {
        List<BookDTO> books = bookService.searchBooks(query);
        return ResponseEntity.ok(books);
    }

}
