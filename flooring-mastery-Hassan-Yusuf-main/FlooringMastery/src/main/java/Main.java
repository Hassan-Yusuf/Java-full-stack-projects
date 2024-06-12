import UserIO.UserIO;
import UserIO.UserIOImpl;
import controller.Controller;
import dao.*;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import service.OrderService;
import service.OrderServiceImpl;
import view.View;

import javax.naming.ldap.Control;

public class Main {
    public static void main(String[] args){
        ClassPathXmlApplicationContext appContext = new ClassPathXmlApplicationContext("classpath:applicationContext.xml");
        Controller controller = appContext.getBean("Controller",Controller.class);
        controller.runnable();
    }
}
