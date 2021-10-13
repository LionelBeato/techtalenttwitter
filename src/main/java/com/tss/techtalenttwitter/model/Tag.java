package com.tss.techtalenttwitter.model;

import lombok.*;

import javax.persistence.*;
import java.util.List;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Tag {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "tag_id")
    private Long id;

    private String phrase;

    @ManyToMany(mappedBy = "tags")
    @ToString.Exclude
    private List<Tweet> tweets;


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Tag tag = (Tag) o;

        if (id != null ? !id.equals(tag.id) : tag.id != null) return false;
        if (phrase != null ? !phrase.equals(tag.phrase) : tag.phrase != null) return false;
        return tweets != null ? tweets.equals(tag.tweets) : tag.tweets == null;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (phrase != null ? phrase.hashCode() : 0);
        result = 31 * result + (tweets != null ? tweets.hashCode() : 0);
        return result;
    }
}
