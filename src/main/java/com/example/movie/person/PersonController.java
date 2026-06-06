package com.example.movie.person;

import com.example.movie.common.IngestResult;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/api/person")
@RestController
@RequiredArgsConstructor
public class PersonController {

    private final PersonService personService;

    @GetMapping("/test")
    public TmdbPersonResponse fetchAll(@RequestParam(required = false, defaultValue = "1") @Min(1) int page){
        return personService.fetchAll(page);
    }

    @PostMapping("/sync")
    public IngestResult sync(
            @RequestParam(required = false, defaultValue = "1")@Min(1) int page
    ){
        return personService.sync(page);
    }

    @GetMapping
    public TmdbPersonResponse search(
            @RequestParam(required = false)String pname,
            @RequestParam(required = false, name = "page", defaultValue = "1") @Min(1) int page,
            @RequestParam(required = false, name = "size", defaultValue = "12") @Min(1) @Max(100) int size
    ){
        TmdbPersonResponse response =new TmdbPersonResponse();

        if(pname!=null) {
            Page<TmdbPersonDTO> results =
                    personService.findByName(pname,page,size);

            response.setResults(results.getContent());
            response.setPage(results.getNumber());
            response.setPageSize(results.getSize());
            response.setTotalPages((long) results.getTotalPages());
            response.setTotalResults((long) results.getTotalElements());
        }

        return response;
    }
}
