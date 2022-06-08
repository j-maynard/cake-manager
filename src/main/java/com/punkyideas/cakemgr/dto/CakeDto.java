package com.punkyideas.cakemgr.dto;

import com.google.gson.Gson;

import java.util.Objects;

public class CakeDto {

    private final int id;
    private final String title;
    private final String description;
    private final String image;

    public CakeDto(int id, String title, String description, String image) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.image = image;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getImage() {
        return image;
    }

    public String toJSON() {
        var gson = new Gson();
        return gson.toJson(this);
    }

    @Override
    public String toString() {
        return "ID = " + this.id + ", Title = '" + this.title + "', Description = '" + this.description + "', Image = '" + this.image + "'";
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof CakeDto)) return false;
        CakeDto cake = (CakeDto) obj;
        return Objects.equals(title, cake.title)
                && Objects.equals(description, cake.description)
                && Objects.equals(image, cake.image);
    }

    @Override
    public int hashCode() {
        return Objects.hash(title,description,image);
    }
}
