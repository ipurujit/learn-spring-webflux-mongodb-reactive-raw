package ota.reactive.room.handlers;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import ota.reactive.room.models.Room;

import ota.reactive.room.repositories.RoomRepository;
import reactor.core.publisher.Mono;

import java.util.Map;
import java.util.Optional;

@Component
public class RoomHandler {
    /**
     * Sample request body
     * {
     *     "id": "6396faf18129087924ed012e",
     *     "number": "211",
     *     "description": "Presidential suite"
     * }
     */

    @Autowired
    private RoomRepository roomRepository;

    public Mono<ServerResponse> helloWorld(ServerRequest request) {
        Room r = new Room("211", "Presidential suite");
        try {
            roomRepository.save(r);
        } catch (Throwable e) {
            System.out.println("\n\n\nFAILED TO SAVE\n\n\n");
            System.out.println(e.getMessage());
        }
        return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(r));
    }


    public Mono<ServerResponse> hello(ServerRequest request) {
        return request.bodyToMono(Room.class)
                .flatMap(roomRepository::save)
                .flatMap(room -> ServerResponse.ok().contentType(MediaType.APPLICATION_JSON)
                        .body(BodyInserters.fromValue(room)));
    }

    public Mono<ServerResponse> query(ServerRequest request) {
        return Mono.just(request.queryParam("id"))
                .flatMap(optionalId -> {
                    if (optionalId.isPresent() && ObjectId.isValid(optionalId.get())) {
                        return roomRepository.findById(new ObjectId(optionalId.get()));
                    }
                    return Mono.empty();
                }).flatMap(room -> ServerResponse.ok().contentType(MediaType.APPLICATION_JSON)
                        .body(BodyInserters.fromValue(room)));
    }

    public Mono<ServerResponse> change(ServerRequest request) {
        return request.bodyToMono(Room.class)
                .flatMap(room -> roomRepository.update(room))
                .flatMap(room -> ServerResponse.ok().contentType(MediaType.APPLICATION_JSON)
                        .body(BodyInserters.fromValue(room)));
    }

    public Mono<ServerResponse> bye(ServerRequest request) {
        return Mono.just(request.queryParam("id"))
                .flatMap(optionalId -> {
                    if (optionalId.isPresent() && ObjectId.isValid(optionalId.get())) {
                        return roomRepository.deleteById(new ObjectId(optionalId.get()));
                    }
                    return Mono.just(0L);
                }).flatMap(delCount -> ServerResponse.ok().contentType(MediaType.APPLICATION_JSON)
                        .body(BodyInserters.fromValue(Map.of("val", delCount))));
    }
}
