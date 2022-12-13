package ota.reactive.room.models;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class RoomTest {

    @Test
    void init() {
        Room r = new Room("a", "b");
    }


}