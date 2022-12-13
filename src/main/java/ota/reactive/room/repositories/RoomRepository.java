package ota.reactive.room.repositories;

import com.mongodb.TransactionOptions;
import com.mongodb.reactivestreams.client.MongoCollection;
import com.mongodb.reactivestreams.client.MongoDatabase;
import jakarta.annotation.PostConstruct;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import ota.reactive.room.models.Room;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.stream.Collectors;

import static com.mongodb.client.model.Filters.eq;


@Repository
public class RoomRepository implements BaseRepository<Room, ObjectId> {

    @Autowired
    private MongoDatabase mongoDatabase;

    @Autowired
    private TransactionOptions txnOptions;

    private static final String COLLECTION_NAME = "rooms";

    private MongoCollection<Room> collection;

    @PostConstruct
    @Override
    public void init() {
        collection = mongoDatabase.getCollection(COLLECTION_NAME, Room.class);
    }

    @Override
    public Mono<Room> save(Room room) {
        room.setId(new ObjectId());
        return Mono.from(collection.insertOne(room)).map(insertOneResult -> {
            System.out.println(insertOneResult);
            return room;
        });
    }

    @Override
    public Flux<Room> saveAll(List<Room> rooms) {
        rooms.forEach(p -> p.setId(new ObjectId()));
        return Flux.from(collection.insertMany(rooms))
                .map(insertManyResult -> {
                    System.out.println("how many times? \n\n"+insertManyResult);
                    System.out.println(rooms);
                    return null;
                });
    }

    @Override
    public Mono<Room> findById(ObjectId roomId) {
        return Mono.from(collection.find(eq("_id", roomId)));
    }

    @Override
    public Flux<Room> findManyById(List<ObjectId> objectIds) {
        return Flux.empty();
    }

    @Override
    public Page<Room> findAll(Pageable pageable) {
        return Page.empty();
    }

    @Override
    public Mono<Room> update(Room person) {
        return Mono.from(collection.findOneAndReplace(
                eq("_id", person.getId()), person)
        ).map(room -> {
            System.out.println(room);
            System.out.println(person);
            return person;
        });
    }

    @Override
    public Mono<Long> deleteById(ObjectId id) {
        return Mono.from(collection.deleteOne(eq("_id", id)))
                .map(deleteResult -> {
            System.out.println(deleteResult);
            return deleteResult.getDeletedCount();
        });
    }

    @Override
    public Mono<Long> deleteManyById(List<ObjectId> ids) {
        return Mono.just(0L);
    }
}
