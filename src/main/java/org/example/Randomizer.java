package org.example;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;

public class Randomizer {
    private long seed = 1;
    private final long a = 0x5DEECE66DL;
    private final long c = 0xBL;
    private final long m = (1L << 48) - 1;
    private final String letters_pool = "abcdefjhijklmnopqrstuvwxyz";

    // a, b, m from java util random, >> (48 - bits) == you get a number of {$bits} length
    public int next(int bit) {
        seed = (a * seed + c) & m;
//        System.out.println(seed);
        return (int) (seed >> (48 - bit));
    }

    public int GetRandomInt() {
        return next(32);
    }


    // from including, to excluding
    public int GetRandomInt(int from, int to) {
        int diff_modulo = to - from;
        return (((GetRandomInt() % diff_modulo) + diff_modulo) % diff_modulo) + from;
    }

    public short GetRandomShort() {
        return (short) next(16);
    }

    public byte GetRandomByte() {
        return (byte) next(4);
    }

    public boolean GetRandomBool() {
        return next(1) == 1;
    }

    // generate higher and lower bits separately
    public long GetRandomLong() {
        return ((long) next(32) << 32) + next(32);
    }

    // turns 0..2^24 int range to 0.0f..1.0f
    public float GetRandomFloat() {
        return next(Float.PRECISION) / ((float) (1 << Float.PRECISION));
    }

    public float GetRandomFloat(float from, float to) {
        return GetRandomFloat() * (to - from) + from;
    }

    public String GetRandomString() {
        int length = GetRandomInt(5, 20);
        StringBuilder ans = new StringBuilder();
        for (int i = 0; i < length; i++) {
            ans.append(letters_pool.charAt(GetRandomInt(0, letters_pool.length())));
        }
        return ans.toString();
    }

    public Object InstantiateRandomized(Class<?> cls) throws InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {

        Object obj =  cls.getDeclaredConstructor().newInstance();
        ArrayList<Field> fields = new ArrayList<>(Arrays.asList(cls.getDeclaredFields()));
        for (Field field : fields) {
            System.out.println(field.getType().getSimpleName());
            field.setAccessible(true);
            if (field.getType().isEnum()) {
                Object[] enumConstants = field.getType().getEnumConstants();
                field.set(obj, enumConstants[GetRandomInt(0, enumConstants.length)]);
            } else if (field.getType().equals(Integer.class) || field.getType().equals(int.class)){
                field.set(obj, GetRandomInt());
            } else if (field.getType().equals(Float.class) || field.getType().equals(float.class)) {
                field.set(obj, GetRandomFloat());
            } else if (field.getType().equals(String.class)) {
                field.set(obj, GetRandomString());
            }
        }
        return obj;
    }

    public void setSeed(long new_seed) {
        seed = new_seed;
    }
}
