package com.example.goodreads_finalproject.repository.custom;

import com.example.goodreads_finalproject.entity.Category;
import com.example.goodreads_finalproject.model.request.UserSearchRequest;
import com.example.goodreads_finalproject.model.response.UserResponse;
import com.example.goodreads_finalproject.repository.BaseRepository;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class UserCustomRepository extends BaseRepository {
    public List<UserResponse> searchUser(UserSearchRequest request) {

        StringBuilder sql = new StringBuilder();
        HashMap<String, Object> parameters = new HashMap<>();
        sql.append("select ");
        sql.append("b.id, ");
        sql.append("b.email, ");
        sql.append("b.activated, ");
        sql.append("b.locked, ");
        sql.append("r.name AS roles, ");
        sql.append("b.avatar, ");
        sql.append("b.full_name, ");
        sql.append("CASE WHEN b.gender = 'MALE' THEN 'Male' WHEN b.gender = 'FEMALE' THEN 'Female' ELSE 'Undefined' END AS gender, ");
        sql.append("b.dob, ");
        sql.append("b.phone, ");
        sql.append("b.street, ");
        sql.append("w.name AS ward, ");
        sql.append("d.name AS district, ");
        sql.append("p.name AS province ");
        sql.append("from users b ");
        sql.append("left join user_role AS ur ON b.id=ur.user_id ");
        sql.append("left join roles AS r ON ur.role_id=r.id ");
        sql.append("left join wards AS w ON b.ward_code = w.code ");
        sql.append("left join districts AS d ON w.district_code = d.code ");
        sql.append("left join provinces AS p ON d.province_code = p.code ");

        sql.append("where 1=1");

        if (request.getEmail() != null && !request.getEmail().trim().equals("")) {
            sql.append(" and lower(b.email) like :email");
            parameters.put("email", "%" + request.getEmail().toLowerCase() + "%");
        }
        if (request.getFullName() != null && !request.getFullName().trim().equals("")) {
            sql.append(" and lower(b.full_name) like :fullName");
            parameters.put("fullName", "%" + request.getFullName().toLowerCase() + "%");
        }

        return getNamedParameterJdbcTemplate().query(sql.toString(), parameters, BeanPropertyRowMapper.newInstance(UserResponse.class));
    }

    public UserResponse getUserById(Long userId) {

        StringBuilder sql = new StringBuilder();
        HashMap<String, Object> parameters = new HashMap<>();
        sql.append("select ");
        sql.append("b.id, ");
        sql.append("b.email, ");
        sql.append("b.activated, ");
        sql.append("b.locked, ");
        sql.append("r.name AS roles, ");
        sql.append("b.avatar, ");
        sql.append("b.full_name, ");
        sql.append("CASE WHEN b.gender = 'MALE' THEN 'Male' WHEN b.gender = 'FEMALE' THEN 'Female' ELSE 'Undefined' END AS gender, ");
        sql.append("b.dob, ");
        sql.append("b.phone, ");
        sql.append("b.about, ");
        sql.append("b.street, ");
        sql.append("w.name AS ward, ");
        sql.append("d.name AS district, ");
        sql.append("p.name AS province, ");
        sql.append("w.full_name AS wardFullName, ");
        sql.append("d.full_name AS districtFullName, ");
        sql.append("p.full_name AS provinceFullName ");
        sql.append("from users b ");
        sql.append("left join user_role AS ur ON b.id=ur.user_id ");
        sql.append("left join roles AS r ON ur.role_id=r.id ");
        sql.append("left join wards AS w ON b.ward_code = w.code ");
        sql.append("left join districts AS d ON w.district_code = d.code ");
        sql.append("left join provinces AS p ON d.province_code = p.code ");

        sql.append("where 1=1");

        if (userId != null) {
            sql.append(" and b.id = :id");
            parameters.put("id", userId);
        }

        List<UserResponse> users = getNamedParameterJdbcTemplate().query(sql.toString(), parameters, BeanPropertyRowMapper.newInstance(UserResponse.class));
        if (!users.isEmpty()) {
            return users.get(0);
        } else {
            return null;
        }
    }

}
