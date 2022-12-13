package ota.reactive.room.models;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@NoArgsConstructor
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
@Document(collection = "rooms")
public class Room {

    @JsonSerialize(using = ToStringSerializer.class)
    @Id
    private ObjectId id;

    private String number;
    private String description;

    public Room(String number, String description) {
        this.number = number;
        this.description = description;
    }

}
