package com.daou.shoppingmall.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * 멤버 엔터티
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Entity
public class Member extends BaseEntity implements Serializable {
    @Id
    @SequenceGenerator(name = "member_id_sequence", sequenceName = "member_id_sequence", initialValue = 1, allocationSize = 50)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "member_id_sequence")
    @Column(name = "MEMBER_ID")
    private Long id;
    @Column(name = "NAME")
    private String name;

    @OneToMany(mappedBy = "member")
    @OrderBy("REFERENCE_PRICE desc")
    @JsonManagedReference
    private List<Coupon> coupons = new ArrayList<>();

    @OneToMany(mappedBy = "member")
    private List<Order> order = new ArrayList<>();

    @OneToMany(mappedBy = "member")
    @OrderBy("EXPIRY_DATE asc")
    @JsonManagedReference
    private List<Point> points = new ArrayList<>();

    @OneToOne(mappedBy = "member", fetch = FetchType.LAZY)
    @JsonManagedReference
    private Mileage mileage;
}
