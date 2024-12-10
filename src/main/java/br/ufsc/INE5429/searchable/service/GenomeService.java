package br.ufsc.INE5429.searchable.service;

import static br.ufsc.INE5429.searchable.utils.ConfigUtils.loadOrCreateKey;
import static br.ufsc.INE5429.searchable.utils.CryptoUtils.encrypt;
import static br.ufsc.INE5429.searchable.utils.CryptoUtils.hashGenomeSubsequence;

import br.ufsc.INE5429.searchable.document.Genome;
import br.ufsc.INE5429.searchable.dto.GenomeDTO;
import br.ufsc.INE5429.searchable.dto.ResponseMessage;
import br.ufsc.INE5429.searchable.dto.request.GenomeIndexesRequest;
import br.ufsc.INE5429.searchable.dto.request.RegisterGenomeRequest;
import br.ufsc.INE5429.searchable.dto.response.GenomeIndexesResponse;
import br.ufsc.INE5429.searchable.dto.response.GetGenomesResponse;
import br.ufsc.INE5429.searchable.dto.response.RegisterGenomeResponse;
import br.ufsc.INE5429.searchable.repository.GenomeRepository;
import br.ufsc.INE5429.searchable.utils.CryptoUtils;
import java.util.*;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class GenomeService {
    final GenomeRepository genomeRepository;
    private final String key;
    private final int K_SIZE = 30;

    public GenomeService(GenomeRepository genomeRepository) {
        this.genomeRepository = genomeRepository;
        this.key = loadOrCreateKey();
    }

    public ResponseEntity<ResponseMessage> registerGenome(RegisterGenomeRequest genome)
            throws Exception {
        if (genome.getGenome().length() < K_SIZE) {
            throw new IllegalArgumentException(
                    "Genome too short, it must have a size of " + K_SIZE + " or more");
        }

        if (genomeRepository
                .findGenomeByEncryptedGenome(CryptoUtils.encrypt(genome.getGenome(), key))
                .isPresent()) {
            throw new IllegalArgumentException("Genome already exists");
        }
        genome.setGenome(genome.getGenome().toUpperCase());
        String dnaGenome = convertRnaToDna(genome.getGenome());
        String cleanedGenome = cleanGenomeSequence(dnaGenome);

        Genome newGenome = new Genome();
        newGenome.setEncryptedGenome(CryptoUtils.encrypt(cleanedGenome, key));
        newGenome.setDescription(genome.getDescription());
        HashMap<String, List<Integer>> indexes = new HashMap<>();
        for (int i = 0; i <= cleanedGenome.length() - K_SIZE; i++) {
            String subsequence = cleanedGenome.substring(i, i + K_SIZE);
            String hash = hashGenomeSubsequence(subsequence);
            String encryptedHash = encrypt(hash, key);
            indexes.computeIfAbsent(encryptedHash, k -> new ArrayList<>()).add(i);
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
        String encryptedHash = encrypt(hash, key);
        return ResponseEntity.ok(
                new GenomeIndexesResponse(
                        genome.get().getIndex().containsKey(encryptedHash)
                                ? genome.get().getIndex().get(encryptedHash)
                                : new ArrayList<>()));
    }

    public ResponseEntity<ResponseMessage> getIndexesLinear(GenomeIndexesRequest request) throws Exception {
        var genome = genomeRepository.findById(request.getGenomeId());
        if (genome.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        if (request.getPattern().length() != K_SIZE) {
            return ResponseEntity.badRequest().build();
        }
        String decryptedGenome = CryptoUtils.decrypt(genome.get().getEncryptedGenome(), key);
        System.out.println(decryptedGenome.length());
        List<Integer> indexes = new ArrayList<>();
        String pattern = request.getPattern();
        for (int i = 0; i <= decryptedGenome.length() - K_SIZE; i++) {
            if (decryptedGenome.substring(i, i + K_SIZE).equals(pattern)) {
                indexes.add(i);
            }
        }
        if (indexes.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(
                new GenomeIndexesResponse(
                        indexes));

    }

    public ResponseEntity<ResponseMessage> getGenomes() {
        var genomes = genomeRepository.findAll();
        List<GenomeDTO> genomesList = new ArrayList<>();
        for (Genome genome : genomes) {
            genomesList.add(new GenomeDTO(genome.getId(), genome.getDescription()));
        }
        return ResponseEntity.ok(new GetGenomesResponse(genomesList));
    }

    private String cleanGenomeSequence(String genomeSequence) {
        return genomeSequence.replaceAll("[^ACGTN]", "");
    }

    private String convertRnaToDna(String rnaGenome) {
        return rnaGenome.replace('U', 'T');
    }
}
