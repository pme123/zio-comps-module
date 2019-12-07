# ZIO: Creating Components with different Implementations



The project consists of 3 modules:
1. `core`: 
    * Domain Model: Different Configuration Components, like DBConnections etc.
    * The Service Interface (as a _ZIO_ module)
    * Generic Program (`CompApp`) that contains the program flow.
    

# Buildtool Mill

## Update dependencies in Intellij

    mill mill.scalalib.GenIdea/idea