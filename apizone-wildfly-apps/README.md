apizone-hibernate-app
---
This app demonstrate JPA with hibernate using JSF based web app and class loading functionality using dependency on Apache PDFBox library.  



### Deploying Apache PDFBox dependency in WildFly as module

vim module.xml

    <module xmlns="urn:jboss:module:1.1" name="org.apache.pdfbox">

        <resources>
            <resource-root path="pdfbox-2.0.15.jar"/>
        </resources>

        <dependencies>
            <module name="org.apache.commons.logging"/>
            <module name="org.apache.commons.io"/>
        </dependencies>
    </module>


ssh jboss@dc | jboss@slave-host1 | jboss@slave-host2

    mkdir -p /app/units/modules/apache-pdfbox/jars
    cd /app/units/modules/apache-pdfbox
    cp /tmp/pdfbox-2.0.15.jar jars
    cp /tmp/module.xml .

    module add --name=system.layers.base.org.apache.pdfbox --resources=/app/units/modules/apache-pdfbox/jars/. --module-xml=/app/units/modules/apache-pdfbox/module.xml


### Removing the module

    module remove --name=system.layers.base.org.apache.pdfbox


### Rebounce the servers
    bin/jboss-cli.sh
    connect <DC-Host>:<DC-Port>
    :reload-servers

### Check Logs

**Slave 1:**

    tail -f /app/slave1/domain/servers/host16038-Server1/log/server.log

**Slave 2:**

    tail -f /app/slave2/domain/servers/host16039-Server1/log/server.log