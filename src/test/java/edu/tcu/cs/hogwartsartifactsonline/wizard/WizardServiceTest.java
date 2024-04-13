package edu.tcu.cs.hogwartsartifactsonline.wizard;
import edu.tcu.cs.hogwartsartifactsonline.HogwartsArtifactsOnlineApplication;
import edu.tcu.cs.hogwartsartifactsonline.artifact.utils.IdWorker;
import edu.tcu.cs.hogwartsartifactsonline.wizard.Wizard;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class WizardServiceTest {
    @Mock
    WizardRepository wizardRepository;
    @InjectMocks
    WizardService wizardService;


    List<Wizard> wizards;


    @BeforeEach
    void setUp() {
        Wizard w1 = new Wizard();
        w1.setId(1);
        w1.setName("Alubs Dumbledore");

        Wizard w2 = new Wizard();
        w2.setId(2);
        w2.setName("Harry Potter");

        Wizard w3 = new Wizard();
        w3.setId(3);
        w3.setName("Neville Longbottom");

        this.wizards = new ArrayList<>();
        this.wizards.add(w1);
        this.wizards.add(w2);
        this.wizards.add(w3);

    }

    @AfterEach
    void tearDown() {

    }

    @Test
    void testFindAllSuccess() {
        given(wizardRepository.findAll()).willReturn(wizards);

        List<Wizard> actualWizards = wizardService.findAll();

        assertThat(actualWizards).hasSameSizeAs(wizards);
        verify(wizardRepository, times(1)).findAll();
    }

    @Test
    void testFindByIdSuccess() {
        Wizard expectedWizard = new Wizard();
        expectedWizard.setId(1);
        expectedWizard.setName("Albus Dumbledore");

        given(wizardRepository.findById(1)).willReturn(Optional.of(expectedWizard));

        Wizard actualWizard = wizardService.findById(1);

        assertThat(actualWizard.getId()).isEqualTo(expectedWizard.getId());
        assertThat(actualWizard.getName()).isEqualTo(expectedWizard.getName());
        verify(wizardRepository, times(1)).findById(1);
    }

    @Test
    void testFindByIdNotFound() {
        given(wizardRepository.findById(anyInt())).willReturn(Optional.empty());

        assertThrows(WizardNotFoundException.class, () -> wizardService.findById(1));
        verify(wizardRepository, times(1)).findById(1);
    }

    @Test
    void testSaveSuccess() {
        Wizard newWizard = new Wizard();
        newWizard.setName("Hermione Granger");

        given(wizardRepository.save(newWizard)).willReturn(newWizard);

        Wizard savedWizard = wizardService.save(newWizard);

        assertThat(savedWizard.getName()).isEqualTo(newWizard.getName());
        verify(wizardRepository, times(1)).save(newWizard);
    }

    @Test
    void testUpdateSuccess() {
        Wizard existingWizard = new Wizard();
        existingWizard.setId(1);
        existingWizard.setName("Albus Dumbledore");

        Wizard updateInfo = new Wizard();
        updateInfo.setName("Albus Dumbledore - update");

        given(wizardRepository.findById(1)).willReturn(Optional.of(existingWizard));
        given(wizardRepository.save(existingWizard)).willReturn(existingWizard);

        Wizard updatedWizard = wizardService.update(1, updateInfo);

        assertThat(updatedWizard.getId()).isEqualTo(1);
        assertThat(updatedWizard.getName()).isEqualTo("Albus Dumbledore - update");
        verify(wizardRepository, times(1)).findById(1);
        verify(wizardRepository, times(1)).save(existingWizard);
    }

    @Test
    void testUpdateNotFound() {
        Wizard update = new Wizard();
        update.setName("Albus Dumbledore - update");

        given(wizardRepository.findById(1)).willReturn(Optional.empty());

        assertThrows(WizardNotFoundException.class, () -> wizardService.update(1, update));

        verify(wizardRepository, times(1)).findById(1);
    }

    @Test
    void testDeleteSuccess() {
        Wizard wizard = new Wizard();
        wizard.setId(1);
        wizard.setName("Albus Dumbledore");

        given(wizardRepository.findById(1)).willReturn(Optional.of(wizard));
        doNothing().when(wizardRepository).deleteById(1);

        wizardService.delete(1);

        verify(wizardRepository, times(1)).deleteById(1);
    }

    @Test
    void testDeleteNotFound() {
        given(wizardRepository.findById(1)).willReturn(Optional.empty());

        assertThrows(WizardNotFoundException.class, () -> wizardService.delete(1));

        verify(wizardRepository, times(1)).findById(1);
    }
}
