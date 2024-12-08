package br.ufsc.INE5429.searchable.dto;

import lombok.Data;

import java.util.HashMap;
import java.util.List;

@Data
public class GenomeDTO {
    private String id;
    private String encryptedGenome;
    private HashMap<String, List<Integer>> index;
}
