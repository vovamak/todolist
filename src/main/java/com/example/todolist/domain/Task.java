package com.example.todolist.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@Entity
@Table(name = "task")
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Description is mandatory")
    @Size(max = 100, message = "Description must be less than 100 characters")
    @Column(name = "description", length = 100, nullable = false)
    private String description;

    @NotNull
    @Enumerated(EnumType.STRING)
    private Status status;
}
