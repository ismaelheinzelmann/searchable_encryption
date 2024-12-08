package br.ufsc.INE5429.searchable.repository;

import br.ufsc.INE5429.searchable.document.Genome;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface GenomeRepository extends MongoRepository<Genome, String> {
    Optional<Genome> findById(String id);

    Optional<Genome> findGenomeByEncryptedGenome(String encryptedGenome);

    @Query(value = "{ '_id': ?0, 'index.?1': { $exists: true } }", fields = "{ 'index.?1': 1 }")
    Optional<List<Integer>> findIndexValuesByIdAndKey(String id, String key);
}
