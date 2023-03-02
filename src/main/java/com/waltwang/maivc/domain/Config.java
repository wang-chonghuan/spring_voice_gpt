package com.waltwang.maivc.domain;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Config {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long id;
    private String value;

    @Enumerated(EnumType.ORDINAL)
    private KeyName key;
    public enum KeyName {
        OPEN_AI_APPKEY
    }
}
