package com.huangrx.cache.repo;

import com.huangrx.cache.entity.Book;

public interface BookRepository {

    Book getByIsbn(String isbn);

	Book getByIsbnNoArgs();

	Book update(Book book);

	void clear();

}