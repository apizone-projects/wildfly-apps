WildFly Training
---

**Duration:** 18 Hours

1. **WildFly Introduction**
   
    Introduction to Java Application Servers and WildFly - History, Walkthrough, High Level setup

2. **WildFly Runtime**

    Walkthrough of WildFly runtime environment that includes Topology, Hosts, Server Groups and Profile - default, ha, full, full-ha

3. **WildFly Configuration - Part I**

    Configuration options for various profiles, interfaces, socket binding, System properties etc.

4. **WildFly Deployments**

    Java application deployments on WildFly. This includes modules (JAR, WAR, EAR) deployments through WildFly UI (HAL) and jboss-cli

5. **WildFly Configuration - Part II**

    More detailed configuration of WildFly in Standalone & Domain mode using standalone.xml or domain.xml  
    Further configurations of host-master and host-slaves for domain mode. JVM parameters, server groups, data sources, interfaces etc.


6. **WildFly Access Control**

    WildFly Default and RBAC based access controls. Users, Groups & Roles.

7. **Hands-on Lab**

    Hands-on lab for WildFly Configuration, Deployment and Runtime changes


---
  
JBoss CLI
---
$DOMAIN_HOME/bin/jboss-cli.sh

### Connect to JBoss Instance

    [disconnected /] connect DC_HOST:DC_PORT
    [domain@DC_HOST:9990 /]

    [domain@localhost:9990 /] quit


### Read All resources

    :read-resource


### Navigation

    cd profile=ha
    cd subsystem=logging | cd subsystem=datasources
    :read-resource(recursive="true")
    :read-operation-names

    ./root-logger=ROOT:change-root-log-level(level=WARN)


    cd /socket-binding-group=ha-sockets



### Read & Add a System Property

    /system-property=db-host:read-resource(recursive="true")

    /system-property=db-host:add(value="192.168.122.10")


### Using Control Flow

    if (outcome != success) of /system-property=db-host3:read-resource
        /system-property=db-host3:add(value="192.168.122.11")
    end-if


### List All Hosts

    :read-children-names(child-type=host)

### List all Interfaces    
    :read-children-names(child-type=interface)


### List All Servers on a Host

    /host=host16038-salve:read-children-names(child-type=server)  



### Start/Stop Servers

    /host=host16038-salve/server-config=host16038-Server1:stop
    /host=host16038-salve/server-config=host16038-Server1:start


### List All Deployments

    /deployment=*:query(select=["name","enabled"])


### Create Datasource

    connect <dc-host>:<dc-port>

    /profile=ha/subsystem=datasources/data-source=`<datasource-name>`:add(jndi-name="java:jboss/datasource/`<jndi-name>`",connection-url="jdbc:oracle:thin:@XXX.XXX.XXX.XXX:XXXX/<service-name>",driver-name=oracle,user-name=`<user-name>`,password=`<password>`,valid-connection-checker-class-name="org.jboss.jca.adapters.jdbc.extensions.oracle.OracleValidConnectionChecker",stale-connection-checker-class-name="org.jboss.jca.adapters.jdbc.extensions.oracle.OracleStaleConnectionChecker",exception-sorter-class-name="org.jboss.jca.adapters.jdbc.extensions.oracle.OracleExceptionSorter")
