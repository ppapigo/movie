package com.example.movie.person;

import com.example.movie.common.IngestResult;
import com.example.movie.movie.TmdbMovieDTO;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

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

    //DB에서 쿼리하여 test와 똑같은 결과가 나오게
    @GetMapping("/all")
    public TmdbPersonResponse allActor(
            @RequestParam(required = false, name = "page", defaultValue = "1") @Min(1) int page,
            @RequestParam(required = false, name = "size", defaultValue = "12") @Min(1) @Max(100) int size
    ){
        TmdbPersonResponse response =new TmdbPersonResponse();

        Page<TmdbPersonDTO> results =
                personService.findAllByOrderByName(page,size);

        response.setResults(results.getContent());
        response.setPage(results.getNumber());
        response.setPageSize(results.getSize());
        response.setTotalPages((long) results.getTotalPages());
        response.setTotalResults((long) results.getTotalElements());

        return response;

    }
    @GetMapping("/top10")
    public List<TmdbPersonDTO> top10(){

        return personService
                .findTop10ByOrderByPopularityDesc();
    }

    @GetMapping("/{id}/movie")
    public List<TmdbMovieDTO> movies(
           @PathVariable("id")Long id
    ){
        System.out.println(id);

        return personService.movies(id);
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
