package com.stream.app.entities;


import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;


@Entity
@Table(name = "yt_courses")
public class Course {

    @Id
    private String id;

    private String title;

}
