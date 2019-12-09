package si.fri.rso.lib.responses;

import javax.persistence.criteria.CriteriaBuilder;
import java.util.ArrayList;

public class NewFileMetadata {
    private String filePath;
    private String fileName;
    private String fileType;
    private Integer userId;
    private Integer channelId;
    private ArrayList<String> fileLabels;

    public NewFileMetadata(String filePath, String fileName, String fileType, Integer userId, Integer channelId, ArrayList<String> fileLabels){

        this.filePath = filePath;
        this.fileName = fileName;
        this.fileType = fileType;
        this.userId = userId;
        this.channelId = channelId;
        this.fileLabels = fileLabels;

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

    public ArrayList<String> getFileLabels() {
        return fileLabels;
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

    public void setFileLabels(ArrayList<String> fileLabels) {
        this.fileLabels = fileLabels;
    }
}
