# Murder
Spigot mini game plugin


### Install

Run `docker compose up -d`.

The command builds a spigot server and populates the server files into `./spigot`.

With `docker attach murder_spigot` you can attach to the running container and issue commands to the mc console.

To build the plugin run `./gradlew clean shadowJar`. It will automatically copy the compiled plugin into the `./spigot/plugins` directory