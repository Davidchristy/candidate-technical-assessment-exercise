package com.gaggle.assessment_1;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.gaggle.assessment_1.database_services.SQLiteDatabaseService;
import com.gaggle.assessment_1.exceptions.EntryNotFoundException;
import com.gaggle.assessment_1.exceptions.RepositoryException;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class SQLiteServiceTests {



    @Test
	void testAddContactValid() {
        Connection conn = mock(Connection.class);
        Statement stmt = mock(Statement.class);
        MockedStatic<DriverManager> DriverManagerMock =  mockStatic(DriverManager.class);
        try{
            when(conn.createStatement()).thenReturn(stmt);
            when(DriverManager.getConnection(anyString())).thenReturn(conn);
            SQLiteDatabaseService db = new SQLiteDatabaseService();
            verify(stmt,times(1)).execute(anyString());
            db.addContact("test");
            verify(stmt,times(2)).execute(anyString());
        } catch (SQLException | RepositoryException e){
            e.printStackTrace();
        } finally {
            DriverManagerMock.close();
        }
	}

    @Test
	void testConnectionError() {
        Connection conn = mock(Connection.class);
        Statement stmt = mock(Statement.class);
        MockedStatic<DriverManager> DriverManagerMock =  mockStatic(DriverManager.class);
        try {
            when(DriverManager.getConnection(anyString())).thenReturn(conn);
            when(conn.createStatement()).thenThrow(SQLException.class);
            assertThrows(RepositoryException.class, ()->{
                new SQLiteDatabaseService();
            });
            verify(stmt,times(0)).execute(anyString());
        } catch (SQLException e){
            e.printStackTrace();
        } finally {
            DriverManagerMock.close();
        }
	}

    @Test
    void testGetNameByIDNoEntry() {
        Connection conn = mock(Connection.class);
        Statement stmt = mock(Statement.class);
        ResultSet resultSet = mock(ResultSet.class);
        MockedStatic<DriverManager> DriverManagerMock =  mockStatic(DriverManager.class);
        try{
            when(DriverManager.getConnection(anyString())).thenReturn(conn);
            when(conn.createStatement()).thenReturn(stmt);
            when(stmt.executeQuery(anyString())).thenReturn(resultSet);
            when(resultSet.next()).thenReturn(false);
            SQLiteDatabaseService db = new SQLiteDatabaseService();
            assertThrows(EntryNotFoundException.class, ()->{
                db.getNameByID(1);
            });
        } catch (SQLException | RepositoryException e){
            e.printStackTrace();
        } finally {
            DriverManagerMock.close();
        }
    }

    @Test
    void testGetNameByIDValid() {
        Connection conn = mock(Connection.class);
        Statement stmt = mock(Statement.class);
        ResultSet resultSet = mock(ResultSet.class);
        MockedStatic<DriverManager> DriverManagerMock =  mockStatic(DriverManager.class);
        try {
            when(DriverManager.getConnection(anyString())).thenReturn(conn);
            when(conn.createStatement()).thenReturn(stmt);
            when(stmt.executeQuery(anyString())).thenReturn(resultSet);
            when(resultSet.next()).thenReturn(true);
            when(resultSet.getString("name")).thenReturn("David");
            SQLiteDatabaseService db = new SQLiteDatabaseService();
            String name = db.getNameByID(1);
            assertEquals("David", name);
        } catch (SQLException | RepositoryException | EntryNotFoundException e) {
            e.printStackTrace();
        } finally {
            DriverManagerMock.close();
        }
    }

    @Test
    void testSearchBasedOnNameError() {
        Connection conn = mock(Connection.class);
        Statement stmt = mock(Statement.class);
        MockedStatic<DriverManager> DriverManagerMock =  mockStatic(DriverManager.class);
        try {
            when(DriverManager.getConnection(anyString())).thenReturn(conn);
            when(conn.createStatement()).thenReturn(stmt);
            when(stmt.executeQuery(anyString())).thenThrow(SQLException.class);
            SQLiteDatabaseService db = new SQLiteDatabaseService();
            assertThrows(RepositoryException.class, ()->{
                db.searchBasedOnName("Test",1);
            });
        } catch (SQLException | RepositoryException e) {
            e.printStackTrace();            
        } finally {
            DriverManagerMock.close();
        }
    }

    @Test
    void testSearchBasedOnName() {
        Connection conn = mock(Connection.class);
        Statement stmt = mock(Statement.class);
        ResultSet resultSet = mock(ResultSet.class);
        MockedStatic<DriverManager> DriverManagerMock =  mockStatic(DriverManager.class);
        try{
            when(DriverManager.getConnection(anyString())).thenReturn(conn);
            when(conn.createStatement()).thenReturn(stmt);
            when(stmt.executeQuery(anyString())).thenReturn(resultSet);
            when(resultSet.next()).thenReturn(true, true, true, false);
            when(resultSet.getString("name")).thenReturn("David", "Anderson", "Alice");
            SQLiteDatabaseService db = new SQLiteDatabaseService();
            ArrayList<String> names = db.searchBasedOnName("a", -1);
            assertEquals(3, names.size());
            // Note, names are not alphabetical here because that's done in SQLite
            assertEquals("David", names.get(0));
            assertEquals("Anderson", names.get(1));
            assertEquals("Alice", names.get(2));
        } catch (SQLException | RepositoryException e) {
            e.printStackTrace();
        } finally {
            DriverManagerMock.close();
        }
    }


}
