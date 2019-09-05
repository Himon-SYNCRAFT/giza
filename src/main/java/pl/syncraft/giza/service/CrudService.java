package pl.syncraft.giza.service;

import lombok.NonNull;

import java.util.Optional;

public interface CrudService<T> {
    Optional<T> get(int id);
    void save(@NonNull T t);
    void delete(int id);
}
