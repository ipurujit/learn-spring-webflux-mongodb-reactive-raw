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
import ota.reactive.room.subscribers.MongoDBReactiveSubscriber.ObservableSubscriber;

import java.util.List;
import java.util.Optional;
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
    public Room save(Room room) throws Throwable {
        room.setId(new ObjectId());
        ObservableSubscriber subscriber = new ObservableSubscriber<>();
        collection.insertOne(room).subscribe(subscriber);
        subscriber.await();
        return room;
    }

    @Override
    public List<Room> saveAll(List<Room> rooms) throws Throwable {
        rooms.forEach(p -> p.setId(new ObjectId()));
        ObservableSubscriber subscriber = new ObservableSubscriber<>();
        collection.insertMany(rooms).subscribe(subscriber);
        subscriber.await();
        return rooms;
    }

    @Override
    public Optional<Room> findById(ObjectId roomId) throws Throwable {
        ObservableSubscriber<Room> subscriber = new ObservableSubscriber<>();
        collection.find(eq("_id", roomId)).subscribe(subscriber);
        subscriber.await();
        return subscriber.getReceived().isEmpty() ?
                Optional.empty() : Optional.of(subscriber.getReceived().get(0));
    }

    @Override
    public List<Room> findManyById(List<ObjectId> objectIds) {
        return null;
    }

    @Override
    public Page<Room> findAll(Pageable pageable) {
        return null;
    }

    @Override
    public Room update(Room person) throws Throwable {
        ObservableSubscriber subscriber = new ObservableSubscriber<>();
        collection.findOneAndReplace(
                eq("_id", person.getId()), person).subscribe(subscriber);
        subscriber.await();
        return subscriber.getReceived().isEmpty() ? null : (Room) subscriber.getReceived().get(0);
    }

    @Override
    public long deleteById(String id) throws Throwable {
        ObservableSubscriber subscriber = new ObservableSubscriber<>();
        collection.deleteOne(eq("_id", new ObjectId(id))).subscribe(subscriber);
        subscriber.await();
        return subscriber.getReceived().isEmpty() ? 1 : 0;
    }

    @Override
    public long deleteManyById(List<String> ids) {
        return 0;
    }

    private List<ObjectId> mapToObjectIds(List<String> ids) {
        return ids.stream().map(ObjectId::new).collect(Collectors.toList());
    }
}
