package ru.netology;

import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class Main {

    static AtomicInteger counterThreeLength = new AtomicInteger(0);
    static AtomicInteger counterThreeLengthSameCharacter = new AtomicInteger(0);
    static AtomicInteger counterFourLength = new AtomicInteger(0);
    static AtomicInteger counterFourLengthSameCharacter = new AtomicInteger(0);
    static AtomicInteger counterFiveLength = new AtomicInteger(0);
    static AtomicInteger counterFiveLengthSameCharacter = new AtomicInteger(0);

    public static void main(String[] args) throws InterruptedException {
        Random random = new Random();
        String[] texts = new String[100_000];
        for (int i = 0; i < texts.length; i++) {
            texts[i] = generateText("abc", 3 + random.nextInt(3));
        }
        // Вообще одинаковые символы никак не повлияют на конечное число так как они пройдут и следующие две проверки, пэтому считать их особого смысла нет, разбивать на виды красивого числа задачи тоже нет, посчитаем просто чтобы было...
        Thread threadSameCharacter = getThread(texts, counterThreeLengthSameCharacter, counterFourLengthSameCharacter, counterFiveLengthSameCharacter, Main::isSameCharacter);

        Thread threadPalindrome = getThread(texts, counterThreeLength, counterFourLength, counterFiveLength, Main::isPalindrome);

        Thread threadIncreasingSequence = getThread(texts, counterThreeLength, counterFourLength, counterFiveLength, Main::isIncreasingSequence);

        threadSameCharacter.start();
        threadPalindrome.start();
        threadIncreasingSequence.start();

        threadSameCharacter.join();
        threadPalindrome.join();
        threadIncreasingSequence.join();

        System.out.println("Красивых слов с длиной 3: " + counterThreeLength + " шт");
        System.out.println("Красивых слов с длиной 4: " + counterFourLength + " шт");
        System.out.println("Красивых слов с длиной 5: " + counterFiveLength + " шт");
        System.out.println("*******");
        System.out.println("Количество слов из одной буквы (не влияет на итоговую статистику):");
        System.out.println("С длиной 3: " + counterThreeLengthSameCharacter + " шт");
        System.out.println("С длиной 4: " + counterFourLengthSameCharacter + " шт");
        System.out.println("С длиной 5: " + counterFiveLengthSameCharacter + " шт");
    }

    public static Thread getThread(String[] text, AtomicInteger threeLength, AtomicInteger fourLength, AtomicInteger fiveLength, Predicate<String> predicate) {
        return new Thread(() -> {
            for (String word : text) {
                if (predicate.test(word)) {
                    switch (word.length()) {
                        case 3:
                            threeLength.incrementAndGet();
                            break;
                        case 4:
                            fourLength.incrementAndGet();
                            break;
                        case 5:
                            fiveLength.incrementAndGet();
                            break;
                        default:
                            break;
                    }

                }
            }
        });
    }

    public static boolean isSameCharacter(String word) {
        if (word.isEmpty()) return false;
        char firstChar = word.charAt(0);
        for (int i = 1; i < word.length(); i++) {
            if (word.charAt(i) != firstChar) return false;
        }
        return true;
    }

    public static boolean isPalindrome(String word) {
        if (word.isEmpty()) return false;
        return word.contentEquals(new StringBuilder(word).reverse());
    }

    public static boolean isIncreasingSequence(String word) {
        if (word.isEmpty()) return false;
        char currentChar = word.charAt(0);
        for (int i = 1; i < word.length(); i++) {
            if (word.charAt(i) < currentChar) return false;
            currentChar = word.charAt(i);
        }
        return true;
    }

    public static String generateText(String letters, int length) {
        Random random = new Random();
        StringBuilder text = new StringBuilder();
        for (int i = 0; i < length; i++) {
            text.append(letters.charAt(random.nextInt(letters.length())));
        }
        return text.toString();
    }

}