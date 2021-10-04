package com.gaggle.assessment_1.database_services;

import java.util.ArrayList;

import com.gaggle.assessment_1.exceptions.EntryNotFoundException;
import com.gaggle.assessment_1.exceptions.RepositoryException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * An SQLite implementation of the IRepositoryService.
 * 
 * Special attention needs to be given to the database file since in this implementation
 * a file called "contacts.db" gets created in the root of this project folder. 
 * 
 * Make sure you have write access and available space. 
 * 
 */
@Repository
public class SQLiteDatabaseService implements IRepositoryService{

    private static final Logger LOGGER=LoggerFactory.getLogger(SQLiteDatabaseService.class);

    /*  
    NOTE: Leaving vauge for now assuming public facing API, but if internal API more 
    infomation can be shown here without information leakage.
    
    Might also set up a "debug mode" that shows the information but by default only shows this vauge error. 
    All depends on the final use-case of the Service.
    */
    private static final String INTERNAL_ERROR_STRING = "There has been an internal error, please contact the system administrator for more information";
    
    private Connection conn = null;
    
    public SQLiteDatabaseService() throws SQLException, RepositoryException{
        /* This is hard coded for now due to scope of project. However if this were
        Were to go live I'd make a config file with the database name. */
        String url = "jdbc:sqlite:contacts.db";
        
        this.conn = DriverManager.getConnection(url);
        
        LOGGER.info("Connection to SQLite has been established.");

        createTableIfNotPresent();             
    }

    /**
     * To help with testing this public method is here to quickly reset the database. 
     * If this Service were to go live, even internally, I would remove this function.
     * Probably keeping the functionality in a python script editing the dev database.
     */
    public void resetDB(){
        String dropTableSql = "DROP TABLE contact;";
        try (Statement stmt = this.conn.createStatement()) {
            // create a new table
            stmt.execute(dropTableSql);            
        } catch (SQLException e) {
            LOGGER.error(e.getMessage());
        }
        try {
            createTableIfNotPresent();
        } catch (RepositoryException e) {
            e.printStackTrace();
        }
    }

    /**
     * Small helper function to create the table if not already there.
     * @throws RepositoryException
     */
    private void createTableIfNotPresent() throws RepositoryException{
        String createTableSql = "CREATE TABLE IF NOT EXISTS contact (\n"
                + "id integer PRIMARY KEY,\n"
                + "name text NOT NULL\n"
                + ");";
        try (Statement stmt = this.conn.createStatement()) {
            // create a new table
            stmt.execute(createTableSql);
        } catch (SQLException e) {
            LOGGER.error(e.getMessage());
            throw new RepositoryException(INTERNAL_ERROR_STRING);
        }
    }


    @Override
    public void addContact(String name) throws RepositoryException {
        LOGGER.info("Adding new contact: {}", name);

        String selectSql = String.format("INSERT INTO contact (name)"
                         + "VALUES"
                         + "('%s')",name);
        try (Statement stmt = this.conn.createStatement()) {
            stmt.execute(selectSql);
        } catch (SQLException e) {
            LOGGER.error(e.getMessage());
            throw new RepositoryException(INTERNAL_ERROR_STRING);
        }        
    }

    @Override
    public String getNameByID(Integer id) throws RepositoryException, EntryNotFoundException {
        String name;
        try (Statement stmt = this.conn.createStatement()) {
            String selectSql = String.format("SELECT name FROM contact WHERE id=%s;",id);
            ResultSet resultSet = stmt.executeQuery(selectSql);
            if(resultSet.next()){
                name = resultSet.getString("name");
            } else {
                throw new EntryNotFoundException();
            }
        } catch (SQLException e) {
            LOGGER.error(e.getMessage());
            throw new RepositoryException(INTERNAL_ERROR_STRING);
        }
        return name;
    }


    @Override
    public ArrayList<String> searchBasedOnName(String searchQuery, Integer maxNumOfResults) throws RepositoryException {
        ArrayList<String> names = new ArrayList<>();
        String selectSql;
        if(maxNumOfResults<0){
            selectSql = String.format("SELECT name FROM contact WHERE name LIKE '%%%s%%' ORDER BY name LIMIT %s;",searchQuery, Integer.toString(maxNumOfResults));
        } else {
            selectSql = String.format("SELECT name FROM contact WHERE name LIKE '%%%s%%' ORDER BY name;",searchQuery);
        }
        try (Statement stmt = this.conn.createStatement()) {
            ResultSet resultSet = stmt.executeQuery(selectSql);
            while (resultSet.next()) {
                names.add(resultSet.getString("name"));
            }
        } catch (SQLException e) {
            LOGGER.error(e.getMessage());
            throw new RepositoryException(INTERNAL_ERROR_STRING);
        }
        return names;

    }
}
