<script src="https://asciinema.org/a/14.js" id="asciicast-14" async></script>

# b2w-game-app-backend-quarkus

B2W Game (quarkus)

"Fast and furious" keeper, all-in-one, simple (though hard to scale)

See app dir for sources. Uses quarkus, resteasy, mongodb

## Running the app in dev mode

```
mvn quarkus:dev
```

## Packaging 

```
mvn clean package
```

Produces the `b2w-game-app-backend-quarkus-1.0.0-runner.jar` file in the `/target` directory.
(it’s not an _über-jar_ as the dependencies are copied into the `target/lib` directory)

## Running 

The app is runnable using `java -jar target/b2w-game-app-backend-quarkus-1.0.0-runner.jar`.

## Creating a native executable (GraalVM/mandrel needed)

You can create a native executable: `mvn clean package -Pnative`-Dmaven.test.test.skip=true`

You can then execute natively: `./target/b2w-game-app-backend-quarkus-1.0.0-runner`

<link rel="alternate" type="application/x-asciicast" href="data/b2w-game.cast">

