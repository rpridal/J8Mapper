# J8Mapper
Generic simply cleary mapper 

##Usage:
###Simple mapping
``` java 
Target target = MapperBuilder.start(Source.class, Target.class)
                .addMapping(Source::getS, Target::setT)
                .addMapping(Source::getI, Target::setI)
                .build()
                .map(s, Target::new);
```
###Collection mapping
``` java 
List<Target> mapList = MapperBuilder.start(Source.class, Target.class)
                .addMapping(Source::getS, Target::setT)
                .addMapping(Source::getI, Target::setI)
                .build()
                .map(source, Target::new)
                .collect(Collectors.toList());
```
###Static value mapping
``` java 
Target target = MapperBuilder.start(Source.class, Target.class)
                .addMapping(Source::getS, Target::setT)
                .addMapping(10, Target::setI)
                .build()
                .map(s, Target::new);
```
###Mapping calculation
``` java 
Target target = MapperBuilder.start(Source.class, Target.class)
                .addMapping(Source::getS, Target::setT)
                .addMapping(s -> s.getI() + 10, Target::setI)
                .build()
                .map(source, Target::new);
```
###Mapping via mapper
``` java 
Mapper<Source, Target> mapper = MapperBuilder.start(Source.class, Target.class)
                .addMapping(Source::getS, Target::setT)
                .addMapping(Source::getI, Target::setI)
                .build();
...
Target target = mapper.map(s, Target::new);
```
