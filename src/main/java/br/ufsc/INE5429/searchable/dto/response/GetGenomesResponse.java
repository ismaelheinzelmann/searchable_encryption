package br.ufsc.INE5429.searchable.dto.response;

import br.ufsc.INE5429.searchable.dto.GenomeDTO;
import br.ufsc.INE5429.searchable.dto.ResponseMessage;
import java.util.List;
import lombok.*;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetGenomesResponse extends ResponseMessage {
    @NonNull private List<GenomeDTO> genomes;
}
