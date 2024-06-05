package com.example.goodreads_finalproject.repository;


import com.example.goodreads_finalproject.entity.District;
import com.example.goodreads_finalproject.entity.Province;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface DistrictRepository extends JpaRepository<District, String> {

    Optional<List<District>> findAllByProvinceCode(String provinceCode);
}