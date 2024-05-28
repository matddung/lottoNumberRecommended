package com.studyjun.lotto.entitiy;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Getter
@Entity
@Setter
public class LottoNum {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    private int round;
    private int num1;
    private int num2;
    private int num3;
    private int num4;
    private int num5;
    private int num6;
    private int bonusNum;
    private int firstPlaceNumber;
}
