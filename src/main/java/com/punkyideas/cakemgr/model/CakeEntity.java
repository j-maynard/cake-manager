package com.punkyideas.cakemgr.model;

import com.google.gson.annotations.SerializedName;

import org.hibernate.annotations.DynamicUpdate;

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@DynamicUpdate
@Table(name = "Cake", uniqueConstraints = {@UniqueConstraint(columnNames = "ID"), @UniqueConstraint(columnNames = "title")})
public class CakeEntity implements Serializable {

    private static final long serialVersionUID = -1798070786993154676L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", unique = true, nullable = false)
    private Integer id;

    @Column(name = "title", unique = true, nullable = false, length = 100)
    private String title;

    @Column(name = "description", unique = false, nullable = false, length = 100)
    @SerializedName("desc")
    private String description;

    @Column(name = "image", unique = false, nullable = false, length = 300)
    private String image;

    public static CakeEntity testCakeBuilder(int id, String title, String description, String image) {
        var cake = new CakeEntity();
        cake.id = id;
        cake.title = title;
        cake.description = description;
        cake.image = image;
        return cake;
    }

    public CakeEntity(String title, String description, String image) {
        this.title = title;
        this.description = description;
        this.image = image;
    }

    public CakeEntity() {

    }

    public Integer getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    @Override
    public String toString() {
        return "ID = " + this.id + ", Title = '" + this.title + "', Description = '" + this.description + "', Image = '"+ this.image + "'";
    }

    @Override
    public boolean equals(Object obj) {
        if(this == obj) return true;
        if(!(obj instanceof CakeEntity)) return false;
        CakeEntity cake = (CakeEntity) obj;
        return Objects.equals(title, cake.title)
                && Objects.equals(description, cake.description)
                && Objects.equals(image, cake.image);
    }

    @Override
    public int hashCode() {
        return Objects.hash(title,description,image);
    }
}