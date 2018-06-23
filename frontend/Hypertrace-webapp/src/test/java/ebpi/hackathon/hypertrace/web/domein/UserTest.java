package ebpi.hackathon.hypertrace.web.domein;

import org.junit.Test;

public class UserTest {
    @Test
    public void testUsers(){
        User user = new User();
        user.setType("niet mijn type");
        user.setFullName("Rose Tyler");
        user.setUsername("companion");
        user.setPassword("itsBigger");
        assert(user.getType().equals("niet mijn type"));
        assert(user.getFullName().equals("Rose Tyler"));
        assert(user.getPassword().equals("itsBigger"));
        assert(user.getUsername().equals("companion"));



    }

}
