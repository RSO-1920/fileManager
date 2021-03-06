package si.fri.rso.config;

import com.kumuluz.ee.configuration.cdi.ConfigBundle;
import com.kumuluz.ee.configuration.cdi.ConfigValue;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
@ConfigBundle("rest-config")
public class FileManagerConfigProperties {

    @ConfigValue(value = "upload-catalog-api-uri", watch = true)
    private String catalogApiUri;

    @ConfigValue(value = "upload-file-storage-api-uri", watch = true)
    private  String fileStorageApiUri;

    @ConfigValue(value = "delete-file-storage-api-uri", watch = true)
    private  String deleteFileStorageUri;

    @ConfigValue(value = "delete-catalog-api-uri", watch = true)
    private String deleteCatalogApiUri;

    @ConfigValue(value = "channel-uri", watch = true)
    private String channelUri;

    @ConfigValue(value = "catalog-uri", watch = true)
    private String catalogUri;


    public void setDeleteCatalogApiUri(String deleteCatalogApiUrl) {
        this.deleteCatalogApiUri = deleteCatalogApiUrl;
    }

    public String getCatalogApiUri() {
        return this.catalogApiUri;
    }
    public void setCatalogApiUri(String catalogApiUri) {
        this.catalogApiUri = catalogApiUri;
    }

    public String getFileStorageApiUri() {
        return this.fileStorageApiUri;
    }
    public void setFileStorageApiUri(String fileStorageApiUri) {
        this.fileStorageApiUri = fileStorageApiUri;
    }

    public String getChannelUri() {
        return channelUri;
    }

    public void setChannelUri(String channelUri) {
        this.channelUri = channelUri;
    }

    public String getDeleteCatalogApiUri() {
        return deleteCatalogApiUri;
    }

    public String getCatalogUri() {
        return catalogUri;
    }
    public void setCatalogUri(String catalogUri) {
        this.catalogUri = catalogUri;
    }

    public String getDeleteFileStorageUri() {
        return deleteFileStorageUri;
    }

    public void setDeleteFileStorageUri(String deleteFileStorageUri) {
        this.deleteFileStorageUri = deleteFileStorageUri;
    }
}
