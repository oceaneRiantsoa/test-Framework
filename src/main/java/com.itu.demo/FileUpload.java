package com.itu.demo;

public class FileUpload {
    private String fileName;
    private byte[] fileBytes;
    private String contentType;

    public FileUpload(String fileName, byte[] fileBytes, String contentType) {
        this.fileName = fileName;
        this.fileBytes = fileBytes;
        this.contentType = contentType;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public byte[] getFileBytes() {
        return fileBytes;
    }

    public void setFileBytes(byte[] fileBytes) {
        this.fileBytes = fileBytes;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public int getSize() {
        return fileBytes != null ? fileBytes.length : 0;
    }
}
