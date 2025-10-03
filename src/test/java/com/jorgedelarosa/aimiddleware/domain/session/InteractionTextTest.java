package com.jorgedelarosa.aimiddleware.domain.session;

import java.util.Optional;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * @author jorge
 */
public class InteractionTextTest {

  public InteractionTextTest() {}

  @BeforeAll
  public static void setUpClass() {}

  @AfterAll
  public static void tearDownClass() {}

  @BeforeEach
  public void setUp() {}

  @AfterEach
  public void tearDown() {}

  @Test
  void constructor_trimsTextAndReasoning() {
    InteractionText it = new InteractionText("  a b  ", Optional.of("  r  "));
    assertEquals("a b", it.getText());
    assertEquals(Optional.of("r"), it.getReasoning());
  }

  @Test
  void constructor_allowsEmptyOptionalReasoning() {
    InteractionText it = new InteractionText("x", Optional.empty());
    assertEquals("x", it.getText());
    assertTrue(it.getReasoning().isEmpty());
  }

  @Test
  void constructor_trimsTextToEmpty_whenOnlyWhitespaceProvided() {
    assertThrows(RuntimeException.class, () -> new InteractionText("   ", Optional.empty()));
  }

  @Test
  void constructor_throwsNPE_whenReasoningOptionalIsNull() {
    assertThrows(NullPointerException.class, () -> new InteractionText("x", null));
  }

  @Test
  void optionalFromNullable_returnsEmpty_whenTextIsNull() {
    Optional<InteractionText> result = InteractionText.optionalFromNullable(null, "reason");
    assertTrue(result.isEmpty());
  }
  
  @Test
  void optionalFromNullable_returnsEmpty_whenTextIsBlank() {
    Optional<InteractionText> result = InteractionText.optionalFromNullable("    ", "reason");
    assertTrue(result.isEmpty());
  }

  @Test
  void optionalFromNullable_returnsPresent_whenTextIsNotNull_andReasoningIsNull() {
    Optional<InteractionText> result = InteractionText.optionalFromNullable("hello", null);
    assertTrue(result.isPresent());
    InteractionText it = result.orElseThrow();
    assertEquals("hello", it.getText());
    assertTrue(it.getReasoning().isEmpty());
  }

  @Test
  void optionalFromNullable_trimsTextAndReasoning() {
    Optional<InteractionText> result =
        InteractionText.optionalFromNullable("  hello  ", "  because it should  ");
    assertTrue(result.isPresent());
    InteractionText it = result.orElseThrow();

    assertEquals("hello", it.getText());
    assertTrue(it.getReasoning().isPresent());
    assertEquals("because it should", it.getReasoning().orElseThrow());
  }
}
