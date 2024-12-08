package br.ufsc.INE5429.searchable.dto.request;

import javax.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegisterGenomeRequest {
    @NotBlank private String genome;
    @NotBlank private String description;
}
