package com.example.goodreads_finalproject.repository.custom;

import com.example.goodreads_finalproject.entity.Category;
import com.example.goodreads_finalproject.model.request.BookSearchRequest;
import com.example.goodreads_finalproject.model.response.BookResponse;
import com.example.goodreads_finalproject.model.response.BookSearchResponse;
import com.example.goodreads_finalproject.repository.BaseRepository;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class BookCustomRepository extends BaseRepository {
    public List<BookResponse> searchBook(BookSearchRequest request) {

        StringBuilder sql = new StringBuilder();
        HashMap<String, Object> parameters = new HashMap<>();
        sql.append("select ");
        sql.append("b.id, ");
        sql.append("b.image, ");
        sql.append("b.title, ");
        sql.append("GROUP_CONCAT(category.name SEPARATOR ', ') categories, ");
        sql.append("b.author, ");
        sql.append("b.description, ");
        sql.append("b.rating, ");
        sql.append("b.pages, ");
        sql.append("b.published, ");
        sql.append("b.buy_book buyBook ");
        sql.append("from books b ");
        sql.append("left join book_category AS book_cat ON b.id=book_cat.book_id ");
        sql.append("left join categories AS category ON book_cat.category_id=category.id ");
        sql.append("where 1=1");

        if (request.getTitle() != null && !request.getTitle().trim().equals("")) {
            sql.append(" and lower(b.title) like :title");
            parameters.put("title", "%" + request.getTitle().toLowerCase() + "%");
        }
        if (request.getAuthor() != null && !request.getAuthor().trim().equals("")) {
            sql.append(" and lower(b.author) like :author");
            parameters.put("author", "%" + request.getAuthor().toLowerCase() + "%");
        }
        if (request.getAll() != null && !request.getAll().trim().equals("")) {
            sql.append(" and (lower(b.author) like :author or lower(b.title) like :title)");
            parameters.put("author", "%" + request.getAll().toLowerCase() + "%");
            parameters.put("title", "%" + request.getAll().toLowerCase() + "%");
        }

        sql.append(" group by b.id");

        if (request.getCategory() != null && !request.getCategory().trim().equals("")) {
            sql.append(" having lower(GROUP_CONCAT(category.name SEPARATOR ', ')) like :category");
            parameters.put("category", "%" + request.getCategory().toLowerCase() + "%");
        }
       
        List<BookSearchResponse> bookSearchResponses = getNamedParameterJdbcTemplate().query(sql.toString(), parameters, BeanPropertyRowMapper.newInstance(BookSearchResponse.class));

        return convertToBookResponse(bookSearchResponses);
    }

    private Set<Category> convertCategory(String CategoryString) {
        Set<Category> categorySet = new HashSet<>();
        if (CategoryString != null && !CategoryString.isEmpty()) {
            String[] categoriesArray = CategoryString.split(", ");
            for (String c : categoriesArray) {
                Category category = new Category();
                category.setName(c);
                categorySet.add(category);
            }
        }
        return categorySet;
    }

    public void deleteBookCategories(Long bookId) {
        String sql = "delete from book_category where book_id=:bookId";
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("bookId", bookId);
        getNamedParameterJdbcTemplate().update(sql, parameters);
    }

    public void removeMarkBook(Long bookId, Long userId) {
        String sql = "DELETE FROM `reading_book` rd WHERE rd.book_id = :bookId AND rd.user_id = :userId";
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("bookId", bookId);
        parameters.put("userId", userId);
        getNamedParameterJdbcTemplate().update(sql, parameters);
    }

    public List<BookResponse> searchBookAuthen(BookSearchRequest request, Long userId) {

        HashMap<String, Object> parameters = new HashMap<>();
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT ");
        sql.append("ANY_VALUE(b.id) AS id, ");
        sql.append("ANY_VALUE(b.image) AS image, ");
        sql.append("ANY_VALUE(b.title) AS title, ");
        sql.append("GROUP_CONCAT(category.name SEPARATOR ', ') AS categories, ");
        sql.append("ANY_VALUE(b.author) AS author, ");
        sql.append("ANY_VALUE(b.description) AS description, ");
        sql.append("ANY_VALUE(b.rating) AS rating, ");
        sql.append("ANY_VALUE(b.pages), ");
        sql.append("ANY_VALUE(b.published) AS published, ");
        sql.append("ANY_VALUE(b.buy_book) AS buyBook, ");
        sql.append("COUNT(DISTINCT rv.id) AS countOfRatings, ");
        sql.append("ANY_VALUE(CASE ");
        sql.append("	WHEN r.reading_status = 'READ' THEN 'Read' ");
        sql.append("    WHEN r.reading_status = 'READING' THEN 'Reading' ");
        sql.append("    WHEN r.reading_status = 'WANT_TO_READ' THEN 'To-read' ");
        sql.append("    END) AS readingStatus ");
        sql.append("FROM books b ");
        sql.append("LEFT JOIN book_category AS book_cat ON b.id=book_cat.book_id ");
        sql.append("LEFT JOIN categories AS category ON book_cat.category_id=category.id ");
        sql.append("LEFT JOIN reviews AS rv ON b.id = rv.book_id AND rv.rating > 0 ");
        sql.append("LEFT JOIN reading_book r ON b.id = r.book_id AND r.user_id = :userId ");
        sql.append("WHERE 1=1 ");

        parameters.put("userId", userId);

        if (request.getSearch() != null && !request.getSearch().trim().equals("")) {
            sql.append(" and lower(b.author) like :search or lower(b.title) like :search");
            parameters.put("search", "%" + request.getSearch().toLowerCase() + "%");
        }

        sql.append(" GROUP BY b.id");

        List<BookSearchResponse> bookSearchResponses = getNamedParameterJdbcTemplate().query(sql.toString(), parameters, BeanPropertyRowMapper.newInstance(BookSearchResponse.class));

        return convertToBookResponse(bookSearchResponses);
    }

    public List<Long> getAllIds(Long userId) {
        String sql;
        Map<String, Object> parameters = new HashMap<>();
        if (userId == null) {
            sql = "Select b.id FROM books b";
        } else {
            sql = "SELECT" +
                    "    b.id " +
                    "FROM" +
                    "    books b " +
                    "LEFT JOIN reading_book r ON " +
                    "    b.id = r.book_id AND r.user_id = :userId " +
                    "WHERE " +
                    "    r.reading_status IS NULL ";

            parameters.put("userId", userId);
        }
        return getNamedParameterJdbcTemplate().queryForList(sql, parameters, Long.class);
//        return getNamedParameterJdbcTemplate().query(sql, parameters, BeanPropertyRowMapper.newInstance(Long.class));
    }

    public List<BookResponse> findRandomBooks(List<Long> randomNumbers, Long userId) {
        HashMap<String, Object> parameters = new HashMap<>();
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT ");
        sql.append("ANY_VALUE(b.id) AS id, ");
        sql.append("ANY_VALUE(b.image) AS image, ");
        sql.append("ANY_VALUE(b.title) AS title, ");
        sql.append("GROUP_CONCAT(category.name SEPARATOR ', ') AS categories, ");
        sql.append("ANY_VALUE(b.author) AS author, ");
        sql.append("ANY_VALUE(b.description) AS description, ");
        sql.append("ANY_VALUE(b.rating) AS rating, ");
        sql.append("ANY_VALUE(b.published) AS published, ");
        sql.append("ANY_VALUE(b.buy_book) AS buyBook, ");
        sql.append("COUNT(DISTINCT rv.id) AS countOfRatings, ");
        sql.append("ANY_VALUE(CASE ");
        sql.append("	WHEN r.reading_status = 'READ' THEN 'Read' ");
        sql.append("    WHEN r.reading_status = 'READING' THEN 'Reading' ");
        sql.append("    WHEN r.reading_status = 'WANT_TO_READ' THEN 'To-read' ");
        sql.append("    ELSE NULL END) AS readingStatus ");
        sql.append("FROM books b ");
        sql.append("LEFT JOIN book_category AS book_cat ON b.id=book_cat.book_id ");
        sql.append("LEFT JOIN categories AS category ON book_cat.category_id=category.id ");
        sql.append("LEFT JOIN reading_book r ON b.id = r.book_id ");

        sql.append("AND r.user_Id = :userId ");
        parameters.put("userId", userId);

        sql.append("LEFT JOIN reviews AS rv ON b.id = rv.book_id AND rv.rating > 0 ");
        sql.append("WHERE b.id in (:randomIds) ");
        if (userId != null) {
            sql.append("And r.reading_status IS NULL ");
        }
        sql.append("GROUP BY b.id ");
        parameters.put("randomIds", randomNumbers);

        List<BookSearchResponse> bookSearchResponses = getNamedParameterJdbcTemplate().query(sql.toString(), parameters, BeanPropertyRowMapper.newInstance(BookSearchResponse.class));

        return convertToBookResponse(bookSearchResponses);
    }

    public BookResponse findByBookIdAndUserId(Long bookId, Long userId) {
        HashMap<String, Object> parameters = new HashMap<>();
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT ");
        sql.append("ANY_VALUE(b.id) AS id, ");
        sql.append("ANY_VALUE(b.image) AS image, ");
        sql.append("ANY_VALUE(b.title) AS title, ");
        sql.append("GROUP_CONCAT(category.name SEPARATOR ', ') AS categories, ");
        sql.append("ANY_VALUE(b.author) AS author, ");
        sql.append("ANY_VALUE(b.pages) AS pages, ");
        sql.append("ANY_VALUE(b.description) AS description, ");
        sql.append("ANY_VALUE(b.rating) AS rating, ");
        sql.append("ANY_VALUE(b.published) AS published, ");
        sql.append("ANY_VALUE(b.buy_book) AS buyBook, ");
        sql.append("ANY_VALUE(rv.rating) AS ratingDetail, ");
        sql.append("ANY_VALUE(rv.content) AS content, ");

        sql.append("ANY_VALUE(CASE ");
        sql.append("	WHEN r.reading_status = 'READ' THEN 'Read' ");
        sql.append("    WHEN r.reading_status = 'READING' THEN 'Reading' ");
        sql.append("    WHEN r.reading_status = 'WANT_TO_READ' THEN 'To-read' ");
        sql.append("    ELSE NULL END) AS readingStatus ");
        sql.append("FROM books b ");
        sql.append("LEFT JOIN book_category AS book_cat ON b.id=book_cat.book_id ");
        sql.append("LEFT JOIN categories AS category ON book_cat.category_id=category.id ");
        sql.append("LEFT JOIN reviews rv ON b.id = rv.book_id AND rv.user_Id = :userId ");
        sql.append("LEFT JOIN reading_book r ON b.id = r.book_id AND r.user_Id = :userId ");
        sql.append("where b.id = :bookId ");

        parameters.put("userId", userId);
        parameters.put("bookId", bookId);

        List<BookSearchResponse> bookSearchResponses = getNamedParameterJdbcTemplate().query(sql.toString(), parameters, BeanPropertyRowMapper.newInstance(BookSearchResponse.class));
        List<BookResponse> bookResponses = convertToBookResponse(bookSearchResponses);
        BookResponse result = bookResponses.get(0);
        result.setCountOfRatings(countOfRatings(bookId));
        result.setCountOfReviews(countOfReviews(bookId));
        return result;
    }

    private Integer countOfRatings(Long bookId) {
        HashMap<String, Object> parameters = new HashMap<>();
        String sql = "SELECT COUNT(rv.id) FROM reviews rv WHERE rv.rating>0 AND rv.book_id =:bookId";
        parameters.put("bookId", bookId);
        return getNamedParameterJdbcTemplate().queryForObject(sql, parameters, Integer.class);
    }

    private Integer countOfReviews(Long bookId) {
        HashMap<String, Object> parameters = new HashMap<>();
        String sql = "SELECT COUNT(rv.id) FROM reviews rv WHERE TRIM(rv.content) <> '' AND rv.book_id = :bookId";
        parameters.put("bookId", bookId);
        return getNamedParameterJdbcTemplate().queryForObject(sql, parameters, Integer.class);
    }

    private List<BookResponse> convertToBookResponse(List<BookSearchResponse> bookSearchResponses) {
        List<BookResponse> bookResponseList = new ArrayList<>();
        bookSearchResponses.forEach(bookSearchResponse -> {
            BookResponse bookResponse = BookResponse.builder()
                    .id(bookSearchResponse.getId())
                    .image(bookSearchResponse.getImage())
                    .title(bookSearchResponse.getTitle())
                    .author(bookSearchResponse.getAuthor())
                    .categories(convertCategory(bookSearchResponse.getCategories()))
                    .description(bookSearchResponse.getDescription())
                    .buyBook(bookSearchResponse.getBuyBook())
                    .published(bookSearchResponse.getPublished())
                    .rating(bookSearchResponse.getRating())
                    .ratingDetail(bookSearchResponse.getRatingDetail() == null ? 0 : Double.parseDouble(bookSearchResponse.getRatingDetail()))
                    .pages(bookSearchResponse.getPages())
                    .readingStatus(bookSearchResponse.getReadingStatus())
                    .countOfRatings(bookSearchResponse.getCountOfRatings())
                    .countOfReviews(bookSearchResponse.getCountOfReview())
                    .content(bookSearchResponse.getContent())
                    .build();
            bookResponseList.add(bookResponse);
        });
        return bookResponseList;
    }

}
