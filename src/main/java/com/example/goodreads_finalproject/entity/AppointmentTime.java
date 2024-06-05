package com.example.goodreads_finalproject.entity;

import com.example.goodreads_finalproject.statics.AppointmentTimes;
import com.example.goodreads_finalproject.statics.Roles;

import javax.persistence.*;
import java.time.LocalDate;

public class AppointmentTime {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    AppointmentTimes appointmentTimes;
}
