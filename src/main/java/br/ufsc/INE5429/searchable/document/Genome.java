package br.ufsc.INE5429.searchable.document;

import java.util.HashMap;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "genomes")
public class Genome {
    @Id private String id;
    private String encryptedGenome;
    private HashMap<String, List<Integer>> index;
    private String description;
}
