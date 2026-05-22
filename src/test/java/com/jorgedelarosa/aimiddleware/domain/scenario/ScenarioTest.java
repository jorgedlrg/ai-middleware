package com.jorgedelarosa.aimiddleware.domain.scenario;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Test;

class ScenarioTest {

  @Test
  void create_shouldGenerateIdAndSetFields() {
    Scenario scenario = Scenario.create("Adventure", "An exciting journey");

    assertNotNull(scenario.getId());
    assertEquals("Adventure", scenario.getName());
    assertEquals("An exciting journey", scenario.getDescription());
    assertTrue(scenario.getContexts().isEmpty());
    assertTrue(scenario.getRoles().isEmpty());
    assertTrue(scenario.getIntroductions().isEmpty());
  }

  @Test
  void restore_shouldUseProvidedIdAndFields() {
    UUID id = UUID.randomUUID();
    List<Context> contexts = new ArrayList<>();
    contexts.add(Context.create("Room", "Description"));
    List<Role> roles = new ArrayList<>();
    roles.add(Role.create("Hero", "Main character"));
    List<Introduction> intros = new ArrayList<>();

    Scenario scenario = Scenario.restore(id, "Epic Tale", "A story", contexts, roles, intros);

    assertEquals(id, scenario.getId());
    assertEquals("Epic Tale", scenario.getName());
    assertEquals("A story", scenario.getDescription());
    assertEquals(1, scenario.getContexts().size());
    assertEquals(1, scenario.getRoles().size());
  }

  @Test
  void addNewContext_shouldAddContext() {
    Scenario scenario = Scenario.create("Name", "Description");
    assertTrue(scenario.getContexts().isEmpty());

    scenario.addNewContext("Kitchen", "Modern and bright");

    assertEquals(1, scenario.getContexts().size());
    assertEquals("Kitchen", scenario.getContexts().get(0).getName());
  }

  @Test
  void addNewContext_shouldThrowWhenInvalid() {
    Scenario scenario = Scenario.create("Name", "Description");
    assertThrows(RuntimeException.class, () ->
        scenario.addNewContext("", "Description"));
  }

  @Test
  void modifyContext_shouldUpdateContext() {
    Scenario scenario = Scenario.create("Name", "Description");
    scenario.addNewContext("Old Name", "Old Description");
    UUID contextId = scenario.getContexts().get(0).getId();

    scenario.modifyContext(contextId, "New Name", "New Description");

    assertEquals("New Name", scenario.getContexts().get(0).getName());
    assertEquals("New Description", scenario.getContexts().get(0).getPhysicalDescription());
  }

  @Test
  void modifyContext_shouldThrowWhenNotFound() {
    Scenario scenario = Scenario.create("Name", "Description");
    assertThrows(RuntimeException.class, () ->
        scenario.modifyContext(UUID.randomUUID(), "Name", "Description"));
  }

  @Test
  void deleteContext_shouldRemoveContext() {
    Scenario scenario = Scenario.create("Name", "Description");
    scenario.addNewContext("To Delete", "Description");
    UUID contextId = scenario.getContexts().get(0).getId();

    scenario.deleteContext(contextId);

    assertTrue(scenario.getContexts().isEmpty());
  }

  @Test
  void addNewRole_shouldAddRole() {
    Scenario scenario = Scenario.create("Name", "Description");
    assertTrue(scenario.getRoles().isEmpty());

    scenario.addNewRole("Hero", "Main character");

    assertEquals(1, scenario.getRoles().size());
    assertEquals("Hero", scenario.getRoles().get(0).getName());
  }

  @Test
  void addNewRole_shouldThrowWhenInvalid() {
    Scenario scenario = Scenario.create("Name", "Description");
    assertThrows(RuntimeException.class, () ->
        scenario.addNewRole("", "Details"));
  }

  @Test
  void modifyRole_shouldUpdateRole() {
    Scenario scenario = Scenario.create("Name", "Description");
    scenario.addNewRole("Old Name", "Old Details");
    UUID roleId = scenario.getRoles().get(0).getId();

    scenario.modifyRole(roleId, "New Name", "New Details");

    assertEquals("New Name", scenario.getRoles().get(0).getName());
    assertEquals("New Details", scenario.getRoles().get(0).getDetails());
  }

  @Test
  void modifyRole_shouldThrowWhenNotFound() {
    Scenario scenario = Scenario.create("Name", "Description");
    assertThrows(RuntimeException.class, () ->
        scenario.modifyRole(UUID.randomUUID(), "Name", "Details"));
  }

  @Test
  void deleteRole_shouldRemoveRole() {
    Scenario scenario = Scenario.create("Name", "Description");
    scenario.addNewRole("To Delete", "Details");
    UUID roleId = scenario.getRoles().get(0).getId();

    scenario.deleteRole(roleId);

    assertTrue(scenario.getRoles().isEmpty());
  }

  @Test
  void addNewIntroduction_shouldAddIntroduction() {
    Scenario scenario = Scenario.create("Name", "Description");
    Context context = Context.create("Room", "Description");
    Role role = Role.create("Role", "Details");
    scenario.addNewContext("Room", "Description");
    scenario.addNewRole("Role", "Details");

    scenario.addNewIntroduction(
        "Hello!",
        Optional.of("Thinking..."),
        Optional.empty(),
        scenario.getRoles().get(0),
        scenario.getContexts().get(0));

    assertEquals(1, scenario.getIntroductions().size());
    assertEquals("Hello!", scenario.getIntroductions().get(0).getSpokenText());
  }

  @Test
  void addNewIntroduction_shouldThrowWhenInvalid() {
    Scenario scenario = Scenario.create("Name", "Description");
    Context context = Context.create("Room", "Description");
    Role role = Role.create("Role", "Details");

    assertThrows(RuntimeException.class, () ->
        scenario.addNewIntroduction("", Optional.empty(), Optional.empty(), role, context));
  }

  @Test
  void modifyIntroduction_shouldUpdateIntroduction() {
    Scenario scenario = Scenario.create("Name", "Description");
    Context context = Context.create("Room", "Description");
    Role role = Role.create("Role", "Details");
    scenario.addNewContext("Room", "Description");
    scenario.addNewRole("Role", "Details");
    scenario.addNewIntroduction("Original", Optional.empty(), Optional.empty(), role, context);
    UUID introId = scenario.getIntroductions().get(0).getId();

    scenario.modifyIntroduction(introId, "Updated", Optional.of("New thought"), Optional.empty());

    assertEquals("Updated", scenario.getIntroductions().get(0).getSpokenText());
    assertTrue(scenario.getIntroductions().get(0).getThoughtText().isPresent());
    assertEquals("New thought", scenario.getIntroductions().get(0).getThoughtText().get());
  }

  @Test
  void modifyIntroduction_shouldThrowWhenNotFound() {
    Scenario scenario = Scenario.create("Name", "Description");
    assertThrows(RuntimeException.class, () ->
        scenario.modifyIntroduction(UUID.randomUUID(), "Text", Optional.empty(), Optional.empty()));
  }

  @Test
  void deleteIntroduction_shouldRemoveIntroduction() {
    Scenario scenario = Scenario.create("Name", "Description");
    Context context = Context.create("Room", "Description");
    Role role = Role.create("Role", "Details");
    scenario.addNewContext("Room", "Description");
    scenario.addNewRole("Role", "Details");
    scenario.addNewIntroduction("To Delete", Optional.empty(), Optional.empty(), role, context);
    UUID introId = scenario.getIntroductions().get(0).getId();

    scenario.deleteIntroduction(introId);

    assertTrue(scenario.getIntroductions().isEmpty());
  }

  @Test
  void setName_shouldUpdateName() {
    Scenario scenario = Scenario.create("Old Name", "Description");
    scenario.setName("New Name");

    assertEquals("New Name", scenario.getName());
  }

  @Test
  void setName_shouldThrowWhenBlank() {
    Scenario scenario = Scenario.create("Name", "Description");
    assertThrows(RuntimeException.class, () -> scenario.setName(""));
  }

  @Test
  void setName_shouldThrowWhenNull() {
    Scenario scenario = Scenario.create("Name", "Description");
    assertThrows(RuntimeException.class, () -> scenario.setName(null));
  }

  @Test
  void setDescription_shouldUpdateDescription() {
    Scenario scenario = Scenario.create("Name", "Old Description");
    scenario.setDescription("New Description");

    assertEquals("New Description", scenario.getDescription());
  }

  @Test
  void getContexts_shouldReturnUnmodifiableList() {
    Scenario scenario = Scenario.create("Name", "Description");
    scenario.addNewContext("Room", "Description");

    assertThrows(UnsupportedOperationException.class, () ->
        scenario.getContexts().add(Context.create("Another", "Description")));
  }

  @Test
  void getRoles_shouldReturnUnmodifiableList() {
    Scenario scenario = Scenario.create("Name", "Description");
    scenario.addNewRole("Role", "Details");

    assertThrows(UnsupportedOperationException.class, () ->
        scenario.getRoles().add(Role.create("Another", "Details")));
  }

  @Test
  void getIntroductions_shouldReturnUnmodifiableList() {
    Scenario scenario = Scenario.create("Name", "Description");
    Context context = Context.create("Room", "Description");
    Role role = Role.create("Role", "Details");
    scenario.addNewContext("Room", "Description");
    scenario.addNewRole("Role", "Details");
    scenario.addNewIntroduction("Speech", Optional.empty(), Optional.empty(), role, context);

    assertThrows(UnsupportedOperationException.class, () ->
        scenario.getIntroductions().add(null));
  }

  @Test
  void isValid_shouldReturnTrueForValidScenario() {
    Scenario scenario = Scenario.create("Valid Name", "Valid Description");
    assertTrue(scenario.isValid());
  }
}