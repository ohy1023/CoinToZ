package com.example.financialfinalproject.domain.entity;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Setter

public class TradingDiary {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String ord_type; // 주문방식
    private String uuid; // 매수 주문 고유 ID
    private String side;
    private LocalDateTime created_at; //주문시간
    private String market; // 코인
    private Double price; // 매수가격
    private Double volume; // 수량
    private String comment; // 메모

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

}
