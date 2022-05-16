# How to use

### Examples

[An example can be found here](https://github.com/254nm/PacketAPI/blob/master/Example/src/main/java/me/txmc/protocolapiexample/ProtocolAPIExample.java)

### Repositories

```xml

<repository>
    <id>txmc-repo</id>
    <url>https://repo.txmc.me/releases</url>
</repository>
```

### Dependencies (maven)

```xml
<dependency>
    <groupId>com.destroystokyo.paper</groupId>
    <artifactId>paper-jar</artifactId>
    <version>1.12.2-R0.1-SNAPSHOT</version>
    <scope>provided</scope>
</dependency>
<dependency>
    <groupId>me.txmc</groupId>
    <artifactId>protocolapi</artifactId>
    <version>1.2-SNAPSHOT</version>
    <scope>compile</scope>
</dependency>
```
### Dependencies (gradle kts)
```kotlin
repositories {
    mavenLocal()
    maven { url = uri("https://repo.txmc.me/releases") }
}

dependencies {
    implementation("me.txmc:protocolapi:1.2-SNAPSHOT")
    compileOnly("com.destroystokyo.paper:paper-jar:1.12.2-R0.1-SNAPSHOT")
}
```
