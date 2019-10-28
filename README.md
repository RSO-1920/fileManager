# fileManager
it is a file Manager


## API

* Upload file: POST ```http://localhost:8089/v1/file/upload```
    ```  
  FORM_DATA
  
  file: <file>,
  integerUser: <int>
  integerChannel: <int>
    
  ```

* Delete file: DELETE ```http://localhost:8089/v1/file/delete/?fileId=<fileId>```


## RUN

```docker run -d --name rso1920-filemanager-api --network rso1920 -e KUMULUZEE_CONFIG_ETCD_HOSTS=http://etcd:2379 -p 8089:8089 rso1920/filemanager```