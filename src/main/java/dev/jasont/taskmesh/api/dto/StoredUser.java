package dev.jasont.taskmesh.api.dto;

import java.util.List;

public record StoredUser(String id, List<StoredTasklist> tasklists) {
    
}
