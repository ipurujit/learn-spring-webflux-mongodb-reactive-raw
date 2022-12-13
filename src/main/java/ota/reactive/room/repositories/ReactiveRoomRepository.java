package ota.reactive.room.repositories;

import org.bson.types.ObjectId;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;

import ota.reactive.room.models.Room;

public interface ReactiveRoomRepository extends ReactiveCrudRepository<Room, ObjectId> {


}
