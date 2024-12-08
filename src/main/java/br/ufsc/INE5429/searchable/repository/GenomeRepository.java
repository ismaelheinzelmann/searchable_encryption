package br.ufsc.INE5429.searchable.repository;

import br.ufsc.INE5429.searchable.document.Genome;
import java.util.List;
import java.util.Optional;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GenomeRepository extends MongoRepository<Genome, String> {
    List<Genome> findAll();

    Optional<Genome> findById(String id);

    Optional<Genome> findGenomeByEncryptedGenome(String encryptedGenome);
}
