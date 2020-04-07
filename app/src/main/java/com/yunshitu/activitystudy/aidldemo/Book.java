package com.yunshitu.activitystudy.aidldemo;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

/**
 * @author : liudouliang
 * @date : 2020/3/4 15:29
 * @ des   :
 */
public class Book implements Parcelable {
    private int bookId;
    private String bookName;

    public int getBookId() {
        return bookId;
    }

    public void setBookId( int bookId ) {
        this.bookId = bookId;
    }

    public String getBookName() {
        return bookName;
    }

    public void setBookName( String bookName ) {
        this.bookName = bookName;
    }

    public Book( int bookId, String bookName ) {
        this.bookId = bookId;
        this.bookName = bookName;
    }

    public Book( Parcel in ) {
        bookId = in.readInt();
        bookName = in.readString();
    }

    public static final Creator<Book> CREATOR = new Creator<Book>() {
        @Override
        public Book createFromParcel( Parcel in ) {
            return new Book(in);
        }

        @Override
        public Book[] newArray( int size ) {
            return new Book[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel( Parcel dest, int flags ) {
        dest.writeInt(bookId);
        dest.writeString(bookName);
    }

    @NonNull
    @Override
    public String toString() {
        StringBuilder sp = new StringBuilder();
        sp.append("id:").append(bookId).append(", name:").append(bookName);
        return super.toString();
    }
}
