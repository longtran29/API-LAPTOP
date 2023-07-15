package com.springboot.laptop.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;


@Entity
@Table(name = "post_images")
@Getter
@Setter
public class PostImage extends ImageBase{

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private ArticleEntity article;
}
