package ota.reactive.room.handlers;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import ota.reactive.room.models.Room;

import ota.reactive.room.repositories.ReactiveRoomRepository;
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
    private ReactiveRoomRepository roomRepository;

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
                .flatMap(room -> {
                    try {
                        System.out.println(room);
                        return roomRepository.save(room);
                    } catch (Throwable ex) {
                        System.out.println("\n\n\nFAILED TO SAVE\n\n\n");
                        System.out.println(ex.getMessage());
                        return null;
                    }
                })
                .flatMap(room -> ServerResponse.ok().contentType(MediaType.APPLICATION_JSON)
                        .body(BodyInserters.fromValue(room)));
    }

    public Mono<ServerResponse> query(ServerRequest request) {
        Optional<String> id = request.queryParam("id");
        try {
            if (id.isEmpty()) {
                throw new Exception("Not found");
            }
            return roomRepository.findById(new ObjectId(id.get()))
                    .flatMap(room -> ServerResponse.ok().contentType(MediaType.APPLICATION_JSON)
                            .body(BodyInserters.fromValue(room)));
        } catch (Exception ex) {
            System.out.println("\n\n\nFAILED TO FIND\n\n\n");
            System.out.println(ex.getMessage());
        }
        return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(new Room()));
    }

    public Mono<ServerResponse> change(ServerRequest request) {
        return request.bodyToMono(Room.class)
                .flatMap(room -> {
                    try {
                        System.out.println(room);
                        return roomRepository.existsById(room.getId())
                                        .flatMap(val -> {
                                            System.out.println("FOUND DATA =="+val);
                                            if (val) {
                                                return roomRepository.save(room);
                                            }
                                            return Mono.empty();
                                        });
                    } catch (Exception ex) {
                        System.out.println("\n\n\nFAILED TO UPDATE\n\n\n");
                        System.out.println(ex.getMessage());
                    }
                    return Mono.empty();
                })
                .flatMap(room -> ServerResponse.ok().contentType(MediaType.APPLICATION_JSON)
                        .body(BodyInserters.fromValue(room)));
    }

    public Mono<ServerResponse> bye(ServerRequest request) {
        Optional<String> id = request.queryParam("id");
        try {
            if (id.isPresent()) {
                return roomRepository.deleteById(new ObjectId(id.get()))
                        .flatMap(delVal -> ServerResponse.ok().contentType(MediaType.APPLICATION_JSON)
                                .body(BodyInserters.fromValue(Map.of("val", true))));
            }
        } catch (Exception ex) {
            System.out.println("\n\n\nFAILED TO DELETE\n\n\n");
            System.out.println(ex.getMessage());
        }
        return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(Map.of("val", false)));
    }
}
