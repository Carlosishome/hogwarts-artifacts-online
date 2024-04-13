package edu.tcu.cs.hogwartsartifactsonline.wizard;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.tcu.cs.hogwartsartifactsonline.artifact.Artifact;
import edu.tcu.cs.hogwartsartifactsonline.system.StatusCode;
import edu.tcu.cs.hogwartsartifactsonline.wizard.dto.WizardDto;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import java.util.ArrayList;
import java.util.List;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
class WizardControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    WizardService wizardService;

    List<Wizard> wizards;


    @BeforeEach
    void setUp() throws Exception {

        Artifact a1 = new Artifact();
        a1.setId("1250808601744904191");
        a1.setName("Deluminator");
        a1.setDescription("Deluminator is a device invented by Albus Dumbledor");
        a1.setImageUrl("ImageUrl");


        Artifact a2 = new Artifact();
        a2.setId("1250808601744904192");
        a2.setName("Invisibility Cloak");
        a2.setDescription("An invisibility cloak is used to make the wearer");
        a2.setImageUrl("ImageUrl");

        Artifact a3 = new Artifact();
        a3.setId("1250808601744904193");
        a3.setName("Elder Wand");
        a3.setDescription("The Elder Wand, known throughout history as the death wand");
        a3.setImageUrl("ImageUrl");

        Artifact a4 = new Artifact();
        a4.setId("1250808601744904194");
        a4.setName("The Marauder's Map");
        a4.setDescription("A magical map of Hogwarts created by Remus lupin");
        a4.setImageUrl("ImageUrl");

        Artifact a5 = new Artifact();
        a5.setId("1250808601744904195");
        a5.setName("The Sword Of Gryffindor");
        a5.setDescription("A goblin-made sword adorned with large rubies on the");
        a5.setImageUrl("ImageUrl");

        Artifact a6 = new Artifact();
        a6.setId("1250808601744904196");
        a6.setName("Resurrection Stone");
        a6.setDescription("The Resurrection Stone allows the holder to bring back");
        a6.setImageUrl("ImageUrl");

        this.wizards = new ArrayList<>();

        Wizard w1 = new Wizard();
        w1.setId(1);
        w1.setName("Albus Dumbledore");
        w1.addArtifact(a1);
        w1.addArtifact(a3);
        this.wizards.add(w1);

        Wizard w2 = new Wizard();
        w2.setId(2);
        w2.setName("Harry Potter");
        w2.addArtifact(a2);
        w2.addArtifact(a4);
        this.wizards.add(w2);


        Wizard w3 = new Wizard();
        w3.setId(3);
        w3.setName("Neville Longbottom");
        w3.addArtifact(a5);
        this.wizards.add(w3);
    }

    @Test
    void testFindAllWizardSuccess() throws Exception {
        //given
        given(wizardService.findAll()).willReturn(wizards);

        //when and then
        mockMvc.perform(get("/api/v1/wizards").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").value("Find all Success"))
                .andExpect(jsonPath("$.data", Matchers.hasSize(wizards.size())))
                .andExpect(jsonPath("$.data[0].id").value(wizards.get(0).getId()))
                .andExpect(jsonPath("$.data[0].name").value(wizards.get(0).getName()))
                .andExpect(jsonPath("$.data[1].id").value(wizards.get(1).getId()))
                .andExpect(jsonPath("$.data[1].name").value(wizards.get(1).getName()));
    }


    @Test
    void testFindWizardByIdSuccess() throws Exception {
        // Given
        given(wizardService.findById(1)).willReturn(wizards.get(0));

        // When & Then
        mockMvc.perform(get("/api/v1/wizards/1").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").value("Find One Success"))
                .andExpect(jsonPath("$.data.id").value(wizards.get(0).getId()))
                .andExpect(jsonPath("$.data.name").value(wizards.get(0).getName()));
    }

    @Test
    void testFindWizardByIdNotFound() throws Exception {
        // Given
        given(wizardService.findById(5)).willThrow(new WizardNotFoundException(5));

        // When & Then
        mockMvc.perform(get("/api/v1/wizards/5").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.flag").value(false))
                .andExpect(jsonPath("$.code").value(StatusCode.NOT_FOUND))
                .andExpect(jsonPath("$.message").value("Could not find wizard with Id 5:("))
                .andExpect(jsonPath("$.data").isEmpty());
    }

    @Test
    void testAddWizardSuccess() throws Exception {
        // Given
        WizardDto wizardDto = new WizardDto(null, "Hermione Granger", 0);
        Wizard savedWizard = new Wizard();
        savedWizard.setId(4);
        savedWizard.setName("Hermione Granger");

        given(wizardService.save(any(Wizard.class))).willReturn(savedWizard);

        // When & Then
        mockMvc.perform(post("/api/v1/wizards")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(wizardDto))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").value("Add Success"))
                .andExpect(jsonPath("$.data.id").value(savedWizard.getId()))
                .andExpect(jsonPath("$.data.name").value(savedWizard.getName()));
    }

    @Test
    void testUpdatedWizardSuccess() throws Exception {
        // Simplified WizardDto to match assumed fields
        WizardDto wizardDto = new WizardDto(1, "Updated Wizard name", 0); // Assuming 0 artifacts for simplicity

        Wizard updatedWizard = new Wizard();
        updatedWizard.setId(1);
        updatedWizard.setName("Updated Wizard name");

        given(wizardService.update(eq(1), any(Wizard.class))).willReturn(updatedWizard);

        // When & Then
        mockMvc.perform(put("/api/v1/wizards/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(wizardDto))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").value("Update Success"))
                .andExpect(jsonPath("$.data.id").value(updatedWizard.getId()))
                .andExpect(jsonPath("$.data.name").value(updatedWizard.getName()));
    }

    @Test
    void testUpdateWizardErrorWithNonExistentId() throws Exception {
        // Mocking the service layer to throw WizardNotFoundException
        given(wizardService.update(eq(5), any(Wizard.class))).willThrow(new WizardNotFoundException(5));

        // Simplified WizardDto to match assumed fields
        WizardDto wizardDto = new WizardDto(5, "Update Wizard name", 0); // Assuming 0 artifacts for simplicity

        // When & Then
        mockMvc.perform(put("/api/v1/wizards/5")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(wizardDto))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.flag").value(false))
                .andExpect(jsonPath("$.code").value(StatusCode.NOT_FOUND))
                .andExpect(jsonPath("$.message").value("Could not find wizard with Id 5:("))
                .andExpect(jsonPath("$.data").isEmpty());
    }


    @Test
    void testDeleteWizardSuccess() throws Exception {
        // Assuming the delete operation is expected to succeed.
        doNothing().when(wizardService).delete(3);

        // Perform the delete operation and expect successful deletion
        mockMvc.perform(delete("/api/v1/wizards/3").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").value("Delete Success"))
                .andExpect(jsonPath("$.data").isEmpty());
    }

    @Test
    void testDeleteWizardErrorWithNonExistentId() throws Exception {
        // Mock the wizardService.delete method to throw a WizardNotFoundException
        doThrow(new WizardNotFoundException(5)).when(wizardService).delete(5);

        // Perform the delete operation and verify the response
        mockMvc.perform(delete("/api/v1/wizards/5").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.flag").value(false))
                .andExpect(jsonPath("$.code").value(StatusCode.NOT_FOUND))
                .andExpect(jsonPath("$.message").value("Could not find wizard with Id 5:("))
                .andExpect(jsonPath("$.data").isEmpty());
    }


}

