package dk.viplev.targetapp.inbound.rest;

import org.springframework.stereotype.Component;

import dk.viplev.targetapp.adapter.inbound.rest.BooksApiDelegate;
import dk.viplev.targetapp.port.inbound.BookService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class BooksApiDelegateImpl implements BooksApiDelegate{

    private final BookService bookService;


}
