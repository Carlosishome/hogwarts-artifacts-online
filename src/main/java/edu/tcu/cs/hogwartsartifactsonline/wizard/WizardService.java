package edu.tcu.cs.hogwartsartifactsonline.wizard;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@Transactional
public class WizardService {
    private final WizardRepository wizardRepository;

    public WizardService(WizardRepository wizardRepository){
        this.wizardRepository = wizardRepository; // Fixed missing assignment
    }

    public List<Wizard> findAll() {
        return wizardRepository.findAll();
    }

    public Wizard findById(Integer wizardId) {
        return wizardRepository.findById(wizardId) // Fixed method name and parenthesis
                .orElseThrow(() -> new WizardNotFoundException(wizardId)); // Fixed method name
    }

    public Wizard save(Wizard newWizard) {
        return wizardRepository.save(newWizard); // Fixed to save the passed object instead of a new instance
    }

    public Wizard update(Integer wizardId, Wizard update) {
        return wizardRepository.findById(wizardId)
                .map(oldWizard -> { // Fixed typo in variable name
                    oldWizard.setName(update.getName());
                    // Add more updates as needed
                    return wizardRepository.save(oldWizard); // Fixed to save the updated object
                })
                .orElseThrow(() -> new WizardNotFoundException(wizardId));
    }

    public void delete(Integer wizardId) {
        Wizard wizardToBeDeleted = wizardRepository.findById(wizardId)
                .orElseThrow(() -> new WizardNotFoundException(wizardId));

        wizardToBeDeleted.removeAllArtifacts();
        wizardRepository.deleteById(wizardId);
    }
}
