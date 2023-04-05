mvn clean package

cp target/kie-server-extension-example1-1.0.0-SNAPSHOT.jar RHPAM_HOME/standalone/deployments/kie-server.war/WEB-INF/lib

curl -u username:password http://HOST:PORT/kie-server/services/rest/server/containers/myRestApi/restapi2/userId