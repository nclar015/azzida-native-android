package com.azzida.model;
import java.util.ArrayList;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GetRecentModelData {

    @SerializedName("post")
    @Expose
    private ArrayList<Post> post = null;
    @SerializedName("applied")
    @Expose
    private ArrayList<Applied> applied = null;

    public ArrayList<Post> getPost() {
        return post;
    }

    public void setPost(ArrayList<Post> post) {
        this.post = post;
    }

    public ArrayList<Applied> getApplied() {
        return applied;
    }

    public void setApplied(ArrayList<Applied> applied) {
        this.applied = applied;
    }

}
