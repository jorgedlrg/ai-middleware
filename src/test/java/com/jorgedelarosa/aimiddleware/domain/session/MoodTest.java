package com.jorgedelarosa.aimiddleware.domain.session;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Optional;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;

class MoodTest {

    static Stream<Arguments> emojiProvider() {
        return Stream.of(
                Arguments.of(Mood.HAPPY, "😊"),
                Arguments.of(Mood.SAD, "😢"),
                Arguments.of(Mood.ANGRY, "😠"),
                Arguments.of(Mood.EXCITED, "🤩"),
                Arguments.of(Mood.NERVOUS, "😰"),
                Arguments.of(Mood.CALM, "😌"),
                Arguments.of(Mood.FLIRTY, "😏"),
                Arguments.of(Mood.PASSIONATE, "🔥"),
                Arguments.of(Mood.TENDER, "🥰"),
                Arguments.of(Mood.SEDUCTIVE, "😈"),
                Arguments.of(Mood.INTIMATE, "💕"),
                Arguments.of(Mood.AROUSED, "💦"),
                Arguments.of(Mood.DOMINANT, "👑"),
                Arguments.of(Mood.SUBMISSIVE, "🙇"),
                Arguments.of(Mood.CONFIDENT, "💪"),
                Arguments.of(Mood.SHY, "🙈"),
                Arguments.of(Mood.PLAYFUL, "😜"),
                Arguments.of(Mood.SERIOUS, "🧐"),
                Arguments.of(Mood.SURPRISED, "😲"),
                Arguments.of(Mood.CONFUSED, "😵‍💫"),
                Arguments.of(Mood.THOUGHTFUL, "🤔"),
                Arguments.of(Mood.FRUSTRATED, "😤"),
                Arguments.of(Mood.RELIEVED, "😅"),
                Arguments.of(Mood.CURIOUS, "🤨"),
                Arguments.of(Mood.TEASING, "😋"),
                Arguments.of(Mood.CARING, "🤗"),
                Arguments.of(Mood.MISCHIEVOUS, "😼"),
                Arguments.of(Mood.VULNERABLE, "🥺"),
                Arguments.of(Mood.EUPHORIC, "🎉")
        );
    }

    @ParameterizedTest(name = "{index} => {0} has emoji {1}")
    @MethodSource("emojiProvider")
    void getEmoji_returnsExpectedEmoji(Mood mood, String expectedEmoji) {
        assertEquals(expectedEmoji, mood.getEmoji());
    }

    @Test
    void optionalValueOf_returnsPresentForAllEnumNames() {
        for (Mood mood : Mood.values()) {
            Optional<Mood> result = Mood.optionalValueOf(mood.name());
            assertTrue(result.isPresent(), "Expected present for: " + mood.name());
            assertEquals(mood, result.orElseThrow());
        }
    }

    @ParameterizedTest
    @CsvSource({
        "happy, HAPPY",
        "sAd, SAD",
        "angry, ANGRY",
        "eXcItEd, EXCITED",
        "nErVoUs, NERVOUS",
        "cAlM, CALM",
        "fLiRtY, FLIRTY",
        "pAsSiOnAtE, PASSIONATE",
        "tEnDeR, TENDER",
        "sEdUcTiVe, SEDUCTIVE",
        "iNtImAtE, INTIMATE",
        "aRoUsEd, AROUSED"
    })
    void optionalValueOf_isCaseInsensitive(String input, String expectedEnumName) {
        Optional<Mood> result = Mood.optionalValueOf(input);
        assertTrue(result.isPresent());
        assertEquals(Mood.valueOf(expectedEnumName), result.orElseThrow());
    }

    @Test
    void optionalValueOf_returnsEmptyForInvalid() {
        Optional<Mood> result = Mood.optionalValueOf("not-a-mood");
        assertTrue(result.isEmpty());
    }

    @Test
    void optionalValueOf_returnsEmptyForNull() {
        Optional<Mood> result = Mood.optionalValueOf(null);
        assertTrue(result.isEmpty());
    }
}
