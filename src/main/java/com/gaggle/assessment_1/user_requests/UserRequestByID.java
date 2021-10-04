package com.gaggle.assessment_1.user_requests;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;


/**
 * A simple DAO for the POST JSON resquest a user sends with only the id.
 * 
 */
@NoArgsConstructor
@Getter
@Setter
public class UserRequestByID {
    private Integer id;     
}
