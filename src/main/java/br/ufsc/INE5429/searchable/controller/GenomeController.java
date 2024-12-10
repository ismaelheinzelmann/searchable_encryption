package br.ufsc.INE5429.searchable.controller;

import br.ufsc.INE5429.searchable.dto.ResponseMessage;
import br.ufsc.INE5429.searchable.dto.request.GenomeIndexesRequest;
import br.ufsc.INE5429.searchable.dto.request.RegisterGenomeRequest;
import br.ufsc.INE5429.searchable.dto.response.ErrorResponse;
import br.ufsc.INE5429.searchable.service.GenomeService;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/genomes")
@RequiredArgsConstructor
public class GenomeController {

    private final GenomeService genomeService;

    @PostMapping("/register")
    public ResponseEntity<ResponseMessage> registerGenome(
            @Valid @RequestBody RegisterGenomeRequest genomeRequest) {
        try {

            return genomeService.registerGenome(genomeRequest);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new ErrorResponse(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @PostMapping("/indexes")
    public ResponseEntity<ResponseMessage> getIndexes(
            @Valid @RequestBody GenomeIndexesRequest request) {
        try {
            return genomeService.getIndexes(request);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new ErrorResponse(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @PostMapping("/indexes-linear")
    public ResponseEntity<ResponseMessage> getIndexesLinear(
            @Valid @RequestBody GenomeIndexesRequest request) {
        try {
            return genomeService.getIndexesLinear(request);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new ErrorResponse(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping()
    public ResponseEntity<ResponseMessage> getGenomes() {
        try {
            return genomeService.getGenomes();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new ErrorResponse(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}
