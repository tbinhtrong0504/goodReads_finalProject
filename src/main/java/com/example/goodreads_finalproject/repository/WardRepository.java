package com.example.goodreads_finalproject.repository;


import com.example.goodreads_finalproject.entity.Province;
import com.example.goodreads_finalproject.entity.Ward;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface WardRepository extends JpaRepository<Ward, String> {

    Optional<List<Ward>> findAllByDistrictCode(String districtCode);

    Ward findByCode(String wardCode);
}