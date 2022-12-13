package ota.reactive.room.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

public interface BaseRepository<T, ID extends Serializable> {

    void init();

    // CREATE

    T save(T entity) throws Throwable;

    List<T> saveAll(List<T> persons) throws Throwable;

    // READ

    // long count();

    Optional<T> findById(ID id) throws Throwable;

    List<T> findManyById(List<ID> ids);

    Page<T> findAll(Pageable pageable);

    // UPDATE

    T update(T entity) throws Throwable;

//    long updateAll(List<T> persons);

    // DELETE

    long deleteById(String id) throws Throwable;

    long deleteManyById(List<String> ids);

}
