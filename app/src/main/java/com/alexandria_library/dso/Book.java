package com.alexandria_library.dso;

import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Objects;

public class Book {

    // instance variables
    private int id;
    private String name;
    private String author;
    private LocalDate date;
    private String[] tags;
    private String[] genres;

    @RequiresApi(api = Build.VERSION_CODES.O)
    public Book (int bookID, String bookName, String bookAuthor, String bookDate,
                 String[] bookTags, String[] bookGenres) {
        id = bookID;
        name = bookName;
        author = bookAuthor;
        date = LocalDate.parse(bookDate);
        tags = bookTags;
        genres = bookGenres;
    }

    public int getID() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getAuthor() {
        return author;
    }

    public LocalDate getDate() {
        return date;
    }

    public String[] getTags() {
        return tags;
    }

    public String[] getGenres() {
        return genres;
    }


    public void setID(int bookID) {
        id = bookID;
    }

    public void setName(String bookName) {
        name = bookName;
    }

    public void setAuthor(String bookAuthor) {
        author = bookAuthor;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void setDate(String bookDate) {
        date = LocalDate.parse(bookDate);
    }

    public void setDate(LocalDate bookDate) {
        date = bookDate;
    }

    public void setTags(String[] bookTags) {
        tags = bookTags;
    }

    public void setGenres(String[] bookGenres) {
        genres = bookGenres;
    }

    public boolean equals(final Book book) {
        return Objects.equals(this.id, book.getID()) &&
                Objects.equals(this.name, book.getName()) &&
                Objects.equals(this.author, book.getAuthor()) &&
                Objects.equals(this.date, book.getDate()) &&
                Arrays.equals(this.tags, book.getTags()) &&
                Arrays.equals(this.genres, book.getGenres());
    }

    @NonNull
    @Override
    public String toString() {
        return "Book{" +
                "id = " + id +
                ", name = '" + name + '\'' +
                ", author = '" + author + '\'' +
                ", date = " + date.toString() +
                ", tag(s) = '" + Arrays.toString(tags) + '\'' +
                ", genre(s) = '" + Arrays.toString(genres) + '\'' +
                '}';
    }

}
