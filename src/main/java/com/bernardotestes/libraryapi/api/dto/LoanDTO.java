package com.bernardotestes.libraryapi.api.dto;
import lombok.*;
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoanDTO {
  private String isbn;
  private String customer;
}
