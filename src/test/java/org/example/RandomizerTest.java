package org.example;

import org.junit.jupiter.api.Test;

import java.lang.reflect.InvocationTargetException;

import static org.junit.jupiter.api.Assertions.*;

class RandomizerTest {

    Randomizer randomizer = new Randomizer();

    @Test
    void getRandomInt() {
        System.out.println(randomizer.GetRandomInt());
    }

    @Test
    void getRandomFloat() {
        for (int i = 0; i < 50; i++) {
//            System.out.println("next");
            float x = randomizer.GetRandomFloat();
//            System.out.println(x);
            assertAll(() -> assertTrue(x >= 0F),
                    () -> assertTrue(x < 1.0F));
        }
    }

    @Test
    void getRandomIntRandged() {
        for (int i = 1; i < 50; i++) {
//            System.out.println("next");
            int x = randomizer.GetRandomInt(i, 2 * i);
//            System.out.println(x);
            assertTrue(x >= i);
            assertTrue(x < 2 * i);
        }
    }

    @Test
    void getRandomFloatRandged() {
        for (int i = 1; i < 50; i++) {
//            System.out.println("next");
            float x = randomizer.GetRandomFloat(i, 2 * i);
//            System.out.println(x);
            assertTrue(x >= i);
            assertTrue(x < 2 * i);
        }
    }

    @Test
    void getRandomString() {
        System.out.println(randomizer.GetRandomString());
    }

    public enum Gender {
        MALE, FEMALE
    }
    public static class A {
        int x1;
        float x2;
        private String kek;
        private Gender gender;
    };

    @Test
    void instantiateRandomized() {
        System.out.println("yoy");
        Object objkek = null;
        try {
            objkek = randomizer.InstantiateRandomized(A.class);
        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
        System.out.println("yay");
    }
}