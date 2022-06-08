package com.daou.shoppingmall.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Mileage {
    @Id
    @SequenceGenerator(name = "mileage_id_sequence", sequenceName = "mileage_id_sequence", initialValue = 1, allocationSize = 50)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "mileage_id_sequence")
    @Column(name = "MILEAGE_ID")
    private Long id;

    @Column(name = "BALANCE")
    private BigDecimal balance;

    @OneToOne(fetch = FetchType.LAZY)
    @JsonBackReference
    @JoinColumn(name = "MEMBER_ID")
    private Member member;
}
