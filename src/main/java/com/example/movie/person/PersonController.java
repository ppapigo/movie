package com.example.movie.person;

import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/person")
@RestController
@RequiredArgsConstructor
public class PersonController {

    private final PersonService personService;

    @GetMapping("/test")
    public TmdbPersonResponse fetchAll(@RequestParam(required = false, defaultValue = "1") @Min(1) int page){
        return personService.fetchAll(page);
    }
}
