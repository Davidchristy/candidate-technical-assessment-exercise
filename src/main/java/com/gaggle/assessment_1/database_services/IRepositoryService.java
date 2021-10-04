package com.gaggle.assessment_1.database_services;

import java.util.ArrayList;

import com.gaggle.assessment_1.exceptions.RepositoryException;
import com.gaggle.assessment_1.exceptions.EntryNotFoundException;


/**
 * A generalized Interface for any and all repositories that might be 
 * connected to this service.
 * 
 * Note: This is currently a very slim version of this interface. 
 * It works for the problem at hand, but if a "Next Version" were
 * to be created I would want it to include general CRUD commands.
 * (Create, Read, Update, Delete) So that every data store we connect 
 * to can do the same basic commands. 
 */
public interface IRepositoryService{    
    /**
     * This method adds a new name to the repository of names, 
     * as an abstract method the exact design will change with
     * the different implementations.
     * 
     * This also makes the assumition all implementations of this 
     * method will allow the same name to be placed into the 
     * repository: either allowing, hiding the error, or throwing 
     * general error. 
     *  
     * This abstracts the various repository errors into a single
     * 'RepositoryException' so they can all be caught by the same 
     * handler higher in the code.
     * 
     * @param name Name of the contact
     * @throws RepositoryException Generalized exception for all Repositories.
     */
    void addContact(String name) throws RepositoryException;

    /**
     * This method returns the name of the contact paired with a unique ID. 
     * 
     * @param id                        The unique ID
     * @return                          The Name paired with the The unique ID
     * @throws EntryNotFoundException   Thrown when id is not found in repository
     * @throws RepositoryException      Generalized exception for all repositories
     */
    String getNameByID(Integer id) throws EntryNotFoundException, RepositoryException;

    /**
     * This method returns a list of names containing the searchQuery as a substring.
     * Cases are ignored and results are ordered alphabetically. 
     * 
     * If maxNumOfResults less than 0 then all results are found. 
     * 
     * @see searchBasedOnName
     * 
     * @param searchQuery               The Substring that will be searched for in the repository
     * @param maxNumOfResults           The number of results we want back
     * @return                          List of names containing the searchQuery as a substring
     * @throws RepositoryException      Generalized exception for all repositories
     */
    ArrayList<String> searchBasedOnName(String searchQuery, Integer maxNumOfResults) throws RepositoryException;

}
