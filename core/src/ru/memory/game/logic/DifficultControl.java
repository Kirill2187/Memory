package ru.memory.game.logic;

public class DifficultControl {

    public static final int EASY_W = 4, EASY_H = 3, EASY_S = 80;
    public static final int NORMAL_W = 6, NORMAL_H = 4, NORMAL_S = 60;
    public static final int HARD_W = 8, HARD_H = 4, HARD_S = 60;
    public static final int IMPOSSIBLE_W = 10, IMPOSSIBLE_H = 6, IMPOSSIBLE_S = 50;

    public static final float DIFFICULT_COEFFICIENT_EASY = 1f;
    public static final float DIFFICULT_COEFFICIENT_NORMAL = 2f;
    public static final float DIFFICULT_COEFFICIENT_HARD = 2.8f;
    public static final float DIFFICULT_COEFFICIENT_IMPOSSIBLE = 5f;

    public static String[] CARDS = {"bear", "buffalo", "chick", "chicken", "cow", "crocodile",
            "dog", "duck", "elephant", "frog", "giraffe", "goat", "gorilla", "hippo", "horse",
            "monkey", "moose", "narwhal", "owl", "panda", "parrot", "penguin", "pig", "rhino",
            "sloth", "snake", "walrus", "whale", "zebra", "rabbit"};

}
