package edu.tcu.cs.hogwartsartifactsonline.wizard.converter;
import edu.tcu.cs.hogwartsartifactsonline.wizard.Wizard;
import edu.tcu.cs.hogwartsartifactsonline.wizard.dto.WizardDto;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class WizardDtoToWizardConverter implements Converter<WizardDto, Wizard> {

    @Override
    public Wizard convert(WizardDto source) {
        Wizard wizard = new Wizard();
        wizard.setId(source.id()); // Accessing record component
        wizard.setName(source.name()); // Accessing record component
        // Since Wizard class likely doesn't have a field for numberOfArtifacts directly,
        // we're not using source.numberOfArtifacts() here. Adjust if needed.
        return wizard;
    }
}
