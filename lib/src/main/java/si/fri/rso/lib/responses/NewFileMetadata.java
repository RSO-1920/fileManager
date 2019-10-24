package si.fri.rso.lib.responses;

import javax.persistence.criteria.CriteriaBuilder;

public class NewFileMetadata {
    private String filePath;
    private String fileName;
    private String fileType;
    private Integer userId;
    private Integer channelId;

    public NewFileMetadata(String filePath, String fileName, String fileType, Integer userId, Integer channelId){

        this.filePath = filePath;
        this.fileName = fileName;
        this.fileType = fileType;
        this.userId = userId;
        this.channelId = channelId;

    }

    public String getFilePath() {
        return filePath;
    }

    public String getFileName() {
        return fileName;
    }

    public String getFileType() {
        return fileType;
    }

    public Integer getUserId() {
        return userId;
    }

    public Integer getChannelId() {
        return channelId;
    }

    public void setUserId(Integer user_id) {
        this.userId = user_id;
    }

    public void setChannelId(Integer channel_id) {
        this.channelId = channel_id;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }
}
