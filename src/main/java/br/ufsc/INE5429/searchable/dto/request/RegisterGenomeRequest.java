package br.ufsc.INE5429.searchable.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegisterGenomeRequest {
    private String genome;
}
