package ota.reactive.room.repositories;

import org.bson.types.ObjectId;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

import org.springframework.stereotype.Repository;
import ota.reactive.room.models.Room;

@Repository
public interface ReactiveRoomRepository extends ReactiveMongoRepository<Room, ObjectId> {
}
