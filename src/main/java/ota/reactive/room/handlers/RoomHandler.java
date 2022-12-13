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
                .doOnNext(room -> {
                    try {
                        System.out.println(room);
                        roomRepository.save(room);
                    } catch (Throwable ex) {
                        System.out.println("\n\n\nFAILED TO SAVE\n\n\n");
                        System.out.println(ex.getMessage());
                    }
                })
                .flatMap(room -> ServerResponse.ok().contentType(MediaType.APPLICATION_JSON)
                        .body(BodyInserters.fromValue(room)));
    }

    public Mono<ServerResponse> query(ServerRequest request) {
        Optional<String> id = request.queryParam("id");
        Room r = new Room();
        try {
            if (!id.isPresent()) {
                throw new Exception("Not found");
            }
            r = roomRepository.findById(new ObjectId(id.get())).orElseThrow();
        } catch (Throwable ex) {
            System.out.println("\n\n\nFAILED TO FIND\n\n\n");
            System.out.println(ex.getMessage());
        }
        return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(r));
    }

    public Mono<ServerResponse> change(ServerRequest request) {
        return request.bodyToMono(Room.class)
                .doOnNext(room -> {
                    try {
                        System.out.println(room);
                        roomRepository.update(room);
                    } catch (Throwable ex) {
                        System.out.println("\n\n\nFAILED TO UPDATE\n\n\n");
                        System.out.println(ex.getMessage());
                    }
                })
                .flatMap(room -> ServerResponse.ok().contentType(MediaType.APPLICATION_JSON)
                        .body(BodyInserters.fromValue(room)));
    }

    public Mono<ServerResponse> bye(ServerRequest request) {
        Optional<String> id = request.queryParam("id");
        long r = -1;
        try {
            if (id.isPresent()) {
                r = roomRepository.deleteById(id.get());
            }
        } catch (Throwable ex) {
            System.out.println("\n\n\nFAILED TO DELETE\n\n\n");
            System.out.println(ex.getMessage());
        }
        return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(Map.of("val", r)));
    }
}
