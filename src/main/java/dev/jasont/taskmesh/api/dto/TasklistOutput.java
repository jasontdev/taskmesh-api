package dev.jasont.taskmesh.api.dto;

import java.util.List;

public record TasklistOutput(Long id, String name, List<UserOutput> users) {
}
