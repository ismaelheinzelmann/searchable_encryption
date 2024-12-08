package br.ufsc.INE5429.searchable.dto.response;

import br.ufsc.INE5429.searchable.dto.ResponseMessage;
import lombok.*;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ErrorResponse extends ResponseMessage {
    @NonNull private String message;
}
