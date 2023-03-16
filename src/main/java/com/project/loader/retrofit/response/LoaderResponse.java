package com.project.loader.retrofit.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class LoaderResponse {
    @SerializedName("files")
    @Expose
    private List<FileResponse> files;

    @SerializedName("folders")
    @Expose
    private List<String> folders;

    public List<FileResponse> getFiles() {
        return files;
    }

    public List<String> getFolders() {
        return folders;
    }
}
