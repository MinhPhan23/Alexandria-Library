package com.alexandria_library.data.DataInterface;

import com.alexandria_library.dso.Book;
import com.alexandria_library.dso.Booklist;
import com.alexandria_library.dso.User;

import java.util.ArrayList;

public interface IBookPersistentHSQLDB extends IBookPersistent{

    /*****
     * those functions we are not using, because we will use on iterations 3
     */
    public boolean upload(Book book, User user);
    public void deleteLibraryBook(Booklist list, User user);
    public ArrayList<String> searchTagByBook (Book book);
    public ArrayList<String> getAllTags ();
    public ArrayList<String> getAllGenres();

    //START searching book
    public Booklist searchTag(String tagName);
    public Booklist searchGenre (String genreName);

    //START finding book
    public Booklist getUserCustomList(User user);
    public Booklist getUserInProgressList(User user);
    public Booklist getUserFinishedList(User user);
}
