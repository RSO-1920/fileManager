package si.fri.rso.lib.responses;

public class NewFileMetadataDTO {
    private String filePath;
    private String fileName;
    private String fileType;
    private Integer userId;
    private Integer channelId;

    public String getFilePath() {
        return this.filePath;
    }
    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getFileName() {
        return this.fileName;
    }
    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFileType() {
        return this.fileType;
    }
    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public Integer getUserId() {
        return this.userId;
    }
    public void  setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getChannelId() {
        return this.channelId;
    }
    public void setChannelId(Integer channelId) {
        this.channelId = channelId;
    }
}
