# Before you development #

## Preparing properties files ##
Goto src/config and copy following files with specified name 

- app.properties.develop -> app.properties
- db.properties.develop -> db.properties
- mail.properties.develop -> mail.properties
- ../log4j2.develop -> ../log4j2.properties

Then setup your database configuration.

## Setup your IDE ##
Following instruction only tested on IntelliJ IDEA.

- Setup JDK 8 with specified name: "jdk 1.8.0" in File/Project Structure
- Setup Application Server in File/Settings/Build, Execution, Deployment
    - Name: "Tomcat 8.5"
    - Add "tomcat-jdbc.jar" into Libraries
- Set Unlimited Strength Crypto Policy
    - find file: java.security in your jdk and uncomment crypto.policy=unlimited
    - if you using jdk before 1.8u151 you should download JCE
 
## View Page ##
Open {ServerAddress}/login to login, in dev mode, only account will be verified.
- Account authorization methods can be found at services.account.*
- In dev mode (setting in app.properties), password is ignored when verify.

# Development Guide #
開發前先pull develop, 並從develop開branch
## Git ##
功能開頭用 "feature/", e.g., feature/add_login
修復開頭用 "fix/", e.g., fix/upload_bug
文件開頭用 "docs/", e.g., docs/init_specs

開發完畢後, push後到GitLab上提出merge request

## Changes of databases ##
於sql資料夾內建立執行的SQL檔案，檔名為 YYYYMMDD_HHmmss_description.sql, 
YYYYMMDD_HHmmss 為年月日和時間, description 輸入變動的原因 

## Coding Style ##
- 縮排使用四個空格
- Servlet一律放置於src/Servlet下，class name 結尾為Servlet
- Dao先建立Interface後於impl內實作, 類型一律為Interface
- Servlet網址使用lowerCamelCase, e.g., student/downloadFile
- method, attribute 使用lowerCamelCase, e.g., doPost(), getCurrentTime()
- DB表格/欄位全大寫, 單字間使用底線分隔

# Things You Should Notice #
- **不要**在Servlet寫attributes
- 使用Dao時必須帶入account或有implement HasSchoolSchema 的class，Account和BaseDao皆有實作此interface 
- 資料庫連線一律由DbConn.getConnection(HasSchoolSchema xxx)取得，必須傳入學校的schema，並使用完畢後要關閉連線，範例如下
```java
class Exapmle {
    private void foo() {
        try (Connection pcon = DbConn.getConnection(account)) {
            try(PreparedStatement preparedStatement = pcon.prepareStatement(sql)) {
                preparedStatement.setString(1, "string");
                preparedStatement.setInt(2, 456);
                List<Data> dataList = new ArrayList<>();
                try(ResultSet resultSet = preparedStatement.executeQuery()) {
                    while (resultSet.next()) {
                        dataList.add(resultSet.getString("key"));
                    }                    
                }
            }
            
            // Do something...
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
```

# Files Storage #
- Files that uploaded by normal user stored at WebContent\WEB-INF\storage\files\{idNo}, idNo is 身分證號碼
- Before you using Upload class at utils, you Upload.getSaveRootDir should be called first.

# About 課程成果審核通知 #
- Build this project as jar file and update properties files in jar file
- run scheduler.VerifyCourseRecordNotifier via command line, this project libraries and tomcat libraries are required.
  Example:
  ```bash
  java -Dfile.encoding=UTF-8 -classpath "%CATALINA_HOME%"\lib\*;"%WEB_ROOT%"\WebContent\WEB-INF\lib\* scheduler.VerifyCourseRecordNotifier
  ```
  
# TODO #
- Logging
- Error Handling
- Codes cleanup
- Remove unused table in database

T.B.C.
