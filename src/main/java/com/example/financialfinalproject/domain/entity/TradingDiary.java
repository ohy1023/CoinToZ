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
    private String bidUuid; // 매수 주문 고유 ID
    private String askUuid; // 매도 주문 고유 ID
    private LocalDateTime bid_created_at; // 매수 주문시간
    private String market; // 코인
    private LocalDateTime ask_created_at; // 매도 주문시간
    private Integer bid_price; // 매수가격
    private Integer ask_price; // 매도가격
    private Double volume; // 수량
    private Integer arbitrage; // 차익
    private Double revenue; // 수익률 (수수료반영)
    private String comment; // 메모

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

}
