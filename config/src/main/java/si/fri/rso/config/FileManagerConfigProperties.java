package si.fri.rso.config;

import com.kumuluz.ee.configuration.cdi.ConfigBundle;
import com.kumuluz.ee.configuration.cdi.ConfigValue;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
@ConfigBundle("rest-config")
public class FileManagerConfigProperties {

    @ConfigValue(value = "catalog-api-url", watch = true)
    private String catalogApiUrl;

    @ConfigValue(value = "file-storage-api-url", watch = true)
    private  String fileStorageApiUrl;

    @ConfigValue(value = "delete-catalog-api-url", watch = true)
    private String deletecatalogApiUrl;

    public String getDeletecatalogApiUrl() {
        return deletecatalogApiUrl;
    }

    public void setDeletecatalogApiUrl(String deletecatalogApiUrl) {
        this.deletecatalogApiUrl = deletecatalogApiUrl;
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
