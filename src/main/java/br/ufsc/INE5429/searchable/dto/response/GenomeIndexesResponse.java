package br.ufsc.INE5429.searchable.dto.response;

import br.ufsc.INE5429.searchable.dto.ResponseMessage;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GenomeIndexesResponse extends ResponseMessage {
    private List<Integer> indexes;
}
