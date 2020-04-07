package com.yunshitu.activitystudy.aidldemo;

import com.yunshitu.activitystudy.aidldemo.Book;
import com.yunshitu.activitystudy.aidldemo.OnBookArriveListener;

interface IBookManager{
  List<Book> getBookList();
  void addBook(in Book book);
  void registerLestener(OnBookArriveListener listener);
  void unregisterLestener(OnBookArriveListener listener);
}