package com.example.goodreads_finalproject.service;

import com.example.goodreads_finalproject.entity.*;
import com.example.goodreads_finalproject.exception.*;
import com.example.goodreads_finalproject.model.request.*;
import com.example.goodreads_finalproject.model.response.*;
import com.example.goodreads_finalproject.repository.*;
import com.example.goodreads_finalproject.repository.custom.BookCustomRepository;
import com.example.goodreads_finalproject.repository.custom.CommentCustomRepository;
import com.example.goodreads_finalproject.repository.custom.ReviewCustomRepository;
import com.example.goodreads_finalproject.statics.ReadingStatus;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@Service
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class BookService {
    BookRepository bookRepository;
    UserRepository userRepository;
    CategoryService categoryService;
    ReadingBookRepository readingBookRepository;
    BookOfChallengeRepository bookOfChallengeRepository;
    ObjectMapper objectMapper;
    BookCustomRepository bookCustomRepository;
    ReviewBookRepository reviewBookRepository;
    ReviewCustomRepository reviewCustomRepository;
    CommentCustomRepository commentCustomRepository;
    CommentRepository commentRepository;
    LikeRepository likeRepository;


    // BOOK
    public void createBook(BookRequest newBook) {
        Set<Category> categories = new HashSet<>();
        newBook.getCategoryId().forEach(id -> categories.add(categoryService.findById(id).get()));

        String bookNameParam = newBook.getTitle().trim().replace(" ", "%20");
        String buyBookFahasaLink = "https://www.fahasa.com/searchengine?q=" + bookNameParam;

        Book book = Book.builder()
                .image(newBook.getImage().equals("") ? "/original/images/books/no-cover.png" : newBook.getImage())
                .title(newBook.getTitle())
                .author(newBook.getAuthor())
                .buyBook(newBook.getBuyBook())
                .categories(categories)
                .description(newBook.getDescription())
                .published(newBook.getPublished())
                .rating(0)
                .pages(newBook.getPages())
                .buyBook(newBook.getBuyBook().equals("") ? buyBookFahasaLink : newBook.getBuyBook())
                .build();
        bookRepository.save(book);
    }

    public void updateBook(BookRequest updateBookRequest) {
        Book book = bookRepository.findById(updateBookRequest.getBookId()).get();
        Set<Long> categoryIds = updateBookRequest.getCategoryId();
        Set<Category> categories = new HashSet<>();

        categoryIds.forEach(id -> categories.add(categoryService.findById(id).get()));

        if (!updateBookRequest.getImage().equals("")) {
            book.setImage(updateBookRequest.getImage());
        }

        if (!updateBookRequest.getBuyBook().equals("")) {
            book.setBuyBook(updateBookRequest.getBuyBook());
        }

        book.setTitle(updateBookRequest.getTitle());
        book.setAuthor(updateBookRequest.getAuthor());
        book.setCategories(categories);
        book.setPages(updateBookRequest.getPages());
        book.setDescription(updateBookRequest.getDescription());
        book.setPublished(updateBookRequest.getPublished());
        bookRepository.save(book);
    }

    public Book findBookByBookId(Long bookId) {
        Optional<Book> bookResponse = bookRepository.findById(bookId);
        if (bookResponse.isEmpty()) {
            throw new NotFoundException("Book not found!");
        }
        return bookResponse.get();
    }

    public BookResponse findBookByBookId(Long bookId, Long userId) {
        BookResponse bookResponse = bookCustomRepository.findByBookIdAndUserId(bookId, userId);
        if (bookResponse == null) {
            throw new NotFoundException("Book not found!");
        }
        return bookResponse;
    }

    public CommonResponse<?> searchBook(BookSearchRequest request) {
        try {
            List<BookResponse> books = bookCustomRepository.searchBook(request);
            Integer totalResult = books.size();

            Integer pageIndex = request.getPageIndex();
            Integer pageSize = request.getPageSize();

            PaginationUtils<BookResponse> paginationUtils = new PaginationUtils<>();
            int pageNumber = paginationUtils.getPageNumber(books, pageSize);
            books = paginationUtils.searchData(books, pageIndex, pageSize);


            return CommonResponse.builder()
                    .totalResult(totalResult)
                    .pageNumber(pageNumber)
                    .data(books)
                    .build();
        } catch (Exception e) {
            throw new NotFoundException("Page index out of bound");
        }
    }

    public CommonResponse<?> searchBookAuthen(BookSearchRequest request, Long userId) {
        try {
            List<BookResponse> books = bookCustomRepository.searchBookAuthen(request, userId);
            Integer totalResult = books.size();
            Integer pageIndex = request.getPageIndex();
            Integer pageSize = request.getPageSize();

            PaginationUtils<BookResponse> paginationUtils = new PaginationUtils<>();
            int pageNumber = paginationUtils.getPageNumber(books, pageSize);
            books = paginationUtils.searchData(books, pageIndex, pageSize);


            return CommonResponse.builder()
                    .totalResult(totalResult)
                    .pageNumber(pageNumber)
                    .data(books)
                    .build();
        } catch (Exception e) {
            throw new NotFoundException("Page index out of bound");
        }
    }

    public void markBook(ReadingBookRequest request) {
        Optional<Book> bookOptional = bookRepository.findById(request.getBookId());
        User user = userRepository.findById(request.getUserId()).get();
        if (bookOptional.isEmpty()) {
            throw new NotFoundException("Book not found!");
        }

        String status = request.getReadingStatus();
        ReadingStatus enumValue = null;
        for (ReadingStatus readingStatus : ReadingStatus.values()) {
            if (readingStatus.getName().equals(status)) {
                enumValue = readingStatus;
                break;
            }
        }
        if (enumValue == null) {
            throw new IllegalArgumentException("Invalid Reading Status!");
        }
        Optional<ReadingBook> readingBookOptional = readingBookRepository.findByUserAndBook(user, bookOptional.get());
        ReadingBook readingBook;
        if (readingBookOptional.isEmpty()) {
            readingBook = ReadingBook.builder()
                    .book(bookOptional.get())
                    .user(user)
                    .readingStatus(enumValue)
                    .addedDate(LocalDate.now())
                    .build();

            if (enumValue == ReadingStatus.READ) {
                readingBook.setStartedDate(request.getStartedDate() == null ? LocalDate.now() : request.getStartedDate());
                readingBook.setFinishedDate(request.getFinishedDate() == null ? LocalDate.now() : request.getFinishedDate());
            } else if (enumValue == ReadingStatus.READING) {
                readingBook.setStartedDate(request.getStartedDate() == null ? LocalDate.now() : request.getStartedDate());
            }
        } else {
            readingBook = readingBookOptional.get();
            readingBook.setReadingStatus(enumValue);
            readingBook.setStartedDate(request.getStartedDate() == null ? readingBook.getStartedDate() : request.getStartedDate());
            readingBook.setFinishedDate(request.getFinishedDate() == null ? readingBook.getFinishedDate() : request.getFinishedDate());

            if (enumValue == ReadingStatus.READ) {
                if (readingBook.getFinishedDate() == null) {
                    readingBook.setFinishedDate(LocalDate.now());
                }
                if (readingBook.getStartedDate() == null) {
                    readingBook.setStartedDate(LocalDate.now());
                }
            }
        }
        readingBookRepository.save(readingBook);
    }

    public CommonResponse<?> getMyBookPagination(ReadingBookRequest request) {
        Optional<User> userOptional = userRepository.findById(request.getUserId());
        if (userOptional.isEmpty()) {
            throw new NotFoundException("User not exist!");
        }
        List<ReadingBook> readingBooks = readingBookRepository.findAllByUser(userOptional.get());
        List<ReadingBookResponse> readingBookResponses = new ArrayList<>();
        for (ReadingBook readingBook : readingBooks) {
            readingBookResponses.add(
                    ReadingBookResponse.builder()
                            .book(readingBook.getBook())
                            .readingStatus(readingBook.getReadingStatus().getName())
                            .readingProgress(readingBook.getReadingProgress())
                            .addedDateTime(LocalDate.now())
                            .startedDateTime(readingBook.getStartedDate())
                            .finishedDateTime(readingBook.getFinishedDate())
                            .build());
        }

        Integer pageIndex = request.getPageIndex();
        Integer pageSize = request.getPageSize();
        PaginationUtils<ReadingBookResponse> paginationUtils = new PaginationUtils<>();
        int pageNumber = paginationUtils.getPageNumber(readingBookResponses, pageSize);
        readingBookResponses = paginationUtils.searchData(readingBookResponses, pageIndex, pageSize);

        return CommonResponse.builder()
                .pageNumber(pageNumber)
                .data(readingBookResponses)
                .build();
    }

    public List<Integer> countMyBookList(Long userId) {
        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isEmpty()) {
            throw new NotFoundException("User not exist!");
        }
        List<ReadingBook> readingBooks = readingBookRepository.findAllByUser(userOptional.get());
        List<Integer> result = new ArrayList<>();
        int countRead = 0;
        int countReading = 0;
        int countWantToRead = 0;
        for (ReadingBook readingBook : readingBooks) {
            switch (readingBook.getReadingStatus().getName()) {
                case "To-read" -> countWantToRead++;
                case "Reading" -> countReading++;
                case "Read" -> countRead++;
            }
        }
        int countAllMyBook = countRead + countReading + countWantToRead;
        result.add(countAllMyBook);
        result.add(countRead);
        result.add(countReading);
        result.add(countWantToRead);
        return result;
    }

    @Transactional
    public void deleteBook(Long bookId) throws BadRequestException {
        Book book = bookRepository.findById(bookId).get();
        readingBookRepository.findAllByBook(book);
        bookOfChallengeRepository.findAllByBook(book);

        if (readingBookRepository.findAllByBook(book).isPresent() || bookOfChallengeRepository.findAllByBook(book).isPresent()) {
            throw new BadRequestException();
        }
        bookCustomRepository.deleteBookCategories(bookId);
        bookRepository.deleteById(bookId);
    }

    public List<CategoryResponse> getAllCategories() {
        return categoryService.getAllCategories();
    }

    public CommonResponse<?> findRandomBooks(Long userId) {
        List<Long> allIds = bookCustomRepository.getAllIds(userId);

        List<Long> randomNumbers = getRandomNumbers(allIds, 7);
        List<BookResponse> randomBooks = bookCustomRepository.findRandomBooks(randomNumbers, userId);

        return CommonResponse.builder()
                .data(randomBooks)
                .build();
    }

    public List<Long> getRandomNumbers(List<Long> source, int count) {
        if (count > source.size()) {
            throw new IllegalArgumentException("Số lượng số ngẫu nhiên yêu cầu vượt quá số lượng số trong danh sách.");
        }
        List<Long> shuffledNumbers = new ArrayList<>(source);
        Collections.shuffle(shuffledNumbers);
        return shuffledNumbers.subList(0, count);
    }

    public void removeMarkBook(Long bookId, Long userId) {
        bookCustomRepository.removeMarkBook(bookId, userId);
        removeRating(bookId, userId);
    }


    // RATING
    public void changeRating(ReviewRequest request, Long userId) {
        saveReview(request, userId);
        reviewCustomRepository.changeRating(request, userId);
        if (request.getReadingStatus() == null) {
            ReadingBookRequest readingBookRequest = ReadingBookRequest.builder()
                    .userId(userId)
                    .bookId(request.getBookId())
                    .readingStatus("Read")
                    .build();
            markBook(readingBookRequest);
        }
        calculateAvgRating(request.getBookId());
    }

    public void removeRating(Long bookId, Long userId) {
        reviewCustomRepository.removeRating(bookId, userId);
        calculateAvgRating(bookId);
    }

    public void calculateAvgRating(Long bookId) {
        Book book = bookRepository.findById(bookId).get();
        double totalRatingValue = 0;
        Integer totalOfRating = 0;
        List<AvgRatingResponse> avgRatingResponses = reviewCustomRepository.calculateAvgRating(bookId);
        for (AvgRatingResponse avg : avgRatingResponses) {
            totalRatingValue += avg.getRating() * avg.getCountOfRating();
            totalOfRating += avg.getCountOfRating();
        }
        double avgRating;
        if (totalOfRating == 0) {
            avgRating = 0;
        } else {
            avgRating = totalRatingValue / totalOfRating;
        }
        DecimalFormat decimalFormat = new DecimalFormat("#.##");
        book.setRating(Double.parseDouble(decimalFormat.format(avgRating)));
        bookRepository.save(book);
    }


    // REVIEW
    public List<ReviewResponse> getAllReviews(Long bookId, Long userId) {
        return reviewCustomRepository.getAllReviews(bookId, userId);
    }

    public void saveReview(ReviewRequest request, Long userId) {
        User user = userRepository.findById(userId).get();
        Book book = bookRepository.findById(request.getBookId()).get();
        Optional<Review> reviewOptional = reviewBookRepository.findByUserAndBook(user, book);
        Review review;
        if (reviewOptional.isEmpty()) {
            review = Review.builder()
                    .book(bookRepository.findById(request.getBookId()).get())
                    .user(userRepository.findById(userId).get())
                    .content(request.getContent())
                    .rating(0)
                    .build();
        } else {
            review = reviewOptional.get();
            review.setContent(request.getContent());
        }
        reviewBookRepository.save(review);
        if (request.getReadingStatus() == null || request.getReadingStatus().equals("Read")) {
            ReadingBookRequest readingBookRequest = ReadingBookRequest.builder()
                    .userId(userId)
                    .bookId(request.getBookId())
                    .readingStatus("Read")
                    .startedDate(request.getStartedDate() == null ? LocalDate.now() : request.getStartedDate())
                    .finishedDate(request.getFinishedDate() == null ? LocalDate.now() : request.getFinishedDate())
                    .build();
            markBook(readingBookRequest);
        }
    }

    @Transactional
    public void deleteReview(Long reviewId) {
        Optional<Review> reviewOptional = reviewBookRepository.findById(reviewId);
        if (reviewOptional.isEmpty()) {
            throw new NotFoundException("Not found review");
        }
        Review review = reviewOptional.get();
        Long bookId = review.getBook().getId();
        likeRepository.deleteAllByReview(review);
        commentRepository.deleteAllByReview(review);
        reviewBookRepository.delete(review);
        calculateAvgRating(bookId);
    }


    // COMMENT
    public CommentResponse createComment(CommentRequest request, Long userId) {
        User user = userRepository.findById(userId).get();
        Comment comment = Comment.builder()
                .review(reviewBookRepository.findById(request.getReviewId()).get())
                .user(user)
                .content(request.getContent())
                .build();
        commentRepository.save(comment);
        return CommentResponse.builder()
                .avatar(user.getAvatar())
                .name(user.getFullName())
                .contentOfComment(request.getContent())
                .commentDate(comment.getCreatedDateTime().toLocalDate())
                .build();
    }

    public void deleteComment(Long commentId) {
        commentRepository.deleteById(commentId);
    }


    // LIKE
    public void likeReview(Long reviewId, Long userCurrentId) {
        Review review = reviewBookRepository.findById(reviewId).get();
        User user = userRepository.findById(userCurrentId).get();
        Like like = Like.builder()
                .user(user)
                .review(review)
                .build();
        likeRepository.save(like);
    }

    public void unLikeReview(Long reviewId, Long userCurrentId) {
        User user = userRepository.findById(userCurrentId).get();
        Review review = reviewBookRepository.findById(reviewId).get();
        Optional<Like> likeOptional = likeRepository.findByUserAndReview(user, review);
        if (likeOptional.isEmpty()) {
            throw new NotFoundException("Not found Like");
        }
        likeRepository.delete(likeOptional.get());
    }
}

