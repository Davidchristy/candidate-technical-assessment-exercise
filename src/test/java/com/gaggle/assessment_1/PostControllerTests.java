package com.gaggle.assessment_1;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.gaggle.assessment_1.controllers.PostController;
import com.gaggle.assessment_1.controllers.PutController;
import com.gaggle.assessment_1.database_services.SQLiteDatabaseService;
import com.gaggle.assessment_1.exceptions.EntryNotFoundException;
import com.gaggle.assessment_1.exceptions.RepositoryException;
import com.gaggle.assessment_1.user_requests.UserRequestByID;
import com.gaggle.assessment_1.user_requests.UserRequestByName;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;

@SpringBootTest
class PostControllerTests {

	@Test
	void addNewContactValid() throws RepositoryException{
		SQLiteDatabaseService db = mock(SQLiteDatabaseService.class);
		PutController controller = new PutController(db);
		UserRequestByName userRequest = new UserRequestByName();
		userRequest.setName("Test");
		ResponseEntity<Object> response = controller.addNewContact(userRequest);
		verify(db,times(1)).addContact(userRequest.getName());
		assertEquals(200, response.getStatusCode().value());
	}

	@Test
	void addNewContactErrorCase() throws RepositoryException{
		SQLiteDatabaseService db = mock(SQLiteDatabaseService.class);
		UserRequestByName userRequest = new UserRequestByName();
		userRequest.setName("Test");
		RepositoryException exception = new RepositoryException("Test");
		doThrow(exception).when(db).addContact(anyString());
		PutController controller = new PutController(db);
		ResponseEntity<Object> response = controller.addNewContact(userRequest);
		assertEquals(500, response.getStatusCode().value());
		assertEquals("<500 INTERNAL_SERVER_ERROR Internal Server Error,{Error=Test},[]>", response.toString());
	}

	@Test
	void testFindUserByIDValid() throws RepositoryException, EntryNotFoundException{
		SQLiteDatabaseService db = mock(SQLiteDatabaseService.class);
		when(db.getNameByID(1)).thenReturn("David");
		UserRequestByID userRequest = new UserRequestByID();
		userRequest.setId(1);
		PostController controller = new PostController(db);
		ResponseEntity<Object> response = controller.findContactByID(userRequest);
		verify(db,times(1)).getNameByID(userRequest.getId());
		assertEquals(200, response.getStatusCode().value());
		assertEquals("<200 OK OK,{Contact=David},[]>", response.toString());
	}

	@Test
	void testFindUserByIDNoEntry() throws RepositoryException, EntryNotFoundException{
		SQLiteDatabaseService db = mock(SQLiteDatabaseService.class);
		UserRequestByID userRequest = new UserRequestByID();
		userRequest.setId(1);
		doThrow(EntryNotFoundException.class).when(db).getNameByID(userRequest.getId());
		PostController controller = new PostController(db);
		ResponseEntity<Object> response = controller.findContactByID(userRequest);
		assertEquals(404, response.getStatusCode().value());
		assertEquals("<404 NOT_FOUND Not Found,{Error=No Contact with that ID is available},[]>", response.toString());
	}

	@Test
	void testFindUserByIDErrorCase() throws RepositoryException, EntryNotFoundException{
		SQLiteDatabaseService db = mock(SQLiteDatabaseService.class);
		UserRequestByID userRequest = new UserRequestByID();
		userRequest.setId(1);
		RepositoryException exception = new RepositoryException("Test");
		doThrow(exception).when(db).getNameByID(userRequest.getId());
		PostController controller = new PostController(db);
		ResponseEntity<Object> response = controller.findContactByID(userRequest);
		assertEquals(500, response.getStatusCode().value());
		assertEquals("<500 INTERNAL_SERVER_ERROR Internal Server Error,{Error=Test},[]>", response.toString());
	}

	@Test
	void testsearchContactNamesValid() throws RepositoryException{
		SQLiteDatabaseService db = mock(SQLiteDatabaseService.class);
		UserRequestByName userRequest = new UserRequestByName();
		userRequest.setName("Test");
		PostController controller = new PostController(db);
		ResponseEntity<Object> response = controller.searchContactNames(userRequest);
		assertEquals(200, response.getStatusCode().value());
		assertEquals("<200 OK OK,{Contacts=[]},[]>", response.toString());
		/* Note: Test shows empty contact list because it's designed to show the response, 
		for test on database see SQLiteServiceTests
		*/ 
	}

	@Test
	void testsearchContactNamesError() throws RepositoryException{
		SQLiteDatabaseService db = mock(SQLiteDatabaseService.class);
		UserRequestByName userRequest = new UserRequestByName();
		userRequest.setName("Test");
		RepositoryException exception = new RepositoryException("Test");
		doThrow(exception).when(db).searchBasedOnName(anyString(),anyInt());
		PostController controller = new PostController(db);
		ResponseEntity<Object> response = controller.searchContactNames(userRequest);
		assertEquals(500, response.getStatusCode().value());
		assertEquals("<500 INTERNAL_SERVER_ERROR Internal Server Error,{Error=Test},[]>", response.toString());
	}
}
