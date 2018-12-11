# RabbitES
A small and direct bridge between RabbitMQ and MapR-ES

This project is built using `Maven`. 

```bash
mvn clean compile package
```

Execute the following command to run it.

```bash
java -cp "/opt/mapr/lib/*:rabbites-1.0.0.jar:." -Djava.library.path=/opt/mapr/lib com.github.anicolaspp.rabbites.App -r localhost -w 2 -q hello --stream /user/mapr/streams/bridge -t mytopic
```

This will create 2 workers (`-w 2`) connect them to `localhost` (`-r localhost`) that read from queue `hello` (`-q hello`). Each message published to RabbitMQ is disributed to the workers for better balancing and work distribution. Each worker will write each message to `/user/mapr/streams/bridge` (`--stream /user/mapr/streams/bridge`) on the specified topic `mytopic` (`-t mytopic`).

![](./IMG_1042.PNG)

