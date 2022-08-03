package com.huangrx.cache;

import com.huangrx.cache.entity.Book;
import com.huangrx.cache.repo.BookRepository;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class AppRunner implements CommandLineRunner {

	private static final Logger logger = LoggerFactory.getLogger(AppRunner.class);

	private final BookRepository bookRepository;

	public AppRunner(BookRepository bookRepository) {
		this.bookRepository = bookRepository;
	}

	@Override
	public void run(String... args) throws Exception {
		//logger.info(".... Fetching books");
		//logger.info("isbn-1234 -->" + bookRepository.getByIsbn("isbn-1234"));
		//logger.info("isbn-4567 -->" + bookRepository.getByIsbn("isbn-4567"));
        //
		//logger.info("以上两个查询一定从数据库查询，再根据cacheenable中的条件确定是否缓存");
        //
		//logger.info("isbn-1234 -->" + bookRepository.getByIsbn("isbn-1234"));
		//logger.info("isbn-4567 -->" + bookRepository.getByIsbn("isbn-4567"));
        //
		//logger.info("获取缓存中的数据，有的不需要查数据库");
        //
        //
		//Book book = new Book("isbn-4567", "更新过的book","");
		//bookRepository.update(book);
        //
		//logger.info("isbn-1234 -->" + bookRepository.getByIsbn("isbn-1234"));
		//logger.info("isbn-4567 -->" + bookRepository.getByIsbn("isbn-4567"));
        //
		//try {
		//	bookRepository.clear();
		//} catch (Exception e) {
		//	e.printStackTrace();
		//}
        //
        //
		//logger.info("isbn-1234 -->" + bookRepository.getByIsbn("isbn-1234"));
		//logger.info("isbn-4567 -->" + bookRepository.getByIsbn("isbn-4567"));

        log.info(".... Fetching books");
        log.info("isbn-123456 -->" + bookRepository.getByIsbnNoArgs());

        log.info("下面用缓存？");
        log.info("isbn-123456 -->" + bookRepository.getByIsbnNoArgs());
	}



}