package com.alexandria_library.tests.logic;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import com.alexandria_library.data.IBookPersistent;
import com.alexandria_library.data.hsqldb.BookPersistentHSQLDB;
import com.alexandria_library.dso.Book;
import com.alexandria_library.dso.Booklist;
import com.alexandria_library.dso.IUser;
import com.alexandria_library.dso.Librarian;
import com.alexandria_library.logic.BookListFilter;
import com.alexandria_library.logic.BookModifier;
import com.alexandria_library.logic.IBookModifier;
import com.alexandria_library.tests.util.TestUtils;

import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class BookModifierITTest {
    private IBookModifier bookModifier;
    private Booklist libraryBooks;
    private IUser librarian;
    private File tempDB;
    private IBookPersistent persistent;
    @Before
    public void setUp() throws IOException {
        this.tempDB = TestUtils.copyDB();
        persistent = new BookPersistentHSQLDB(this.tempDB.getAbsolutePath().replace(".script", "")) ;
        libraryBooks = persistent.getBookList();
        this.bookModifier = new BookModifier(persistent);
        this.librarian = new Librarian("xxxx", "123", 20);
    }

    @Test
    public void TestAddNewBookWithOneTagAndGenre(){
        String uniqueBookName = "UniqueBookName";
        String uniqueAuthorName = "UniqueAuthorName";
        String date = "2000-08-02";
        ArrayList<String> newTags = new ArrayList<>();
        ArrayList<String> newGenres = new ArrayList<>();
        newTags.add("newTag1");
        newGenres.add("newGenre1");

        boolean shouldBeTrue = bookModifier.uploadBook(librarian, libraryBooks.size()+1, uniqueBookName, uniqueAuthorName, date, newTags, newGenres);
        assertTrue(shouldBeTrue);
        libraryBooks = persistent.getBookList();
        boolean checkExits = false;
        Book comparable = new Book(6, uniqueBookName, uniqueAuthorName, date, newTags, newGenres);
        for (int i = 0; i<libraryBooks.size(); i++){
            if(libraryBooks.get(i).equals(comparable)){
                checkExits = true;
            }
        }
        assertTrue(checkExits);
    }

    @Test
    public void TestAddNewBookWithTwoTagsAndGenres(){
        String name = "Two tags and two genres";
        String author = "Xiang";
        String date = "2024-06-05";
        ArrayList<String> newTags = new ArrayList<>();
        ArrayList<String> newGenres = new ArrayList<>();
        newTags.add("Tag1");
        newTags.add("Tage2");
        newGenres.add("Genres2");
        newGenres.add("Genres1");
        boolean shouldBeTrue = bookModifier.uploadBook(librarian, libraryBooks.size()+1, name, author, date, newTags, newGenres);
        assertTrue(shouldBeTrue);
        libraryBooks = persistent.getBookList();
        boolean checkExits = false;
        Book comparable = new Book(6, name, author, date, newTags, newGenres);
        for (int i = 0; i<libraryBooks.size(); i++){
            if(libraryBooks.get(i).noOrderEquals(comparable)){
                checkExits = true;
            }
        }
        assertTrue(checkExits);
    }
}
