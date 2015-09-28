export PATH=/services/apache-maven-3.0.5/bin/:$PATH
export JAVA_HOME=/services/jdk1.7.0_21
export MAVEN_OPTS=-Xmx512m

mvn clean install -Pstaging -Dmaven.test.skip=true
