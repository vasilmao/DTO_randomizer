package org.example;


import lombok.Setter;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;

public class Randomizer {
    /**
     * -- SETTER --
     *  Forces
     *  to new value
     *
     */
    @Setter
    private long seed = 1;
    private final long multiplier = 0x5DEECE66DL;
    private final long increment = 0xBL;
    private final long modulus = (1L << 48) - 1;
    private final String letters_pool = "abcdefjhijklmnopqrstuvwxyz";

    /**
     * Function generates pseudorandom integer from 1 to 32 bits length
     * @param bit integer bit length
     * @return pseudorandom integer of {@code bit} length
     * @implNote
     * a, b, m from java util random, >> (48 - bits) == you get a number of {@code bits} length;
     * {@code &(m-1)} instead of {@code %m}, because m is power of to, so m - 1 is consecutive ones;
     * also no problem with negative numbers
     *
     */
    public int next(int bit) {
        seed = (multiplier * seed + increment) & modulus;
        return (int) (seed >> (48 - bit));
    }

    /**
     * Function generates pseudorandom integer from {@code 0} to {@code MAXINT}
     * @return pseudorandom integer of {@code bit} length
     * @implNote
     * returns {@code next(32)}
     *
     */
    public int getRandomInt() {
        return next(32);
    }


    /**
     * Function generates pseudorandom integer within given range
     * @param from lower bound (included)
     * @param to upper bound (excluded)
     * @return pseudorandom integer of {@code bit} length
     * @implNote
     * <pre>{@link Randomizer#getRandomInt()}{@code % (from - to) + to}</pre>
     *
     */
    public int getRandomInt(int from, int to) {
        int diff_modulo = to - from;
        return (((getRandomInt() % diff_modulo) + diff_modulo) % diff_modulo) + from;
    }

    /**
     * Function generates pseudorandom short
     * @return pseudorandom short
     * @implNote
     * returns {@code next(16)}
     *
     */
    public short getRandomShort() {
        return (short) next(16);
    }

    /**
     * Function generates pseudorandom byte
     * @return pseudorandom byte
     * @implNote
     * returns {@code next(4)}
     *
     */
    public byte getRandomByte() {
        return (byte) next(4);
    }

    /**
     * Function generates pseudorandom bool
     * @return pseudorandom bool
     * @implNote
     * returns {@code next(1)}
     *
     */
    public boolean getRandomBool() {
        return next(1) == 1;
    }

    /**
     * Function generates pseudorandom long
     * @return pseudorandom long
     * @implNote
     * generates higher and lower 32 bits separatly using {@code next(32)}
     *
     */
    public long getRandomLong() {
        return ((long) next(32) << 32) + next(32);
    }

    /**
     * Function generates pseudorandom float within range from 0.0 (including) to 1.0 (excluding)
     * using all possible bit values evenly
     * @return pseudorandom float
     * @implNote
     * returns turns 0...2<sup>24</sup>-1 range into 0.0...1.0 range, where 24 is float presicion bits
     *
     */
    public float getRandomFloat() {
        return next(Float.PRECISION) / ((float) (1 << Float.PRECISION));
    }

    /**
     * Function generates pseudorandom float within range from {@code from} (including) to {@code to} (excluding)
     * @param from lower bound (including)
     * @param to upper bound (excluding)
     * @return pseudorandom float
     * @implNote
     * <pre>{@link Randomizer#getRandomFloat()}{@code * (to - from) + to}</pre>
     *
     */
    public float getRandomFloat(float from, float to) {
        return getRandomFloat() * (to - from) + from;
    }

    /**
     * Function generates random string of random length (from 5 to 20) using random lower anglish alphabet letters
     * @return random string
     * @implNote
     * randomize length, then generate {@code length} random letters
     */
    public String getRandomString() {
        int length = getRandomInt(5, 20);
        StringBuilder ans = new StringBuilder();
        for (int i = 0; i < length; i++) {
            ans.append(letters_pool.charAt(getRandomInt(0, letters_pool.length())));
        }
        return ans.toString();
    }

    /**
     * Function generates object of given class, and sets all integer/float/string/enum type fields to random values
     * @param cls Class type
     * @return Object type value with randomized fields
     * @throws InstantiationException when it can't create instance of the object
     * @throws IllegalAccessException when it can't access to constructor or fields
     * @throws InvocationTargetException when it can't create instance of the object
     * @throws NoSuchMethodException when it can't find constructor
     * @implNote first gets constructor and creates instance, then runs through all fields, and sets random
     * value if type is familiar
     *
     */
    public <T> T instantiateRandomized(Class<T> cls) throws InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {

        T return_object =  cls.getDeclaredConstructor().newInstance();
        Field[] fields = cls.getDeclaredFields();
        for (Field field : fields) {
            System.out.println(field.getType().getSimpleName());
            field.setAccessible(true);
            if (field.getType().isEnum()) {
                Object[] enumConstants = field.getType().getEnumConstants();
                field.set(return_object, enumConstants[getRandomInt(0, enumConstants.length)]);
            } else if (field.getType().equals(Integer.class) || field.getType().equals(int.class)){
                field.set(return_object, getRandomInt());
            } else if (field.getType().equals(Short.class) || field.getType().equals(short.class)) {
                field.set(return_object, getRandomShort());
            } else if (field.getType().equals(Long.class) || field.getType().equals(long.class)) {
                field.set(return_object, getRandomLong());
            } else if (field.getType().equals(Byte.class) || field.getType().equals(byte.class)) {
                field.set(return_object, getRandomByte());
            } else if (field.getType().equals(Boolean.class) || field.getType().equals(boolean.class)) {
                field.set(return_object, getRandomBool());
            } else if (field.getType().equals(Float.class) || field.getType().equals(float.class)) {
                field.set(return_object, getRandomFloat());
            } else if (field.getType().equals(String.class)) {
                field.set(return_object, getRandomString());
            }
        }
        return return_object;
    }

}
