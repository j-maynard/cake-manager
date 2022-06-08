package com.punkyideas.cakemgr.dto;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Objects;

public class CreateCakeDto {

    @NotNull(message = "Cakes require a title.")
    @NotEmpty(message = "Cakes require a title.")
    private String title;

    @NotNull(message = "Cakes require a description.")
    @NotEmpty(message = "Cakes require a description.")
    private String description;

    @NotNull(message = "Cakes require an image")
    @NotEmpty(message = "Cakes require an image")
    private String image;

    public CreateCakeDto(String title, String description, String image) {
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

    @Override
    public String toString() {
        return "Title = '" + this.title + "', Description = '" + this.description + "', Image = '" + this.image + "'";
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof CreateCakeDto)) return false;
        CreateCakeDto cake = (CreateCakeDto) obj;
        return Objects.equals(title, cake.title)
                && Objects.equals(description, cake.description)
                && Objects.equals(image, cake.image);
    }

    @Override
    public int hashCode() {
        return Objects.hash(title,description,image);
    }
}
