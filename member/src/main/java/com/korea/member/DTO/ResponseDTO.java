package com.korea.member.DTO;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;


@Builder
@AllArgsConstructor
@Data
public class ResponseDTO<T> {
		
		private String error;
		private List<T> data;
}
