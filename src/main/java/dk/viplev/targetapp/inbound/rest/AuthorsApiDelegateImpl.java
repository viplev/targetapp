package dk.viplev.targetapp.inbound.rest;

import org.springframework.stereotype.Component;

import dk.viplev.targetapp.adapter.inbound.rest.AuthorsApiDelegate;
import dk.viplev.targetapp.port.inbound.AuthorService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@Component
@RequiredArgsConstructor
@Slf4j
public class AuthorsApiDelegateImpl implements AuthorsApiDelegate {

    private final AuthorService authorService;


}
