package com.korea.product2.DTO;

import java.util.List;
import java.util.stream.Collectors;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderDTO {
   
   private int orderId;//주문번호
   private int productId;//상품번호
   private String productName;//상품 이름
   private int productCount;//주문 개수
   private int productPrice;//상품 가격
   private int totalPrice;//결제 금액
   private String orderDate;//주문 날짜
   
   
   
   //Object[]에 들어있는 데이터를 OrderDTO로 변환
   public static List<OrderDTO> toListOrderDTO(List<Object[]> list){
      //Object -> Integer -> int
      return list.stream().map(result -> OrderDTO.builder()
                                    .orderId((int)result[0])
                                    .productName((String)result[1])
                                    .productCount((int)result[2])
                                    .productPrice((int)result[3])
                                    .totalPrice((int)result[4])
                                    .orderDate((String)result[5])
                                    .build()).collect(Collectors.toList());
   }
}

