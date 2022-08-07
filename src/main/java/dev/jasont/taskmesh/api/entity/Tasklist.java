package dev.jasont.taskmesh.api.repository;

import dev.jasont.taskmesh.api.entity.Task;
import lombok.Data;

import javax.persistence.*;
import java.util.Set;

@Data
@Entity
public class Tasklist {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;
    private Boolean isComplate;

    @OneToMany
    private Set<Task> tasks;
}
