package com.gaggle.assessment_1.exceptions;

/**
 * A generalized exception for any repository we end up using. 
 * 
 * For example, we can have a text file, an sqlite connection, MySQL or 
 * many more data stores all with different errors. This simplifed the 
 * errors into the same type of error to be used downstream.
 */
public class RepositoryException extends Exception{
    public RepositoryException(String errorMessage) {
        super(errorMessage);
    }
}
