package com.alexandria_library.logic;

import com.alexandria_library.application.Service;
import com.alexandria_library.data.IUserPersistent;
import com.alexandria_library.dso.Book;
import com.alexandria_library.dso.Booklist;
import com.alexandria_library.dso.IReader;
import com.alexandria_library.dso.User;
import com.alexandria_library.logic.Exception.BooklistException;

public class DefaultBooklist implements IDefaultBooklist {
    private final IUserPersistent data;
    public  DefaultBooklist() {
        data = Service.getUserPersistent();
    }
    public  DefaultBooklist(IUserPersistent data) {
        this.data = data;
    }

    private void checkDuplicate(Booklist booklist, Booklist newBook) throws BooklistException {
        Booklist compareList = new Booklist(newBook);
        compareList.retainAll(booklist);
        if (!compareList.isEmpty()) {
            boolean firstWord = true;
            String bookNames = "";
            for (Book book : compareList) {
                if (firstWord) {
                    bookNames = book.getName();
                    firstWord = false;
                }
                else {
                    bookNames += (", "+ book.getName());
                }
            }
            throw new BooklistException(String.format("The book(s) %s is already in list %s", bookNames, booklist.getName()));
        }
    }

    @Override
    public void addBookToAll(IReader reader, Booklist newBook) throws BooklistException {
        Booklist all = reader.getAllBooksList();
        checkDuplicate(all, newBook);
        all.addAll(newBook);
        data.addBookToAllList(newBook, (User) reader);
    }

    @Override
    public void addBookToInProgress(IReader reader, Booklist newBook) throws BooklistException {
        Booklist inProgress = reader.getInProgressList();
        checkDuplicate(inProgress, newBook);
        inProgress.addAll(newBook);
        data.addBookToReadingList(newBook, (User) reader);
    }

    @Override
    public void addBookToFinished(IReader reader, Booklist newBook) throws BooklistException {
        Booklist finished = reader.getFinishedList();
        checkDuplicate(finished, newBook);
        finished.addAll(newBook);
        data.addBookToFinishedList(newBook, (User) reader);
    }

    private void checkExist(Booklist oldList, Booklist removeList) throws BooklistException {
        Booklist temp = new Booklist(removeList);
        temp.retainAll(oldList);
        if (!removeList.equals(temp)) {
            temp = new Booklist(removeList);
            temp.removeAll(oldList);
            boolean firstWord = true;
            String bookNames = "";
            for (Book book : temp) {
                if (firstWord) {
                    bookNames = book.getName();
                    firstWord = false;
                }
                else {
                    bookNames += (", "+ book.getName());
                }
            }
            throw new BooklistException(String.format("The book(s) %s is not in list %s", bookNames, oldList.getName()));
        }
    }

    @Override
    public void removeBookFromAll(IReader reader, Booklist books) throws BooklistException {
        Booklist all = reader.getAllBooksList();
        checkExist(all, books);
        all.removeAll(books);
        data.deleteUserAllListBook(books, (User) reader);
    }

    @Override
    public void removeBookFromInProgress(IReader reader, Booklist books) throws BooklistException {
        Booklist inProgressList = reader.getInProgressList();
        checkExist(inProgressList, books);
        inProgressList.removeAll(books);
        data.deleteReadingListBook(books, (User) reader);
    }

    @Override
    public void removeBookFromFinished(IReader reader, Booklist books) throws BooklistException {
        Booklist finishedList = reader.getFinishedList();
        checkExist(finishedList, books);
        finishedList.removeAll(books);
        data.deleteFinishedListBook(books, (User) reader);
    }
}
