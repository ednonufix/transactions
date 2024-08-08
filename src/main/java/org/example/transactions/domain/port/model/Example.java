package org.example.transactions.domain.port.model;

import java.io.Serializable;

public record Example(Long id, String name, String description) implements Serializable {
}