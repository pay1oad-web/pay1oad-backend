package com.pay1oad.homepage.dto;

import java.util.List;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ResponseDTO<T> {
    private String error;
    private List<T> data;
}
