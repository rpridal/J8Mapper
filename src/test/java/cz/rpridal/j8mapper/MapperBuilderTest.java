package cz.rpridal.j8mapper;

import static org.junit.Assert.assertEquals;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.Test;

public class MapperBuilderTest {

    class Source {
        String s;
        int i;

        public String getS() {
            return s;
        }

        public void setS(String s) {
            this.s = s;
        }

        public void setI(int i) {
            this.i = i;
        }

        public int getI() {
            return i;
        }
    }

    class Target {
        String t;
        int i;

        public String getT() {
            return t;
        }

        public void setT(String t) {
            this.t = t;
        }

        public void setI(int i) {
            this.i = i;
        }

        public int getI() {
            return i;
        }

        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + getOuterType().hashCode();
            result = prime * result + i;
            result = prime * result + ((t == null) ? 0 : t.hashCode());
            return result;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj)
                return true;
            if (obj == null)
                return false;
            if (getClass() != obj.getClass())
                return false;
            Target other = (Target) obj;
            if (!getOuterType().equals(other.getOuterType()))
                return false;
            if (i != other.i)
                return false;
            if (t == null) {
                if (other.t != null)
                    return false;
            }
            else if (!t.equals(other.t))
                return false;
            return true;
        }

        private MapperBuilderTest getOuterType() {
            return MapperBuilderTest.this;
        }
        
        
    }

    @Test
    public void mappingSimpleObjectBuilder() {
        Source s = new Source();
        s.setI(55);
        s.setS("text");
        Target target = MapperBuilder.start(Source.class, Target.class)
                .addMapping(Source::getS, Target::setT)
                .addMapping(Source::getI, Target::setI)
                .build()
                .map(s, Target::new);

        assertEquals("text", target.getT());
        assertEquals(55, target.getI());
    }
    
    @Test
    public void mappingSimpleObjectBuilderStaticData() {
        Source s = new Source();
        s.setI(55);
        s.setS("text");
        Target target = MapperBuilder.start(Source.class, Target.class)
                .addMapping(Source::getS, Target::setT)
                .addMapping(10, Target::setI)
                .build()
                .map(s, Target::new);

        assertEquals("text", target.getT());
        assertEquals(10, target.getI());
    }
    
    Source getNewSource(int i, String s){
        Source result = new Source();
        result.setI(i);
        result.setS(s);
        return result;
    }
    
    Target getNewTarget(int i, String t){
        Target result = new Target();
        result.setI(i);
        result.setT(t);
        return result;
    }
    
    
    
    @Test
    public void mappingListObjects() {
        List<Source> source = new LinkedList<Source>();
        source.add(getNewSource(1, "p1"));
        source.add(getNewSource(2, "p2"));
        
        List<Target> mapList = MapperBuilder.start(Source.class, Target.class)
                .addMapping(Source::getS, Target::setT)
                .addMapping(Source::getI, Target::setI)
                .build()
                .map(source, Target::new)
                .collect(Collectors.toList());

        assertEquals("p1", mapList.get(0).getT());
        assertEquals(1, mapList.get(0).getI());
        assertEquals("p2", mapList.get(1).getT());
        assertEquals(2, mapList.get(1).getI());
    }   
}
