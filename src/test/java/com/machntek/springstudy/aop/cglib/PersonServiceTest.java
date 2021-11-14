package com.machntek.springstudy.aop.cglib;

import org.junit.jupiter.api.Test;
import org.springframework.cglib.beans.BeanGenerator;
import org.springframework.cglib.proxy.Enhancer;
import org.springframework.cglib.proxy.FixedValue;
import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.Mixin;

import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.*;

class PersonServiceTest {

    @Test
    public void returningSameValue() {
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(PersonService.class);
        enhancer.setCallback((FixedValue) () -> "Hello Tom!");
        PersonService proxy = (PersonService) enhancer.create();

        String res = proxy.sayHello(null);
        assertEquals("Hello Tom!", res);
    }

    @Test
    public void returningDependsOnMethodSignature() {
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(PersonService.class);
        enhancer.setCallback((MethodInterceptor) (obj, method, args, proxy) -> {
            if(method.getDeclaringClass() != Object.class && method.getReturnType() == String.class) {
                return "Hello Tom!";
            } else {
                return proxy.invokeSuper(obj, args);
            }
        });

        PersonService proxy = (PersonService) enhancer.create();

        assertEquals("Hello Tom!", proxy.sayHello(null));
        int lengthOfName = proxy.lengthOfName("Mary");

        assertEquals(4, lengthOfName);
    }


    // POJO 만드는데 사용 가능
    @Test
    public void beanCreator() throws Exception {
        BeanGenerator beanGenerator = new BeanGenerator();

        beanGenerator.addProperty("name", String.class);
        Object myBean = beanGenerator.create();
        Method setter = myBean.getClass().getMethod("setName", String.class);
        setter.invoke(myBean, "some string value set by a cglib");

        Method getter = myBean.getClass().getMethod("getName");
        assertEquals("some string value set by a cglib", getter.invoke(myBean));
    }

    @Test
    public void mixinObject() throws Exception {
        Mixin mixin = Mixin.create(
                new Class[] { Interface1.class, Interface2.class, MixinInterface.class },
                new Object[] { new Class1(), new Class2() }
        );
        MixinInterface mixinDelegate = (MixinInterface) mixin;

        assertEquals("first behaviour", mixinDelegate.first());
        assertEquals("second behaviour", mixinDelegate. second());
    }

    public interface Interface1 {
        String first();
    }
    public interface Interface2 {
        String second();
    }
    public class Class1 implements Interface1 {
        @Override
        public String first() {
            return "first behaviour";
        }
    }
    public class Class2 implements Interface2 {
        @Override
        public String second() {
            return "second behaviour";
        }
    }
    public interface MixinInterface extends Interface1, Interface2 {}
}
