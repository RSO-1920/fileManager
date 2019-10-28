package si.fri.rso.config;

import com.kumuluz.ee.configuration.cdi.ConfigBundle;
import com.kumuluz.ee.configuration.cdi.ConfigValue;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
@ConfigBundle("rest-config")
public class FileManagerConfigProperties {

    @ConfigValue(value = "upload-catalog-api-url", watch = true)
    private String catalogApiUrl;

    @ConfigValue(value = "upload-file-storage-api-url", watch = true)
    private  String fileStorageApiUrl;

    @ConfigValue(value = "delete-catalog-api-url", watch = true)
    private String deleteCatalogApiUrl;

    public String getDeletecatalogApiUrl() {
        return deleteCatalogApiUrl;
    }

    public void setDeleteCatalogApiUrl(String deleteCatalogApiUrl) {
        this.deleteCatalogApiUrl = deleteCatalogApiUrl;
    }

    public String getCatalogApiUrl() {
        return this.catalogApiUrl;
    }
    public void setCatalogApiUrl(String catalogApiUrl) {
        this.catalogApiUrl = catalogApiUrl;
    }

    public String getFileStorageApiUrl() {
        return this.fileStorageApiUrl;
    }
    public void setFileStorageApiUrl(String fileStorageApiUrl) {
        this.fileStorageApiUrl = fileStorageApiUrl;
    }

}
