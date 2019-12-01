package si.fri.rso.lib.responses;

public class DTO {
    private ChannelBucketName data;
    private String message;
    private Integer status;

    public ChannelBucketName getData() {
        return data;
    }

    public Integer getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public void setData(ChannelBucketName data) {
        this.data = data;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}
