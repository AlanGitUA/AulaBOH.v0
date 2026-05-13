# springboot-bff-archetype

Arquetipo Maven usado como base para crear componentes backend del proyecto AulaBOH.

## Instalar localmente

```bash
mvn install
```

## Generar un proyecto

```bash
mvn archetype:generate \
  -DarchetypeGroupId=cl.aulaboh \
  -DarchetypeArtifactId=springboot-bff-archetype \
  -DarchetypeVersion=1.0.0 \
  -DgroupId=cl.aulaboh \
  -DartifactId=nuevo-componente \
  -Dpackage=cl.aulaboh.nuevo
```
