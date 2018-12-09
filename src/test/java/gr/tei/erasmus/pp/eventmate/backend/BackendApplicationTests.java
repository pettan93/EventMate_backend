package gr.tei.erasmus.pp.eventmate.backend;

import gr.tei.erasmus.pp.eventmate.backend.repository.EventRepository;
import gr.tei.erasmus.pp.eventmate.backend.repository.PermissionRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class BackendApplicationTests {

    @Autowired
    PermissionRepository permissionRepository;

    @Autowired
    EventRepository eventRepository;

    @Test
    public void contextLoads() {
    }

    @Test
    public void test() {

//        System.out.println(permissionRepository.findPermissionsByUserIdAndUserRole(1L, UserRole.EVENT_OWNER));

//        System.out.println("------------");
//        System.out.println(eventRepository.findAllById(Collections.singletonList(5L)));

    }




}
