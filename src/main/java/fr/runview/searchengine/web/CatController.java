package fr.runview.searchengine.web;

import fr.runview.searchengine.domain.Cat;
import fr.runview.searchengine.dtos.ChatListResponse;
import fr.runview.searchengine.dtos.ErrorResponse;
import fr.runview.searchengine.services.CatService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


@Slf4j
@RestController
@RequestMapping("api/v1/chats")
public class CatController {

    private final CatService catService;

    public CatController(CatService catService) {
        this.catService = catService;
    }

    @PostMapping(value = "/upload", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public List<Cat> saveEmployee(@RequestPart MultipartFile file) throws Exception {
        return catService.uploadCSV(file);
    }

    @GetMapping("/{pageNumber}")
    public ChatListResponse findAll(@PathVariable("pageNumber") Integer pageNumber) {
        Page<Cat> found = catService.findAll(pageNumber);
        return ChatListResponse.builder()
                .cats(found.getContent())
                .totalPages(found.getTotalPages())
                .currentPage(found.getNumber())
                .build();
    }

    @GetMapping
    public ChatListResponse findAll() {
        return findAll(0);
    }


    @GetMapping("/females")
    public List<Cat> findAllFemales() {
        return catService.findAllFemales();
    }

    @GetMapping("/males")
    public List<Cat> findAllMales() {
        return catService.findAllMales();
    }

    @GetMapping("/filter/prices")
    public ResponseEntity<Object> filterByPrice(@RequestParam(value = "eq", required = false) Double eq,
                                                @RequestParam(value = "sup", required = false) Double sup,
                                                @RequestParam(value = "inf", required = false) Double inf,
                                                // borne
                                                @RequestParam(value = "min", required = false) Double between,
                                                @RequestParam(value = "max", required = false) Double and) {

        try {
            List<Cat> results = catService.filterByPrice(eq, sup, inf, new Double[]{between, and});
            return ResponseEntity.accepted().body(results);
        } catch (IllegalArgumentException e) {
            log.error(e.getMessage());
            return ResponseEntity.badRequest().body(new ErrorResponse(e.getMessage()));
        }
    }

    @GetMapping("/filter/age")
    public ResponseEntity<Object> filterByAge(@RequestParam(value = "eq", required = false) Integer eq,
                                              @RequestParam(value = "sup", required = false) Integer sup,
                                              @RequestParam(value = "inf", required = false) Integer inf,
                                              // borne
                                              @RequestParam(value = "min", required = false) Integer between,
                                              @RequestParam(value = "max", required = false) Integer and) {
        try {
            List<Cat> results = catService.filterByAge(eq, sup, inf, new Integer[]{between, and});
            return ResponseEntity.accepted().body(results);
        } catch (IllegalArgumentException e) {
            log.error(e.getMessage());
            return ResponseEntity.badRequest().body(new ErrorResponse(e.getMessage()));
        }
    }

    @PostMapping
    public ResponseEntity<Object> create(@RequestBody Cat cat) {
        try {
            Cat saved = catService.save(cat);
            return ResponseEntity.accepted().body(saved);
        } catch (IllegalArgumentException e) {
            log.error(e.getMessage());
            return ResponseEntity.badRequest().body(new ErrorResponse(e.getMessage()));
        }
    }

    @PutMapping
    public ResponseEntity<Object> update(@RequestBody Cat cat) {
        try {
            Cat saved = catService.update(cat);
            return ResponseEntity.accepted().body(saved);
        } catch (IllegalArgumentException e) {
            log.error(e.getMessage());
            return ResponseEntity.badRequest().body(new ErrorResponse(e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> delete(@PathVariable("id") Long id) {

        try {
            catService.delete(id);
            return ResponseEntity.ok().build();
        } catch (IllegalArgumentException e) {
            log.error(e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }

}
