# teleport-coordinate-logger
A Minecraft teleport coordinate "exploit" that works by intercepting & decrypting network traffic between the client and the server to read player teleport packets.

### Features
- Log coordinates of all players teleporting from within render distance (even if their destination is outside of it)
- Requires no client modifications and as such works with every game client, vanilla or not

### Requirements
- Java 8 or higher
- Minecraft version 1.12.2+ // 1.13.2+ // 1.14.1+

### Basic usage
[Download](https://github.com/mircokroon/teleport-coordinate-logger/releases) the latest release and execute the jar file using the commandline by running:

```java -jar coord-logger.jar -s address.to.server.com```

Then connect to ```localhost``` in Minecraft to log coordinates.


### Options
|  **Parameter** | **Default** | **Description** |
| --- | --- | --- |
|  --server | *required* | Server address |
|  --port | 25565 | Server port |
|  --local-port | 25565 | Local server port |
|  --minecraft | %appdata%/.minecraft | Path to your Minecraft installation, used for Mojang authentication |
