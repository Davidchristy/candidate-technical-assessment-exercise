package com.gaggle.assessment_1.user_requests;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * A simple DAO used by searchContactNames. 
 * 
 * By default, "maxNames" is not required and will default to a negative number, 
 * meaning all names are found.
 * By allowing the end user to limit the number of names in the Rest call 
 * we save resources if they only need N results results (For example, a suggestion list)  
 * 
 * @see searchContactNames
 */
@NoArgsConstructor
@Getter
@Setter
public class UserRequestByName {
    private String name;     
    
    // If maxNames is less than or equal to 0, all names are found. We want this the be the default behavior 
    private Integer maxNames = -1;
}