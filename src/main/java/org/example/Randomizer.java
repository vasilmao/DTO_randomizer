package org.example;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;

public class Randomizer {
    private long seed = 1;
    private final long multiplier = 0x5DEECE66DL;
    private final long increment = 0xBL;
    private final long modulus = (1L << 48) - 1;
    private final String letters_pool = "abcdefjhijklmnopqrstuvwxyz";

    // a, b, m from java util random, >> (48 - bits) == you get a number of {$bits} length
    // & (m-1) instead of % m, because m is power of to, so m - 1 is consecutive ones
    // also no problem with negative numbers
    public int Next(int bit) {
        seed = (multiplier * seed + increment) & modulus;
        return (int) (seed >> (48 - bit));
    }

    public int GetRandomInt() {
        return Next(32);
    }


    // from including, to excluding
    public int GetRandomInt(int from, int to) {
        int diff_modulo = to - from;
        return (((GetRandomInt() % diff_modulo) + diff_modulo) % diff_modulo) + from;
    }

    public short GetRandomShort() {
        return (short) Next(16);
    }

    public byte GetRandomByte() {
        return (byte) Next(4);
    }

    public boolean GetRandomBool() {
        return Next(1) == 1;
    }

    // generate higher and lower bits separately
    public long GetRandomLong() {
        return ((long) Next(32) << 32) + Next(32);
    }

    // turns 0..2^24 int range to 0.0f..1.0f
    public float GetRandomFloat() {
        return Next(Float.PRECISION) / ((float) (1 << Float.PRECISION));
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
