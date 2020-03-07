package com.example.kouveepetshopapps.response;

import com.google.gson.annotations.SerializedName;

public class PostUpDelPelanggan {
    @SerializedName("error")
    String error;
    @SerializedName("message")
    String message;

    public PostUpDelPelanggan(String error, String message) {
        this.error = error;
        this.message = message;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
