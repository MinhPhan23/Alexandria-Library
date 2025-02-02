package com.alexandria_library.application;

import com.alexandria_library.data.IBookPersistent;
import com.alexandria_library.data.IUserPersistent;
import com.alexandria_library.data.hsqldb.BookPersistentHSQLDB;
import com.alexandria_library.data.hsqldb.UserPersistentHSQLDB;

public class Service {
    private static IUserPersistent userPersistent = null;
    private static IBookPersistent bookPersistent = null;

    /******
     * get user persistence
     * @return
     */
    public static synchronized IUserPersistent getUserPersistent(){
        if(userPersistent == null){
            userPersistent = new UserPersistentHSQLDB(Main.getDBPathName());
        }
        return userPersistent;
    }

    /******
     * get book persistence
     * @return
     */
    public static synchronized IBookPersistent getBookPersistent(){
        if(bookPersistent == null){
            bookPersistent = new BookPersistentHSQLDB(Main.getDBPathName());
        }
        return bookPersistent;
    }
}
