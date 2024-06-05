package com.example.goodreads_finalproject.repository.custom;

import com.example.goodreads_finalproject.model.request.ReviewRequest;
import com.example.goodreads_finalproject.model.response.AvgRatingResponse;
import com.example.goodreads_finalproject.model.response.ChallengeResponse;
import com.example.goodreads_finalproject.model.response.CommentResponse;
import com.example.goodreads_finalproject.model.response.ReviewResponse;
import com.example.goodreads_finalproject.repository.BaseRepository;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class ChallengeCustomRepository extends BaseRepository {


    public ChallengeResponse getChallengeDetail(Long userId) {
            Map<String, Object> parameters = new HashMap<>();
            StringBuilder sql = new StringBuilder();

            sql.append("SELECT ");
            sql.append("ch.id AS challengeId, ");
            sql.append("ch.number_of_book AS numberOfBook, ");
            sql.append("ch.started_date AS startedDate, ");
            sql.append("ch.end_date AS endDate, ");
            sql.append("COUNT(r.id) AS booksRead ");
            sql.append("FROM challenges ch ");
            sql.append("LEFT JOIN reading_book r ON ");
            sql.append("    ch.user_id = r.user_id AND r.reading_status = 'READ' AND r.finished_date <= ch.end_date AND r.finished_date >= ch.started_date ");
            sql.append("    AND ch.challenge_status = 'HAPPENING' ");
            sql.append("WHERE ch.user_id = :userId ");
            sql.append("GROUP BY ch.id ");

            parameters.put("userId", userId);

            List<ChallengeResponse> challengeResponses = getNamedParameterJdbcTemplate().query(sql.toString(), parameters, BeanPropertyRowMapper.newInstance(ChallengeResponse.class));
            if (challengeResponses.isEmpty()) {
                return null;
            } else {
                return challengeResponses.get(0);
            }

    }


}
