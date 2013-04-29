# AMP Quickstart Archetype

Generates an AMP Quick Start project with a properly wired Spring Application Context, necessary Maven dependencies, and an example application.

## Usage

From the shell, execute the following command:

```bash

# We've been experiencing trouble with Maven/Nexus in generating archetypes by this command,
# but this should be the appropriate way to start.

mvn archetype:generate                                                               \
  -DarchetypeRepository=nexus.bericotechnologies.com/content/repositories/releases   \
  -DarchetypeGroupId=amp.archetypes                                                  \
  -DarchetypeArtifactId=quickstart                                                   \
  -DarchetypeVersion=3.1.0                        
  
# Alternatively, use this command and choose the AMP Quickstart Archetype (we know this works):

mvn archetype:generate  \
  -DarchetypeCatalog=http://nexus.bericotechnologies.com/content/repositories/releases/archetype-catalog.xml
```

Follow the instructions displayed in the prompt.
