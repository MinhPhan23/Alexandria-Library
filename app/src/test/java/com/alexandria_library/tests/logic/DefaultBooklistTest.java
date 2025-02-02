package com.alexandria_library.tests.logic;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;

import com.alexandria_library.data.IUserPersistent;
import com.alexandria_library.dso.Book;
import com.alexandria_library.dso.Booklist;
import com.alexandria_library.dso.Reader;
import com.alexandria_library.dso.User;
import com.alexandria_library.logic.DefaultBooklist;
import com.alexandria_library.logic.Exception.BooklistException;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

/** @noinspection CodeBlock2Expr*/
public class DefaultBooklistTest {
    @Mock
    private Reader reader;
    @Mock
    private IUserPersistent data;
    @Mock
    private Book book1, book2;
    private Booklist oldList, newList;
    private DefaultBooklist defaultBooklist;
    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        Mockito.doNothing().when(data).addBookToAllList(Mockito.any(Booklist.class), Mockito.any(User.class));
        Mockito.doNothing().when(data).addBookToFinishedList(Mockito.any(Booklist.class), Mockito.any(User.class));
        Mockito.doNothing().when(data).addBookToReadingList(Mockito.any(Booklist.class), Mockito.any(User.class));

        defaultBooklist = new DefaultBooklist(data);

        newList = new Booklist();
        oldList = new Booklist();

        assertNotNull(defaultBooklist);
    }

    @Test
    public void testAddBookToAll() {
        try {
            Mockito.when(book1.getName()).thenReturn("Twilight");
            Mockito.when(book2.getName()).thenReturn("Jedi");

            newList.add(book1);
            oldList.add(book2);

            Mockito.when(reader.getAllBooksList()).thenReturn(oldList);

            defaultBooklist.addBookToAll(reader, newList);

            Mockito.verify(data, Mockito.times(1)).addBookToAllList(Mockito.any(Booklist.class), Mockito.any(User.class));
            Mockito.verify(reader).getAllBooksList();
            assertEquals(2, oldList.size());
        }
        catch (Exception e) {
            e.printStackTrace();
            assert(false);
        }
    }

    @Test
    public void testAddBooklistToAll() {
        try {
            Mockito.when(book1.getName()).thenReturn("Twilight");
            Mockito.when(book2.getName()).thenReturn("Jedi");

            newList.add(book1);
            newList.add(book2);

            Mockito.when(reader.getAllBooksList()).thenReturn(oldList);

            defaultBooklist.addBookToAll(reader, newList);

            Mockito.verify(data, Mockito.times(1)).addBookToAllList(Mockito.any(Booklist.class), Mockito.any(User.class));
            Mockito.verify(reader).getAllBooksList();
            assertEquals(2, oldList.size());
        }
        catch (Exception e) {
            e.printStackTrace();
            assert(false);
        }
    }

    @Test
    public void testAddDuplicateBookToAll() {
        try {
            Mockito.when(book1.getName()).thenReturn("Twilight");

            oldList.setName("all");
            newList.add(book1);
            oldList.add(book1);

            Mockito.when(reader.getAllBooksList()).thenReturn(oldList);

            Exception exception = assertThrows(BooklistException.class, () -> {
                defaultBooklist.addBookToAll(reader, newList);
            });

            String expectedMessage = "The book(s) Twilight is already in list all";
            String actualMessage = exception.getMessage();

            assertNotNull(actualMessage);
            assertEquals(expectedMessage, actualMessage);

            Mockito.verify(data, Mockito.times(0)).addBookToAllList(Mockito.any(Booklist.class), Mockito.any(User.class));
            Mockito.verify(reader).getAllBooksList();
        }
        catch (Exception e) {
            e.printStackTrace();
            assert(false);
        }
    }

    @Test
    public void testAddMultipleDuplicateBookToAll() {
        try {
            Mockito.when(book1.getName()).thenReturn("Twilight");
            Mockito.when(book2.getName()).thenReturn("Jedi");

            oldList.setName("all");
            newList.add(book1);
            newList.add(book2);
            oldList.add(book1);
            oldList.add(book2);

            Mockito.when(reader.getAllBooksList()).thenReturn(oldList);

            Exception exception = assertThrows(BooklistException.class, () -> {
                defaultBooklist.addBookToAll(reader, newList);
            });

            String expectedMessage = "The book(s) Twilight, Jedi is already in list all";
            String actualMessage = exception.getMessage();

            assertNotNull(actualMessage);
            assertEquals(expectedMessage, actualMessage);

            Mockito.verify(data, Mockito.times(0)).addBookToAllList(Mockito.any(Booklist.class), Mockito.any(User.class));
            Mockito.verify(reader).getAllBooksList();
        }
        catch (Exception e) {
            e.printStackTrace();
            assert(false);
        }
    }

    @Test
    public void testAddBookToReading() {
        try {
            Mockito.when(book1.getName()).thenReturn("Twilight");
            Mockito.when(book2.getName()).thenReturn("Jedi");

            newList.add(book1);
            oldList.add(book2);

            Mockito.when(reader.getInProgressList()).thenReturn(oldList);

            defaultBooklist.addBookToInProgress(reader, newList);

            Mockito.verify(data, Mockito.times(1)).addBookToReadingList(Mockito.any(Booklist.class), Mockito.any(User.class));
            Mockito.verify(reader).getInProgressList();
            assertEquals(2, oldList.size());
        }
        catch (Exception e) {
            e.printStackTrace();
            assert(false);
        }
    }

    @Test
    public void testAddBooklistToReading() {
        try {
            Mockito.when(book1.getName()).thenReturn("Twilight");
            Mockito.when(book2.getName()).thenReturn("Jedi");

            newList.add(book1);
            newList.add(book2);

            Mockito.when(reader.getInProgressList()).thenReturn(oldList);

            defaultBooklist.addBookToInProgress(reader, newList);

            Mockito.verify(data, Mockito.times(1)).addBookToReadingList(Mockito.any(Booklist.class), Mockito.any(User.class));
            Mockito.verify(reader).getInProgressList();
            assertEquals(2, oldList.size());
        }
        catch (Exception e) {
            e.printStackTrace();
            assert(false);
        }
    }

    @Test
    public void testAddDuplicateBookToReading() {
        try {
            Mockito.when(book1.getName()).thenReturn("Twilight");

            oldList.setName("in progress");
            newList.add(book1);
            oldList.add(book1);

            Mockito.when(reader.getInProgressList()).thenReturn(oldList);

            Exception exception = assertThrows(BooklistException.class, () -> {
                defaultBooklist.addBookToInProgress(reader, newList);
            });

            String expectedMessage = "The book(s) Twilight is already in list in progress";
            String actualMessage = exception.getMessage();

            assertNotNull(actualMessage);
            assertEquals(expectedMessage, actualMessage);

            Mockito.verify(data, Mockito.times(0)).addBookToReadingList(Mockito.any(Booklist.class), Mockito.any(User.class));
            Mockito.verify(reader).getInProgressList();
        }
        catch (Exception e) {
            e.printStackTrace();
            assert(false);
        }
    }

    @Test
    public void testAddMultipleDuplicateBookToReading() {
        try {
            Mockito.when(book1.getName()).thenReturn("Twilight");
            Mockito.when(book2.getName()).thenReturn("Jedi");

            oldList.setName("all");
            newList.add(book1);
            newList.add(book2);
            oldList.add(book1);
            oldList.add(book2);

            Mockito.when(reader.getInProgressList()).thenReturn(oldList);

            Exception exception = assertThrows(BooklistException.class, () -> {
                defaultBooklist.addBookToInProgress(reader, newList);
            });

            String expectedMessage = "The book(s) Twilight, Jedi is already in list all";
            String actualMessage = exception.getMessage();

            assertNotNull(actualMessage);
            assertEquals(expectedMessage, actualMessage);

            Mockito.verify(data, Mockito.times(0)).addBookToReadingList(Mockito.any(Booklist.class), Mockito.any(User.class));
            Mockito.verify(reader).getInProgressList();
        }
        catch (Exception e) {
            e.printStackTrace();
            assert(false);
        }
    }

    @Test
    public void testAddBookToFinished() {
        try {
            Mockito.when(book1.getName()).thenReturn("Twilight");
            Mockito.when(book2.getName()).thenReturn("Jedi");

            newList.add(book1);
            oldList.add(book2);

            Mockito.when(reader.getFinishedList()).thenReturn(oldList);

            defaultBooklist.addBookToFinished(reader, newList);

            Mockito.verify(data, Mockito.times(1)).addBookToFinishedList(Mockito.any(Booklist.class), Mockito.any(User.class));
            Mockito.verify(reader).getFinishedList();
            assertEquals(2, oldList.size());
        }
        catch (Exception e) {
            e.printStackTrace();
            assert(false);
        }
    }

    @Test
    public void testAddBooklistToFinished() {
        try {
            Mockito.when(book1.getName()).thenReturn("Twilight");
            Mockito.when(book2.getName()).thenReturn("Jedi");

            newList.add(book1);
            newList.add(book2);

            Mockito.when(reader.getFinishedList()).thenReturn(oldList);

            defaultBooklist.addBookToFinished(reader, newList);

            Mockito.verify(data, Mockito.times(1)).addBookToFinishedList(Mockito.any(Booklist.class), Mockito.any(User.class));
            Mockito.verify(reader).getFinishedList();
            assertEquals(2, oldList.size());
        }
        catch (Exception e) {
            e.printStackTrace();
            assert(false);
        }
    }

    @Test
    public void testAddDuplicateBookToFinished() {
        try {
            Mockito.when(book1.getName()).thenReturn("Twilight");

            oldList.setName("finished");
            newList.add(book1);
            oldList.add(book1);

            Mockito.when(reader.getFinishedList()).thenReturn(oldList);

            Exception exception = assertThrows(BooklistException.class, () -> {
                defaultBooklist.addBookToFinished(reader, newList);
            });

            String expectedMessage = "The book(s) Twilight is already in list finished";
            String actualMessage = exception.getMessage();

            assertNotNull(actualMessage);
            assertEquals(expectedMessage, actualMessage);

            Mockito.verify(data, Mockito.times(0)).addBookToFinishedList(Mockito.any(Booklist.class), Mockito.any(User.class));
            Mockito.verify(reader).getFinishedList();
        }
        catch (Exception e) {
            e.printStackTrace();
            assert(false);
        }
    }

    @Test
    public void testAddMultipleDuplicateBookToFinished() {
        try {
            Mockito.when(book1.getName()).thenReturn("Twilight");
            Mockito.when(book2.getName()).thenReturn("Jedi");

            oldList.setName("finished");
            newList.add(book1);
            newList.add(book2);
            oldList.add(book1);
            oldList.add(book2);

            Mockito.when(reader.getFinishedList()).thenReturn(oldList);

            Exception exception = assertThrows(BooklistException.class, () -> {
                defaultBooklist.addBookToFinished(reader, newList);
            });

            String expectedMessage = "The book(s) Twilight, Jedi is already in list finished";
            String actualMessage = exception.getMessage();

            assertNotNull(actualMessage);
            assertEquals(expectedMessage, actualMessage);

            Mockito.verify(data, Mockito.times(0)).addBookToFinishedList(Mockito.any(Booklist.class), Mockito.any(User.class));
            Mockito.verify(reader).getFinishedList();
        }
        catch (Exception e) {
            e.printStackTrace();
            assert(false);
        }
    }
    @Test
    public void testRemoveBookFromAll() {
        try {
            Mockito.when(book1.getName()).thenReturn("Twilight");
            Mockito.when(book2.getName()).thenReturn("Jedi");

            oldList.add(book1);
            oldList.add(book2);
            newList.add(book1);

            Mockito.when(reader.getAllBooksList()).thenReturn(oldList);

            defaultBooklist.removeBookFromAll(reader, newList);

            Mockito.verify(data, Mockito.times(1)).deleteUserAllListBook(Mockito.any(Booklist.class), Mockito.any(User.class));
            Mockito.verify(reader).getAllBooksList();
            assertEquals(1, oldList.size());
            assertEquals(oldList.get(0).getName(), "Jedi");
        }
        catch (Exception e) {
            e.printStackTrace();
            assert(false);
        }
    }

    @Test
    public void testRemoveBooklistFromAll() {
        try {
            Mockito.when(book1.getName()).thenReturn("Twilight");
            Mockito.when(book2.getName()).thenReturn("Jedi");

            oldList.add(book1);
            oldList.add(book2);
            newList.add(book1);
            newList.add(book2);

            Mockito.when(reader.getAllBooksList()).thenReturn(oldList);

            defaultBooklist.removeBookFromAll(reader, newList);

            Mockito.verify(data, Mockito.times(1)).deleteUserAllListBook(Mockito.any(Booklist.class), Mockito.any(User.class));
            Mockito.verify(reader).getAllBooksList();
            assertEquals(0, oldList.size());
        }
        catch (Exception e) {
            e.printStackTrace();
            assert(false);
        }
    }
    @Test
    public void testRemovedNonExistingBookFromAll() {
        try {
            Mockito.when(book1.getName()).thenReturn("Twilight");
            Mockito.when(book2.getName()).thenReturn("Jedi");

            oldList.setName("all");
            newList.add(book1);
            oldList.add(book2);

            Mockito.when(reader.getAllBooksList()).thenReturn(oldList);

            Exception exception = assertThrows(BooklistException.class, () -> {
                defaultBooklist.removeBookFromAll(reader, newList);
            });

            String expectedMessage = "The book(s) Twilight is not in list all";
            String actualMessage = exception.getMessage();

            assertNotNull(actualMessage);
            assertEquals(expectedMessage, actualMessage);

            Mockito.verify(data, Mockito.times(0)).addBookToAllList(Mockito.any(Booklist.class), Mockito.any(User.class));
            Mockito.verify(reader).getAllBooksList();
        }
        catch (Exception e) {
            e.printStackTrace();
            assert (false);
        }
    }

    @Test
    public void testRemovedNonExistingBooklistFromAll() {
        try {
            Mockito.when(book1.getName()).thenReturn("Twilight");
            Mockito.when(book2.getName()).thenReturn("Jedi");

            oldList.setName("all");
            newList.add(book1);
            newList.add(book2);

            Mockito.when(reader.getAllBooksList()).thenReturn(oldList);

            Exception exception = assertThrows(BooklistException.class, () -> {
                defaultBooklist.removeBookFromAll(reader, newList);
            });

            String expectedMessage = "The book(s) Twilight, Jedi is not in list all";
            String actualMessage = exception.getMessage();

            assertNotNull(actualMessage);
            assertEquals(expectedMessage, actualMessage);

            Mockito.verify(data, Mockito.times(0)).addBookToAllList(Mockito.any(Booklist.class), Mockito.any(User.class));
            Mockito.verify(reader).getAllBooksList();
        }
        catch (Exception e) {
            e.printStackTrace();
            assert(false);
        }
    }

    @Test
    public void testRemoveBookFromInProgress() {
        try {
            Mockito.when(book1.getName()).thenReturn("Twilight");
            Mockito.when(book2.getName()).thenReturn("Jedi");

            oldList.add(book1);
            oldList.add(book2);
            newList.add(book1);

            Mockito.when(reader.getInProgressList()).thenReturn(oldList);

            defaultBooklist.removeBookFromInProgress(reader, newList);

            Mockito.verify(data, Mockito.times(1)).deleteReadingListBook(Mockito.any(Booklist.class), Mockito.any(User.class));
            Mockito.verify(reader).getInProgressList();
            assertEquals(1, oldList.size());
            assertEquals(oldList.get(0).getName(), "Jedi");
        }
        catch (Exception e) {
            e.printStackTrace();
            assert(false);
        }
    }

    @Test
    public void testRemoveBooklistFromInProgress() {
        try {
            Mockito.when(book1.getName()).thenReturn("Twilight");
            Mockito.when(book2.getName()).thenReturn("Jedi");

            oldList.add(book1);
            oldList.add(book2);
            newList.add(book1);
            newList.add(book2);

            Mockito.when(reader.getInProgressList()).thenReturn(oldList);

            defaultBooklist.removeBookFromInProgress(reader, newList);

            Mockito.verify(data, Mockito.times(1)).deleteReadingListBook(Mockito.any(Booklist.class), Mockito.any(User.class));
            Mockito.verify(reader).getInProgressList();
            assertEquals(0, oldList.size());
        }
        catch (Exception e) {
            e.printStackTrace();
            assert(false);
        }
    }
    @Test
    public void testRemovedNonExistingBookFromInProgress() {
        try {
            Mockito.when(book1.getName()).thenReturn("Twilight");
            Mockito.when(book2.getName()).thenReturn("Jedi");

            oldList.setName("In Progress");
            newList.add(book1);
            oldList.add(book2);

            Mockito.when(reader.getInProgressList()).thenReturn(oldList);

            Exception exception = assertThrows(BooklistException.class, () -> {
                defaultBooklist.removeBookFromInProgress(reader, newList);
            });

            String expectedMessage = "The book(s) Twilight is not in list In Progress";
            String actualMessage = exception.getMessage();

            assertNotNull(actualMessage);
            assertEquals(expectedMessage, actualMessage);

            Mockito.verify(data, Mockito.times(0)).addBookToReadingList(Mockito.any(Booklist.class), Mockito.any(User.class));
            Mockito.verify(reader).getInProgressList();
        }
        catch (Exception e) {
            e.printStackTrace();
            assert(false);
        }
    }

    @Test
    public void testRemovedNonExistingBooklistFromInProgress() {
        try {
            Mockito.when(book1.getName()).thenReturn("Twilight");
            Mockito.when(book2.getName()).thenReturn("Jedi");

            oldList.setName("In Progress");
            newList.add(book1);
            newList.add(book2);

            Mockito.when(reader.getInProgressList()).thenReturn(oldList);

            Exception exception = assertThrows(BooklistException.class, () -> {
                defaultBooklist.removeBookFromInProgress(reader, newList);
            });

            String expectedMessage = "The book(s) Twilight, Jedi is not in list In Progress";
            String actualMessage = exception.getMessage();

            assertNotNull(actualMessage);
            assertEquals(expectedMessage, actualMessage);

            Mockito.verify(data, Mockito.times(0)).addBookToReadingList(Mockito.any(Booklist.class), Mockito.any(User.class));
            Mockito.verify(reader).getInProgressList();
        }
        catch (Exception e) {
            e.printStackTrace();
            assert(false);
        }
    }
    @Test
    public void testRemoveBookFromFinished() {
        try {
            Mockito.when(book1.getName()).thenReturn("Twilight");
            Mockito.when(book2.getName()).thenReturn("Jedi");

            oldList.add(book1);
            oldList.add(book2);
            newList.add(book1);

            Mockito.when(reader.getFinishedList()).thenReturn(oldList);

            defaultBooklist.removeBookFromFinished(reader, newList);

            Mockito.verify(data, Mockito.times(1)).deleteFinishedListBook(Mockito.any(Booklist.class), Mockito.any(User.class));
            Mockito.verify(reader).getFinishedList();
            assertEquals(1, oldList.size());
            assertEquals(oldList.get(0).getName(), "Jedi");
        }
        catch (Exception e) {
            e.printStackTrace();
            assert(false);
        }
    }

    @Test
    public void testRemoveBooklistFromFinished() {
        try {
            Mockito.when(book1.getName()).thenReturn("Twilight");
            Mockito.when(book2.getName()).thenReturn("Jedi");

            oldList.add(book1);
            oldList.add(book2);
            newList.add(book1);
            newList.add(book2);

            Mockito.when(reader.getFinishedList()).thenReturn(oldList);

            defaultBooklist.removeBookFromFinished(reader, newList);

            Mockito.verify(data, Mockito.times(1)).deleteFinishedListBook(Mockito.any(Booklist.class), Mockito.any(User.class));
            Mockito.verify(reader).getFinishedList();
            assertEquals(0, oldList.size());
        }
        catch (Exception e) {
            e.printStackTrace();
            assert(false);
        }
    }
    @Test
    public void testRemovedNonExistingBookFromFinished() {
        try {
            Mockito.when(book1.getName()).thenReturn("Twilight");
            Mockito.when(book2.getName()).thenReturn("Jedi");

            oldList.setName("Finished");
            newList.add(book1);
            oldList.add(book2);

            Mockito.when(reader.getFinishedList()).thenReturn(oldList);

            Exception exception = assertThrows(BooklistException.class, () -> {
                defaultBooklist.removeBookFromFinished(reader, newList);
            });

            String expectedMessage = "The book(s) Twilight is not in list Finished";
            String actualMessage = exception.getMessage();

            assertNotNull(actualMessage);
            assertEquals(expectedMessage, actualMessage);

            Mockito.verify(data, Mockito.times(0)).addBookToFinishedList(Mockito.any(Booklist.class), Mockito.any(User.class));
            Mockito.verify(reader).getFinishedList();
        }
        catch (Exception e) {
            e.printStackTrace();
            assert(false);
        }
    }

    @Test
    public void testRemovedNonExistingBooklistFromFinished() {
        try {
            Mockito.when(book1.getName()).thenReturn("Twilight");
            Mockito.when(book2.getName()).thenReturn("Jedi");

            oldList.setName("Finished");
            newList.add(book1);
            newList.add(book2);

            Mockito.when(reader.getFinishedList()).thenReturn(oldList);

            Exception exception = assertThrows(BooklistException.class, () -> {
                defaultBooklist.removeBookFromFinished(reader, newList);
            });

            String expectedMessage = "The book(s) Twilight, Jedi is not in list Finished";
            String actualMessage = exception.getMessage();

            assertNotNull(actualMessage);
            assertEquals(expectedMessage, actualMessage);

            Mockito.verify(data, Mockito.times(0)).addBookToFinishedList(Mockito.any(Booklist.class), Mockito.any(User.class));
            Mockito.verify(reader).getFinishedList();
        }
        catch (Exception e) {
            e.printStackTrace();
            assert(false);
        }
    }
}
