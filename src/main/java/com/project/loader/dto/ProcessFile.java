package com.project.loader.dto;

import com.project.loader.utils.MD5Utils;
import okhttp3.ResponseBody;
import retrofit2.Call;

import java.io.File;

public class ProcessFile {
    private final File file;
    private final String fileHash;
    private final String serverHash;
    private final long serverSize;
    private final String serverPath;

    private Call<ResponseBody> call;

    public ProcessFile(File file, String hash, long serverSize, String serverPath) {
        this.file = file;
        this.fileHash = MD5Utils.getMd5Hash(file.getAbsolutePath());
        this.serverHash = hash;
        this.serverSize = serverSize;
        this.serverPath = serverPath;
    }

    public ProcessFile setCall(Call<ResponseBody> call) {
        this.call = call;
        return this;
    }

    public File getFile() {
        return file;
    }

    public String getFileHash() {
        return fileHash;
    }

    public String getServerHash() {
        return serverHash;
    }

    public long getServerSize() {
        return serverSize;
    }

    public String getServerPath() {
        return serverPath;
    }

    public Call<ResponseBody> getCall() {
        return call;
    }

    public boolean filterFilesToRequest() {
        return !file.exists() || fileHash == null || !fileHash.equals(serverHash) || file.length() != serverSize;
    }
}
