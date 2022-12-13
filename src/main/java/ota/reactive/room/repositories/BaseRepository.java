package ota.reactive.room.repositories;

import org.bson.types.ObjectId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

public interface BaseRepository<T, ID extends Serializable> {

    void init();

    // CREATE

    Mono<T> save(T entity);

    Flux<T> saveAll(List<T> persons);

    // READ

    // long count();

    Mono<T> findById(ID id);

    Flux<T> findManyById(List<ID> ids);

    Page<T> findAll(Pageable pageable);

    // UPDATE

    Mono<T> update(T entity);

//    long updateAll(List<T> persons);

    // DELETE

    Mono<Long> deleteById(ID id);

    Mono<Long> deleteManyById(List<ID> ids);

}
