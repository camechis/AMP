# AMP Quickstart Archetype

Generates an AMP Quick Start project with a properly wired Spring Application Context, necessary Maven dependencies, and an example application.

## Usage

From the shell, execute the following command:

```bash
 
# This is just the standard mvn archetype:generate command, except it points to 
# the nexus repository that contains the archetype (it's not in maven central)
mvn archetype:generate  \
  -DarchetypeCatalog=http://nexus.bericotechnologies.com/content/repositories/releases/archetype-catalog.xml
```

Follow the instructions displayed in the prompt.  Once complete, you can cd into your project's directory and run the start script:

```bash
# runs your program, compiling it first if needed
sh bin/amp-start
```
