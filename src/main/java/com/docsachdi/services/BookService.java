package com.docsachdi.services;

import com.docsachdi.dtos.AuthorDTO;
import com.docsachdi.dtos.BookDTO;
import com.docsachdi.dtos.TagDTO;
import com.docsachdi.entities.Author;
import com.docsachdi.entities.Book;
import com.docsachdi.entities.Tag;
import com.docsachdi.exceptions.ResourceNotFoundException;
import com.docsachdi.repositories.AuthorRepository;
import com.docsachdi.repositories.BookRepository;
import com.docsachdi.repositories.TagRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class BookService {

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private AuthorRepository authorRepository;

    @Autowired
    private TagRepository tagRepository;

    public List<BookDTO> getAllBooks() {
        List<Book> books = bookRepository.findAll();
        List<BookDTO> bookDTOs = new ArrayList<>();
        for (Book book : books) {
            bookDTOs.add(convertToBookDTO(book));
        }
        return bookDTOs;
    }

    public List<BookDTO> getNewestBooks() {
        Pageable pageable = PageRequest.of(0, 10); // Page 0, size 10
        Page<Book> booksPage = bookRepository.findAllByOrderByCreatedAtDesc(pageable);

        List<BookDTO> bookDTOs = new ArrayList<>();
        for (Book book : booksPage) {
            bookDTOs.add(convertToBookDTO(book));
        }
        return bookDTOs;
    }

    public BookDTO saveBook(BookDTO bookDTO) {
        Book book = convertToBookEntity(bookDTO);
        Book savedBook = bookRepository.save(book);
        return convertToBookDTO(savedBook);
    }

    public Optional<BookDTO> getBookById(Long bookId) {
        Optional<Book> book = bookRepository.findById(bookId);
        return book.map(this::convertToBookDTO);
    }

    private final String uploadDir = "/Users/vinguyen/OneDrive/GitHub/docsachdi/public/uploads";

    public String saveBookCover(MultipartFile file) throws IOException {
        String fileName = file.getOriginalFilename();
        Path filePath = Paths.get(uploadDir, fileName);
        Files.createDirectories(filePath.getParent()); // Tạo thư mục nếu chưa tồn tại
        try (FileOutputStream fos = new FileOutputStream(new File(filePath.toString()))) {
            fos.write(file.getBytes());
        }
        return filePath.toString();
    }

    public List<String> parseAuthors(String authorsJson) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(authorsJson, new TypeReference<List<String>>() {});
    }

    public List<String> parseTags(String tagsJson) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(tagsJson, new TypeReference<List<String>>() {});
    }

    private BookDTO convertToBookDTO(Book book) {
        BookDTO bookDTO = new BookDTO();
        bookDTO.setId(book.getId());
        bookDTO.setTitle(book.getTitle());
        bookDTO.setDescription(book.getDescription());
        bookDTO.setRating(book.getRating());
        bookDTO.setCoverPath(book.getCoverPath());
        bookDTO.setAuthorNames(book.getAuthorNames());
        bookDTO.setTagNames(book.getTagNames());

        Set<AuthorDTO> authorDTOs = book.getAuthors().stream().map(author -> {
            AuthorDTO authorDTO = new AuthorDTO();
            authorDTO.setId(author.getId());
            authorDTO.setName(author.getName());
            return authorDTO;
        }).collect(Collectors.toSet());

        bookDTO.setAuthors(authorDTOs);

        Set<TagDTO> tagDTOs = book.getTags().stream().map(tag -> {
            TagDTO tagDTO = new TagDTO();
            tagDTO.setId(tag.getId());
            tagDTO.setName(tag.getName());
            return tagDTO;
        }).collect(Collectors.toSet());

        bookDTO.setTags(tagDTOs);

        return bookDTO;
    }

    private Book convertToBookEntity(BookDTO bookDTO) {
        Book book = new Book();
        book.setTitle(bookDTO.getTitle());
        book.setDescription(bookDTO.getDescription());
        book.setRating(bookDTO.getRating());

        Set<Author> authors = new HashSet<>();
        for (AuthorDTO authorDTO : bookDTO.getAuthors()) {
            Author author = new Author();
            author.setId(authorDTO.getId());
            author.setName(authorDTO.getName());
            authors.add(author);
        }
        book.setAuthors(authors);

        Set<Tag> tags = new HashSet<>();
        for (TagDTO tagDTO : bookDTO.getTags()) {
            Tag tag = new Tag();
            tag.setId(tagDTO.getId());
            tag.setName(tagDTO.getName());
            tags.add(tag);
        }
        book.setTags(tags);

        return book;
    }

    public BookDTO saveBookWithAuthorsAndTags(BookDTO bookDTO, List<String> authorNames, List<String> tagNames) {
        Book book = new Book();
        book.setTitle(bookDTO.getTitle());
        book.setDescription(bookDTO.getDescription());
        book.setCoverPath(bookDTO.getCoverPath());

        Set<Author> authors = new HashSet<>();
        for (String authorName : authorNames) {
            Author author = authorRepository.findByName(authorName).orElse(null);
            if (author == null) {
                author = new Author();
                author.setName(authorName);
                author = authorRepository.save(author);
            }
            authors.add(author);
        }
        book.setAuthors(authors);

        Set<Tag> tags = new HashSet<>();
        for (String tagName : tagNames) {
            Tag tag = tagRepository.findByName(tagName).orElse(null);
            if (tag == null) {
                tag = new Tag();
                tag.setName(tagName);
                tag = tagRepository.save(tag);
            }
            tags.add(tag);
        }
        book.setTags(tags);

        Book savedBook = bookRepository.save(book);

        BookDTO savedBookDTO = convertToBookDTO(savedBook);
        savedBookDTO.setAuthorNames(authorNames);
        savedBookDTO.setTagNames(tagNames);

        return savedBookDTO;
    }

    public BookDTO editBook(Long bookId, BookDTO bookDTO, List<String> authorNames, List<String> tagNames) {

        Book existingBook = bookRepository.findById(bookId)
                .orElseThrow(() -> new ResourceNotFoundException("Book not found with id: " + bookId));

        if (bookDTO.getTitle() != null && !bookDTO.getTitle().trim().isEmpty()) {
            existingBook.setTitle(bookDTO.getTitle());
        }

        if (bookDTO.getDescription() != null && !bookDTO.getDescription().trim().isEmpty()) {
            existingBook.setDescription(bookDTO.getDescription());
        }

        if (bookDTO.getCoverPath() != null) {
            existingBook.setCoverPath(bookDTO.getCoverPath());
        }

        if (authorNames != null && !authorNames.isEmpty()) {
            Set<Author> authors = new HashSet<>();
            for (String authorName : authorNames) {
                Author author = authorRepository.findByName(authorName)
                        .orElse(null);
                if (author == null) {
                    author = new Author();
                    author.setName(authorName);
                    author = authorRepository.save(author);
                }
                authors.add(author);
            }
            existingBook.setAuthors(authors);
        }

        if (tagNames != null && !tagNames.isEmpty()) {
            Set<Tag> tags = new HashSet<>();
            for (String tagName : tagNames) {
                Tag tag = tagRepository.findByName(tagName)
                        .orElse(null);
                if (tag == null) {
                    tag = new Tag();
                    tag.setName(tagName);
                    tag = tagRepository.save(tag);
                }
                tags.add(tag);
            }
            existingBook.setTags(tags);
        }

        Book savedBook = bookRepository.save(existingBook);

        return convertToBookDTO(savedBook);
    }


    public void deleteBook(Long bookId) {
        if (bookRepository.existsById(bookId)) {
            bookRepository.deleteById(bookId);
        } else {
            throw new ResourceNotFoundException("Book not found with id: " + bookId);
        }
    }

}
