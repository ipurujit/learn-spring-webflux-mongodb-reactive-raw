package ota.reactive.room.models;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.bson.types.ObjectId;

@Getter
@Setter
@NoArgsConstructor
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Room {

    @JsonSerialize(using = ToStringSerializer.class)
    private ObjectId id;

    private String number;
    private String description;

    public Room(String number, String description) {
        this.number = number;
        this.description = description;
    }

}
