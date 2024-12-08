package br.ufsc.INE5429.searchable.dto.response;

import br.ufsc.INE5429.searchable.dto.ResponseMessage;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ErrorResponse extends ResponseMessage {
    private String message;
}
