package org.example;

import org.junit.jupiter.api.Test;

import java.lang.reflect.InvocationTargetException;

import static org.junit.jupiter.api.Assertions.*;

class RandomizerTest {

    Randomizer randomizer = new Randomizer();

    @Test
    void getRandomInt() {
        System.out.println(randomizer.getRandomInt());
    }

    @Test
    void getRandomFloat() {
        for (int i = 0; i < 50; i++) {
            float x = randomizer.getRandomFloat();
            assertAll(() -> assertTrue(x >= 0F),
                    () -> assertTrue(x < 1.0F));
        }
    }

    @Test
    void getRandomIntRandged() {
        for (int i = 1; i < 50; i++) {
            int x = randomizer.getRandomInt(i, 2 * i);
            assertTrue(x >= i);
            assertTrue(x < 2 * i);
        }
    }

    @Test
    void getRandomFloatRandged() {
        for (int i = 1; i < 50; i++) {
            float x = randomizer.getRandomFloat(i, 2 * i);
            assertTrue(x >= i);
            assertTrue(x < 2 * i);
        }
    }

    @Test
    void getRandomString() {
        System.out.println(randomizer.getRandomString());
    }

    public enum Gender {
        MALE, FEMALE
    }
    public static class A {
        int some_int;
        float some_float;
        private String some_string;
        private Gender some_gender;
    }

    @Test
    void seedSetter() {
        randomizer.setSeed(5);
    }

    @Test
    void instantiateRandomized() {
        // easy to see changes in debug mode
        A some_object = null;
        try {
            some_object = randomizer.instantiateRandomized(A.class);
        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }
}