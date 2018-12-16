package gr.tei.erasmus.pp.eventmate.backend;


import org.junit.Test;

public class Tst {

    @Test
    public void testStream(){

//        List<Obj> list = Arrays.asList(new Obj(1L),new Obj(2L));
//
//
//        List<Long> ids = list.stream().map(Obj::getId).collect(Collectors.toList());
//
//        System.out.println(ids);





    }


    class Obj{
        Long id;

        public Obj(Long id) {
            this.id = id;
        }

        public Long getId() {
            return id;
        }
    }
}
