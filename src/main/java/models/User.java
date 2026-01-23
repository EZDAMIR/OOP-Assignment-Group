package models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class User {
    private int id;
    private String fullName;
    private String phone;

    @Override
    public String toString() {
        return id + ": " + fullName + " (" + phone + ")";
    }
}
