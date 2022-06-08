package com.punkyideas.cakemgr.dto;

import com.google.gson.Gson;

import java.util.Objects;

public class MessageDto {

    private String message;

    public MessageDto(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public String toString() {
        return "Message: '" + this.message + "'";
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof MessageDto)) return false;
        MessageDto msg = (MessageDto) obj;
        return Objects.equals(message, msg.message);
    }

    @Override
    public int hashCode() {
        return Objects.hash(message);
    }

    public String toJSON() {
        var gson = new Gson();
        return gson.toJson(this);
    }
}
