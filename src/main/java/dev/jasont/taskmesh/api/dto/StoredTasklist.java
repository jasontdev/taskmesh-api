package dev.jasont.taskmesh.api.dto;

import java.util.List;

public record StoredTasklist(Long id, String name, List<TasklistUser> users, List<StoredTask> tasks) {
}
