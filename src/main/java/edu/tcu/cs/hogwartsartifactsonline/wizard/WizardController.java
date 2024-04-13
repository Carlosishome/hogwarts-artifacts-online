package edu.tcu.cs.hogwartsartifactsonline.wizard;

import edu.tcu.cs.hogwartsartifactsonline.system.Result;
import edu.tcu.cs.hogwartsartifactsonline.system.StatusCode;
import edu.tcu.cs.hogwartsartifactsonline.wizard.converter.WizardDtoToWizardConverter;
import edu.tcu.cs.hogwartsartifactsonline.wizard.converter.WizardToWizardDtoConverter;
import edu.tcu.cs.hogwartsartifactsonline.wizard.dto.WizardDto;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/wizards")
public class WizardController {
    private final WizardService wizardService;
    private final WizardDtoToWizardConverter wizardDtoToWizardConverter; // Corrected the field name
    private final WizardToWizardDtoConverter wizardToWizardDtoConverter;

    // Fixed constructor syntax and initialization
    public WizardController(WizardService wizardService,
                            WizardDtoToWizardConverter wizardDtoToWizardConverter,
                            WizardToWizardDtoConverter wizardToWizardDtoConverter) {
        this.wizardService = wizardService;
        this.wizardDtoToWizardConverter = wizardDtoToWizardConverter; // Corrected the field name
        this.wizardToWizardDtoConverter = wizardToWizardDtoConverter;
    }

    @GetMapping
    public Result findAllWizards() {
        List<Wizard> foundWizards = this.wizardService.findAll(); // Variable name corrected
        List<WizardDto> wizardDtos = foundWizards.stream()
                .map(wizardToWizardDtoConverter::convert)
                .collect(Collectors.toList());
        return new Result(true, StatusCode.SUCCESS, "Find all successful", wizardDtos); // Added wizardDtos to the response
    }

    @GetMapping("/{wizardId}")
    public Result findWizardById(@PathVariable Integer wizardId) {
        Wizard foundWizard = this.wizardService.findById(wizardId); // Corrected method call
        WizardDto wizardDto = this.wizardToWizardDtoConverter.convert(foundWizard);
        return new Result(true, StatusCode.SUCCESS, "Find one successful", wizardDto); // Added wizardDto to the response
    }

    @PostMapping
    public Result addWizard(@Valid @RequestBody WizardDto wizardDto) {
        Wizard newWizard = this.wizardDtoToWizardConverter.convert(wizardDto);
        Wizard savedWizard = this.wizardService.save(newWizard);
        WizardDto savedWizardDto = this.wizardToWizardDtoConverter.convert(savedWizard);
        return new Result(true, StatusCode.SUCCESS, "Add successful", savedWizardDto); // Added savedWizardDto to the response
    }

    @PutMapping("/{wizardId}")
    public Result updateWizard(@PathVariable Integer wizardId, @Valid @RequestBody WizardDto wizardDto) { // Corrected variable name and type
        Wizard update = this.wizardDtoToWizardConverter.convert(wizardDto); // Corrected method name
        Wizard updatedWizard = this.wizardService.update(wizardId, update);
        WizardDto updatedWizardDto = this.wizardToWizardDtoConverter.convert(updatedWizard);
        return new Result(true, StatusCode.SUCCESS, "Update successful", updatedWizardDto); // Added updatedWizardDto to the response
    }

    @DeleteMapping("/{wizardId}") // Corrected path variable annotation
    public Result deleteWizard(@PathVariable Integer wizardId) {
        this.wizardService.delete(wizardId);
        return new Result(true, StatusCode.SUCCESS, "Delete successful");
    }
}
