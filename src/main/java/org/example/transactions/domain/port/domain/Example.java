package org.example.transactions.domain.port.domain;

import java.io.Serializable;

public record Example(Long id, String name, String description) implements Serializable {
}