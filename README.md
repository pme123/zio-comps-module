# ZIO: Creating Components with different Implementations

![image](https://user-images.githubusercontent.com/3437927/70376847-bfb80980-190d-11ea-99e8-a452e3d1560d.png)

We want to provide an infrastructure for loading Components (configurations) with different technologies.

The idea is to experiment with the reusability of the ZIO modules.

So all that is different should be handled by the implementation of the _Service_.

Then the _App_ itself just provides the implementation without adjusting the program itself.

    object YamlApp extends CompApp {
    
      def run(args: List[String]): ZIO[zio.ZEnv, Nothing, Int] =
        program.provide(
          new Console.Live with YamlComps {
          }
        )
    }

So all that is different in the _HoconApp_ is `with YamlComps` is `with HoconComps`.

Sounds great, right! Sadly I am not there yet.

See: https://stackoverflow.com/questions/59225054/delegate-to-a-more-specific-context-bound-additional-implicit-parameter

The project consists of 3 modules:
1. `core`: 
    * Domain Model: Different Configuration Components, like DBConnections etc.
    * The Service Interface (as a _ZIO_ module)
    * Generic Program (`CompApp`) that contains the program flow.
    

# Buildtool Mill

## Update dependencies in Intellij

    mill mill.scalalib.GenIdea/idea