package br.ufsc.INE5429.searchable.dto.response;

import br.ufsc.INE5429.searchable.dto.ResponseMessage;
import java.util.List;
import lombok.*;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class GenomeIndexesResponse extends ResponseMessage {
    @NonNull private List<Integer> indexes;
}
