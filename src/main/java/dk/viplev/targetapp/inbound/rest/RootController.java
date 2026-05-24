package dk.viplev.targetapp.inbound.rest;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.RedirectView;

import io.swagger.v3.oas.annotations.Hidden;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/")
public class RootController {
    
    @Hidden
    @GetMapping
    public RedirectView redirectToSwagger() {
        log.info("GET / - redirecting to Swagger UI");
        return new RedirectView("/swagger-ui/index.html");
    }
}
