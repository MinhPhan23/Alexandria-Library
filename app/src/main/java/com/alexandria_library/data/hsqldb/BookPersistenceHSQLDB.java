package com.alexandria_library.data.hsqldb;

import com.alexandria_library.R;
import com.alexandria_library.data.IBookPersistenceSQLDB;
import com.alexandria_library.data.IBookPersistentIntermediate;
import com.alexandria_library.dso.Book;
import com.alexandria_library.dso.Booklist;
import com.alexandria_library.dso.Librarian;
import com.alexandria_library.dso.Reader;
import com.alexandria_library.dso.User;

import java.sql.Array;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class BookPersistenceHSQLDB implements IBookPersistenceSQLDB {

    private final String dbPath;
    private static int bookID = 1;
    private static int tagID = 1;
    private static int genreID = 1;
    private static int bookTagID = 1;
    private static int bookGenreID = 1;

    public BookPersistenceHSQLDB(final String dbPath){this.dbPath = dbPath;}

    private Connection connection() throws SQLException {
        return DriverManager.getConnection("jdbc:hsqldb:file:" + dbPath + ";shutdown=true", "SA", "");
    }

    private Book fromResultSet(final ResultSet rs) throws SQLException{
        Book book = null;
        List<String> tags = new ArrayList<>();
        List<String> genres = new ArrayList<>();

        while(rs.next()){
            if(book == null){
                //getting information
                final int bookID = rs.getInt("BOOK_ID");
                final String bookName = rs.getString("BOOK_NAME");
                final String bookAuthor = rs.getString("BOOK_AUTHOR");
                final String bookDate = rs.getString("BOOK_DATE");
                List<String> tempGenre = new ArrayList<>();
                List<String> tempTag = new ArrayList<>();
                book = new Book(bookID, bookName, bookAuthor, bookDate, tempTag, tempGenre);
            }

            String tag = rs.getString("TAG_NAME");
            if(tag != null && !tags.contains(tag)){
                tags.add(tag);
            }
            String genre = rs.getString("GENRE_NAME");
            if(tag != null && !genres.contains(genre)){
                genres.add(genre);
            }
        }
        if(book != null){
            book.setTags(tags);
            book.setGenres(genres);
        }
        return book;
    }

    @Override
    public int checkCredentials(User user){
        return 0;
    }

    @Override
    public boolean upload(Book book, User user) throws SQLException {
        boolean result = false;
        if(checkCredentials(user) == 0 && duplicateBook(book.getName())<0){
            addBook(book);
            result = true;
        }
        return result;
    }

    private void addBook(Book newBook) throws SQLException{
        String insert = "INSERT INTO BOOKS(BOOK_ID, BOOK_NAME, BOOK_AUTHOR, BOOK_DATE) VALUES (?, ?, ?, ?)";
        try(final Connection c = connection()){
            PreparedStatement statement = c.prepareStatement(insert);

            statement.setInt(1, bookID);
            statement.setString(2, newBook.getName());
            statement.setString(3, newBook.getAuthor());
            statement.setString(4, newBook.getDate());
            int success = statement.executeUpdate();

            if(success == 0){
                throw new SQLException ("@BookPersistenceHSQLDB.java addBook unsuccessful");
            }

            // adding new tag or make new relation with tag and book
            for(int i = 0; i<newBook.getTags().size(); i++){
                String currentTag = newBook.getTags().get(i);
                int findTagID = duplicateTag(currentTag);
                if(findTagID < 0){
                    int newTagID = addTag(currentTag);
                    addBookTagRelation(bookID, newTagID);
                }
                else{
                    addBookTagRelation(bookID, findTagID);
                }
            }

            // adding new genre or make new relation with genre and book
            for(int j = 0; j<newBook.getGenres().size(); j++){
                String currentGenre = newBook.getGenres().get(j);
                int findGenreID = duplicateGenre(currentGenre);
                if(findGenreID < 0){
                    int newGenreID = addGenre(currentGenre);
                    addBookGenreRelation(bookID, newGenreID);
                }
                else{
                    addBookGenreRelation(bookID, findGenreID);
                }
            }

            bookID++;
            statement.close();
        }
    }

    private int addTag(String tagName) throws SQLException {
        String insertTag = "INSERT INTO TAGS (TAG_NAME, TAG_ID) VALUES (?, ?)";
        int result = tagID;
        try(final Connection c = connection()){
            PreparedStatement statement = c.prepareStatement(insertTag);

            statement.setString(1, tagName);
            statement.setInt(2, tagID);
            int success = statement.executeUpdate();
            if(success == 0){
                throw new SQLException ("@BookPersistenceHSQLDB.java addTag unsuccessful");
            }
            tagID++;
            statement.close();
        }
        return result;
    }

    private int addGenre(String genreName) throws SQLException{
        String insertGenre = "INSERT INTO GENRES (GENRE_NAME, GENRE_ID) VALUES (?, ?)";
        int result = genreID;
        try(final Connection c = connection()){
            PreparedStatement statement = c.prepareStatement(insertGenre);

            statement.setString(1, genreName);
            statement.setInt(2, genreID);
            int success = statement.executeUpdate();
            if(success == 0){
                throw new SQLException ("@BookPersistenceHSQLDB.java addGenre unsuccessful");
            }
            genreID++;
            statement.close();
        }
        return result;
    }

    private int addBookTagRelation(int bookID, int tagID) throws SQLException{
        String insertBookTag = "INSERT INTO BOOKTAGS(BOOK_ID, TAG_ID, BOOKTAGS_PK) VALUES (?, ?, ?)";
        int result = bookTagID;
        try(final Connection c = connection()){
            PreparedStatement statement = c.prepareStatement(insertBookTag);

            statement.setInt(1, bookID);
            statement.setInt(2, tagID);
            statement.setInt(3, bookTagID);
            int success = statement.executeUpdate();
            if(success == 0){
                throw new SQLException ("@BookPersistenceHSQLDB.java addBookTagRelation unsuccessful");
            }
            bookTagID++;
            statement.close();
        }
        return result;
    }

    private int addBookGenreRelation(int bookID, int genreID) throws SQLException{
        String insertBookGenre = "INSERT INTO BOOKGENRES(BOOK_ID, GENRE_ID, BOOKGENRES_PK) VALUES (?, ?, ?)";
        int result = bookGenreID;
        try(final Connection c = connection()){
            PreparedStatement statement = c.prepareStatement(insertBookGenre);

            statement.setInt(1, bookID);
            statement.setInt(2, genreID);
            statement.setInt(3, bookGenreID);
            int success = statement.executeUpdate();
            if(success == 0){
                throw new SQLException ("@BookPersistenceHSQLDB.java addBookGenreRelation unsuccessful");
            }
            bookGenreID++;
            statement.close();
        }
        return result;
    }

    private int duplicateBook (String bookName) throws SQLException{
        String query = "SELECT * FROM BOOKS";
        int findBookID = -1;
        try(final Connection c = connection()){
            PreparedStatement statement = c.prepareStatement(query);
            ResultSet rs = statement.executeQuery();

            while(rs.next()){
                int id = rs.getInt("BOOK_ID");
                if(rs.getString("BOOK_NAME").equals(bookName)){
                    findBookID = id;
                }
            }
        }
        return findBookID;
    }
    private int duplicateTag (String tagName) throws SQLException {
        String query = "SELECT * FROM TAGS";
        int findTagID = -1;
        try(final Connection c = connection()){
            PreparedStatement statement = c.prepareStatement(query);
            ResultSet rs = statement.executeQuery();

            while(rs.next()){
                int id = rs.getInt("TAG_ID");
                if(rs.getString("TAG_NAME").equals(tagName)){
                    findTagID = id;
                }
            }
        }
        return findTagID;
    }

    private int duplicateGenre(String genreName) throws SQLException{
        String query = "SELECT * FROM GENRES";
        int findTagID = -1;
        try(final Connection c = connection()){
            PreparedStatement statement = c.prepareStatement(query);
            ResultSet rs = statement.executeQuery();

            while(rs.next()){
                int id = rs.getInt("GENRE_ID");
                if(rs.getString("GENRE_NAME").equals(genreName)){
                    findTagID = id;
                }
            }
        }
        return findTagID;
    }

    @Override
    public int update(Book book, User user) {
        return 0;
    }


    @Override
    public void deleteLibraryBook(ArrayList<Book> list, User user) throws SQLException{
        if(user instanceof Librarian){
            Librarian librarian = (Librarian) user;
            for(int i = 0; i<list.size(); i++){
                deleteFromLibrary(list.get(i), librarian);
            }
        }
    }
    @Override
    public void deleteUserAllListBook(ArrayList<Book> list, User user) throws SQLException{
        if(user instanceof Reader){
            Reader reader = (Reader) user;
            for (int i = 0; i<list.size(); i++){
                deleteFromAllList(list.get(i), reader);
            }
        }
    }
    @Override
    public void deleteInProgressListBook(ArrayList<Book> list, User user) throws SQLException{
        if(user instanceof Reader){
            Reader reader = (Reader) user;
            for (int i = 0; i<list.size(); i++){
                deleteFromInProgressList(list.get(i), reader);
            }
        }

    }
    @Override
    public void deleteFinishedListBook(ArrayList<Book> list, User user) throws SQLException{
        if(user instanceof Reader){
            Reader reader = (Reader) user;
            for (int i = 0; i<list.size(); i++){
                deleteFromFinishedList(list.get(i), reader);
            }
        }
    }

    private void deleteFromLibrary(Book book, Librarian librarian) throws SQLException {

    }
    private void deleteFromAllList(Book book, Reader reader) throws SQLException {

    }
    private void deleteFromInProgressList(Book book, Reader reader) throws SQLException {

    }
    private void deleteFromFinishedList(Book book, Reader reader) throws SQLException {

    }

    @Override
    public Booklist searchBookByTag(String tagName) throws SQLException{
        Booklist books = new Booklist();
        String query = "SELECT B.BOOK_ID, B.BOOK_NAME, B.BOOK_AUTHOR, B.BOOK_DATE, " +
                "TG.TAG_NAME, GS.GENRE_NAME FROM BOOKS B "+
                "JOIN BOOKTAGS BT ON B.BOOK_ID = BT.BOOK_ID "+
                "JOIN TAGS TG ON BT.TAG_ID = TG.TAG_ID " +
                "JOIN BOOKGENRES BG ON B.BOOK_ID = BG.BOOK_ID "+
                "JOIN GENRES GS ON BG.GENRE_ID = GS.GENRE_ID "+
                "WHERE TG.TAG_NAME = ?";

        try(Connection c = connection()){
            PreparedStatement statement = c.prepareStatement(query);
            statement.setString(1, tagName);
            ResultSet rs = statement.executeQuery();

            while(rs.next()){
                Book newBook = fromResultSet(rs);
                if(newBook != null){
                    books.add(newBook);
                }
            }
            rs.close();
        }
        return books;
    }

    @Override
    public ArrayList<String> searchTagByBook (Book book) throws SQLException{
        ArrayList<String> result = new ArrayList<>();
        String query = "SELECT TG.TAG_NAME FROM TAGS TG "+
                        "JOIN BOOKTAGS BT ON TG.TAG_ID = BT.TAG_ID "+
                        "JOIN BOOKS B ON BT.BOOK_ID = B.BOOK_ID " +
                        "WHERE B.BOOK_NAME = ? ";
        try(Connection c = connection()){
            PreparedStatement statement = c.prepareStatement(query);
            statement.setString(1, book.getName());
            ResultSet rs = statement.executeQuery();

            while (rs.next()){
                String tagName = rs.getString("TAG_NAME");
                result.add(tagName);
            }
            rs.close();
        }
        return result;
    }

    @Override
    public Booklist searchGenre (String genreName) throws SQLException{
        Booklist books = new Booklist();
        String query =
                "SELECT B.BOOK_ID, B.BOOK_NAME, B.BOOK_AUTHOR, B.BOOK_DATE, " +
                        "TG.TAG_NAME, GS.GENRE_NAME FROM BOOKS B "+
                "JOIN BOOKTAGS BT ON B.BOOK_ID = BT.BOOK_ID " +
                "JOIN TAGS TG ON BT.TAG_ID = TG.TAG_ID "+
                "JOIN BOOKGENRES BG ON B.BOOK_ID = BG.BOOK_ID "+
                "JOIN GENRES GS ON BG.GENRE_ID = GS.GENRE_ID "+
                "WHERE GS.GENRE_NAME = ? ";

        try(Connection c = connection()){
            PreparedStatement statement = c.prepareStatement(query);
            statement.setString(1, genreName);
            ResultSet rs = statement.executeQuery();

            while(rs.next()){
                Book newBook = fromResultSet(rs);
                if(newBook != null){
                    books.add(newBook);
                }
            }
            rs.close();
        }
        return books;
    }

    @Override
    public Booklist searchAuthor(String author) throws SQLException{
        Booklist books = new Booklist();
        String query = "SELECT B.BOOK_ID, B.BOOK_NAME, B.BOOK_AUTHOR, B.BOOK_DATE, " +
                "TG.TAG_NAME, GS.GENRE_NAME FROM BOOKS B "+
                "JOIN BOOKTAGS BT ON B.BOOK_ID = BT.BOOK_ID " +
                "JOIN TAGS TG ON BT.TAG_ID = TG.TAG_ID "+
                "JOIN BOOKGENRES BG ON B.BOOK_ID = BG.BOOK_ID "+
                "JOIN GENRES GS ON BG.GENRE_ID = GS.GENRE_ID "+
                "WHERE B.BOOK_AUTHOR = ? ";

        try(Connection c = connection()){
            PreparedStatement statement = c.prepareStatement(query);
            statement.setString(1, author);
            ResultSet rs = statement.executeQuery();

            while(rs.next()){
                Book newBook = fromResultSet(rs);
                if(newBook != null){
                    books.add(newBook);
                }
            }
            rs.close();
        }
        return books;
    }

    @Override
    public Booklist searchName(String bookName) throws SQLException{
        Booklist books = new Booklist();
        String query = "SELECT B.BOOK_ID, B.BOOK_NAME, B.BOOK_AUTHOR, B.BOOK_DATE, " +
                "TG.TAG_NAME, GS.GENRE_NAME FROM BOOKS B "+
                "JOIN BOOKTAGS BT ON B.BOOK_ID = BT.BOOK_ID " +
                "JOIN TAGS TG ON BT.TAG_ID = TG.TAG_ID "+
                "JOIN BOOKGENRES BG ON B.BOOK_ID = BG.BOOK_ID "+
                "JOIN GENRES GS ON BG.GENRE_ID = GS.GENRE_ID "+
                "WHERE B.BOOK_NAME = ? ";

        try(Connection c = connection()){
            PreparedStatement statement = c.prepareStatement(query);
            statement.setString(1, bookName);
            ResultSet rs = statement.executeQuery();

            while(rs.next()){
                Book newBook = fromResultSet(rs);
                if(newBook != null){
                    books.add(newBook);
                }
            }
            rs.close();
        }
        return books;
    }

    @Override
    public Booklist getBookList() throws SQLException{
        Booklist books = new Booklist();
        String query = "SELECT B.BOOK_ID, B.BOOK_NAME, B.BOOK_AUTHOR, B.BOOK_DATE, " +
                "TG.TAG_NAME, GS.GENRE_NAME FROM BOOKS B "+
                "JOIN BOOKTAGS BT ON B.BOOK_ID = BT.BOOK_ID " +
                "JOIN TAGS TG ON BT.TAG_ID = TG.TAG_ID "+
                "JOIN BOOKGENRES BG ON B.BOOK_ID = BG.BOOK_ID "+
                "JOIN GENRES GS ON BG.GENRE_ID = GS.GENRE_ID ";

        try(Connection c = connection()){
            PreparedStatement statement = c.prepareStatement(query);
            ResultSet rs = statement.executeQuery();

            while(rs.next()){
                Book newBook = fromResultSet(rs);
                if(newBook != null){
                    books.add(newBook);
                }
            }
            rs.close();
        }
        return books;
    }
}
