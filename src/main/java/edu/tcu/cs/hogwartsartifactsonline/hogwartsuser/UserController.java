package edu.tcu.cs.hogwartsartifactsonline.user;

import edu.tcu.cs.hogwartsartifactsonline.hogwartsuser.HogwartsUser;
import edu.tcu.cs.hogwartsartifactsonline.system.exception.ObjectNotFoundException;
import edu.tcu.cs.hogwartsartifactsonline.user.dto.UserDto;
import edu.tcu.cs.hogwartsartifactsonline.user.converter.UserDtoToUserConverter;
import edu.tcu.cs.hogwartsartifactsonline.user.converter.UserToUserDtoConverter;
import edu.tcu.cs.hogwartsartifactsonline.system.Result;
import edu.tcu.cs.hogwartsartifactsonline.system.StatusCode;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {
    private final UserService userService;
    private final UserDtoToUserConverter userDtoToUserConverter;
    private final UserToUserDtoConverter userToUserDtoConverter;

    public UserController(UserService userService, UserDtoToUserConverter userDtoToUserConverter,
                          UserToUserDtoConverter userToUserDtoConverter) {
        this.userService = userService;
        this.userDtoToUserConverter = userDtoToUserConverter;
        this.userToUserDtoConverter = userToUserDtoConverter;
    }

    @GetMapping
    public Result findAllUsers() {
        List<HogwartsUser> foundHogwartsUsers = this.userService.findAll();
        List<UserDto> userDtos = foundHogwartsUsers.stream()
                .map(userToUserDtoConverter::convert)
                .collect(Collectors.toList());
        return new Result(true, StatusCode.SUCCESS, "Find all users successful", userDtos);
    }

    @GetMapping("/{userId}")
    public Result findUserById(@PathVariable Integer userId) {
        try {
            HogwartsUser foundHogwartsUser = this.userService.findById(userId);
            UserDto userDto = userToUserDtoConverter.convert(foundHogwartsUser);
            return new Result(true, StatusCode.SUCCESS, "Find One Success", userDto);
        } catch (ObjectNotFoundException ex) {
            return new Result(false, StatusCode.NOT_FOUND, "Could not find user with Id " + userId + ":(");
        }
    }

    @PostMapping
    public Result addUser(@Valid @RequestBody UserDto newUserDto) {
        HogwartsUser newUser = userDtoToUserConverter.convert(newUserDto);
        HogwartsUser savedUser = this.userService.save(newUser);
        UserDto savedUserDto = userToUserDtoConverter.convert(savedUser);
        return new Result(true, StatusCode.SUCCESS, "Add Success", savedUserDto);
    }

    @PutMapping("/{userId}")
    public Result updateUser(@PathVariable Integer userId, @Valid @RequestBody UserDto updatedUserDto) {
        HogwartsUser updatedUser = userDtoToUserConverter.convert(updatedUserDto);
        try {
            HogwartsUser updatedHogwartsUser = this.userService.update(userId, updatedUser);
            UserDto updatedUserDto = userToUserDtoConverter.convert(updatedHogwartsUser);
            return new Result(true, StatusCode.SUCCESS, "Update Success", updatedUserDto);
        } catch (ObjectNotFoundException ex) {
            return new Result(false, StatusCode.NOT_FOUND, "Could not find user with Id " + userId + ":(");
        }
    }

    @DeleteMapping("/{userId}")
    public Result deleteUser(@PathVariable Integer userId) {
        try {
            this.userService.delete(userId);
            return new Result(true, StatusCode.SUCCESS, "Delete Success");
        } catch (ObjectNotFoundException ex) {
            return new Result(false, StatusCode.NOT_FOUND, "Could not find user with Id " + userId + ":(");
        }
    }
}
