package com.example.goodreads_finalproject.controller.admin;

import com.example.goodreads_finalproject.entity.Province;
import com.example.goodreads_finalproject.entity.Role;
import com.example.goodreads_finalproject.exception.BadRequestException;
import com.example.goodreads_finalproject.model.request.RoleRequest;
import com.example.goodreads_finalproject.model.request.UserRequest;
import com.example.goodreads_finalproject.model.request.UserSearchRequest;
import com.example.goodreads_finalproject.model.response.CommonResponse;
import com.example.goodreads_finalproject.model.response.LocationResponse;
import com.example.goodreads_finalproject.model.response.UserResponse;
import com.example.goodreads_finalproject.security.SecurityUtils;
import com.example.goodreads_finalproject.service.RoleService;
import com.example.goodreads_finalproject.service.UserService;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping()
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ManageUserController {
    UserService userService;
    RoleService roleService;

    @GetMapping("/admin/users")
    public String searchUsers(Model model, UserSearchRequest request) {
        CommonResponse<?> commonResponse = userService.searchUser(request);

        model.addAttribute("pageUserInfo", commonResponse);
        model.addAttribute("currentPage", request.getPageIndex());
        model.addAttribute("pageSize", request.getPageSize());

        return "admin/user/user-list";
    }

    @GetMapping("/admin/add-user")
    public String createUserPage(Model model, UserRequest request) {


        return "admin/user/user-create";
    }

    @GetMapping("/admin/roles")
    public String searchRoles(Model model) {
        List<Role> allRole = roleService.findAllRole();
        model.addAttribute("roleList", allRole);
//        model.addAttribute("pageUserInfo", commonResponse);
//        model.addAttribute("currentPage", request.getPageIndex());
//        model.addAttribute("pageSize", request.getPageSize());

        return "admin/user/role-list";
    }

    @GetMapping("/admin/profile")
    public String editProfile(Model model) {
        Long userLoginId = SecurityUtils.getCurrentUserLoginId().get();
        UserResponse userResponse = userService.findUserById(userLoginId);
        List<LocationResponse> allProvince = userService.getAllProvince();

        model.addAttribute("userResponse", userResponse);
        model.addAttribute("provinceList", allProvince);

        return "admin/user/profile";
    }

    // API
//    @PostMapping("api/v1/admin/users")
//    public ResponseEntity<?> createUser(@RequestBody @Valid CreateUserRequest request) {
//        try {
//            userService.createUser(request);
//            return new ResponseEntity<>(null, HttpStatus.CREATED);
//        } catch (ExistedUserException ex) {
//            return new ResponseEntity<>("Email existed", HttpStatus.BAD_REQUEST);
//        }
//    }

    @PutMapping("api/v1/admin/users/{userId}")
    public ResponseEntity<?> lockedUser(@PathVariable Long userId) {
        userService.lockedOrUnlockedUser(userId);
        return new ResponseEntity<>(null, HttpStatus.OK);
    }

    @PostMapping("api/v1/admin/roles")
    public ResponseEntity<?> createRole(@RequestBody @Valid RoleRequest request) {
        try {
            roleService.createRole(request);
            return new ResponseEntity<>(null, HttpStatus.CREATED);
        } catch (BadRequestException e) {
            return new ResponseEntity<>("Role existed", HttpStatus.BAD_REQUEST);
        }

    }

    @PutMapping("api/v1/admin/profile")
    public ResponseEntity<?> updateProfile(@RequestBody @Valid UserRequest request) {
        userService.updateUser(request);
        return new ResponseEntity<>(null, HttpStatus.OK);
    }

}

