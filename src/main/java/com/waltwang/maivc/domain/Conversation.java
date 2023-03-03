package com.waltwang.maivc.domain;

import com.vladmihalcea.hibernate.type.json.JsonType;
import javax.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@TypeDefs({@TypeDef(name = "json", typeClass = JsonType.class)})
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Conversation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long id;

    private Long usermId;

    @Type(type = "json")
    @Column(name = "messages", columnDefinition = "json")
    private Map<String,Object> messages = new HashMap<>();
}
