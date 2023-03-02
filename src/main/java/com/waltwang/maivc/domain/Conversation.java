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

    @Type(type = "json")
    @Column(name = "messages", columnDefinition = "json")
    private Map<String,Object> messages = new HashMap<>();

    @Type(type = "json")
    @Column(name = "model_parameters", columnDefinition = "json")
    private Map<String,Object> modelParameters = new HashMap<>();

    @ManyToMany(cascade = CascadeType.MERGE)
    @JoinTable(
            name = "CONVERSATION_USERM_RELATION",
            joinColumns = @JoinColumn(name = "CONVERSATION_ID"),
            inverseJoinColumns = @JoinColumn(name = "USERM_ID")
    )
    private Set<Userm> userms = new HashSet<>();
}
