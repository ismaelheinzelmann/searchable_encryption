package br.ufsc.INE5429.searchable.service;

import br.ufsc.INE5429.searchable.document.Genome;
import br.ufsc.INE5429.searchable.dto.ResponseMessage;
import br.ufsc.INE5429.searchable.dto.request.GenomeIndexesRequest;
import br.ufsc.INE5429.searchable.dto.request.RegisterGenomeRequest;
import br.ufsc.INE5429.searchable.dto.response.GenomeIndexesResponse;
import br.ufsc.INE5429.searchable.dto.response.RegisterGenomeResponse;
import br.ufsc.INE5429.searchable.repository.GenomeRepository;
import br.ufsc.INE5429.searchable.utils.CryptoUtils;
import org.springframework.stereotype.Service;
import org.springframework.http.ResponseEntity;

import java.util.*;

import static br.ufsc.INE5429.searchable.utils.ConfigUtils.loadOrCreateKey;
import static br.ufsc.INE5429.searchable.utils.CryptoUtils.hashGenomeSubsequence;

@Service
public class GenomeService {
    final GenomeRepository genomeRepository;
    private final String key;
    private final int K_SIZE = 30;

    public GenomeService(GenomeRepository genomeRepository) {
        this.genomeRepository = genomeRepository;
        this.key = loadOrCreateKey();

    }

    public ResponseEntity<ResponseMessage> registerGenome(RegisterGenomeRequest genome) throws Exception {
        if (genome.getGenome().length() < K_SIZE) {
            throw new IllegalArgumentException("Genome too short, it must have a size of " + K_SIZE + " or more");
        }
        if (genomeRepository.findGenomeByEncryptedGenome(CryptoUtils.encrypt(genome.getGenome(), key)).isPresent()) {
            throw new IllegalArgumentException("Genome already exists");
        }
        Genome newGenome = new Genome();
        newGenome.setEncryptedGenome(CryptoUtils.encrypt(genome.getGenome(), key));
        HashMap<String, List<Integer>> indexes = new HashMap<>();
        for (int i = 0; i <= genome.getGenome().length() - K_SIZE; i++) {
            String subsequence = genome.getGenome().substring(i, i + K_SIZE);
            String hash = hashGenomeSubsequence(subsequence);

            indexes.computeIfAbsent(hash, k -> new ArrayList<>()).add(i);
        }
        newGenome.setIndex(indexes);
        genomeRepository.save(newGenome);
        return ResponseEntity.ok(new RegisterGenomeResponse(newGenome.getId()));
    }

    public ResponseEntity<ResponseMessage> getIndexes(GenomeIndexesRequest request) throws Exception {
        var genome = genomeRepository.findById(request.getGenomeId());
        if (genome.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        if (request.getPattern().length() != K_SIZE) {
            return ResponseEntity.badRequest().build();
        }
        String hash = hashGenomeSubsequence(request.getPattern());
        return ResponseEntity.ok(new GenomeIndexesResponse(genome.get().getIndex().containsKey(hash) ? genome.get().getIndex().get(hash) : new ArrayList<>()));
    }
}
