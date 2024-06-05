package com.example.goodreads_finalproject.service;

import com.example.goodreads_finalproject.entity.*;
import com.example.goodreads_finalproject.exception.*;
import com.example.goodreads_finalproject.model.request.*;
import com.example.goodreads_finalproject.model.response.CommonResponse;
import com.example.goodreads_finalproject.model.response.JwtResponse;
import com.example.goodreads_finalproject.model.response.LocationResponse;
import com.example.goodreads_finalproject.model.response.UserResponse;
import com.example.goodreads_finalproject.repository.*;
import com.example.goodreads_finalproject.repository.custom.UserCustomRepository;
import com.example.goodreads_finalproject.security.CustomUserDetails;
import com.example.goodreads_finalproject.security.JwtUtils;
import com.example.goodreads_finalproject.security.SecurityUtils;
import com.example.goodreads_finalproject.statics.Gender;
import com.example.goodreads_finalproject.statics.Roles;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserService {
    @Autowired
    final PasswordEncoder passwordEncoder;
    @Autowired
    final UserRepository userRepository;
    @Autowired
    final RoleRepository roleRepository;
    @Autowired
    final ObjectMapper objectMapper;
    @Autowired
    final RefreshTokenRepository refreshTokenRepository;
    @Autowired
    final JwtUtils jwtUtils;
    @Autowired
    EmailService emailService;
    @Autowired
    OtpRepository otpRepository;
    @Autowired
    UserCustomRepository userCustomRepository;
    @Autowired
    ProvinceRepository provinceRepository;
    @Autowired
    DistrictRepository districtRepository;
    @Autowired
    WardRepository wardRepository;

    @Value("${application.security.refreshToken.tokenValidityMilliseconds}")
    long refreshTokenValidityMilliseconds;

    public UserService(PasswordEncoder passwordEncoder, UserRepository userRepository,
                       RoleRepository roleRepository, ObjectMapper objectMapper,
                       RefreshTokenRepository refreshTokenRepository, JwtUtils jwtUtils) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.objectMapper = objectMapper;
        this.refreshTokenRepository = refreshTokenRepository;
        this.jwtUtils = jwtUtils;
    }

    public void registerUser(RegistrationRequest registrationRequest) {
        Optional<Role> optionalRole = roleRepository.findByName(Roles.USER);
        Set<Role> roles = new HashSet<>();
        roles.add(optionalRole.get());

        String name;
        if (registrationRequest.getFullName().equals("")) {
            int atIndex = registrationRequest.getEmail().indexOf('@');
            name = registrationRequest.getEmail().substring(0, atIndex);
        } else {
            name = registrationRequest.getFullName();
        }

        User user = User.builder()
                .fullName(name)
                .email(registrationRequest.getEmail())
                .password(passwordEncoder.encode(registrationRequest.getPassword()))
                .roles(roles)
                .avatar("https://firebasestorage.googleapis.com/v0/b/fir-e9a96.appspot.com/o/images%2Fu_60x60-267f0ca0ea48fd3acfd44b95afa64f01.png?alt=media&token=894f32ca-266a-40c1-81c0-eb7f8142f13a")
                .phone("")
                .build();
        userRepository.save(user);
        emailService.sendOtpActivedAccount(user.getEmail());
    }

    public JwtResponse refreshToken(RefreshTokenRequest request, HttpServletResponse response) throws RefreshTokenNotFoundException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        String newToken = userRepository.findById(userDetails.getId())
                .flatMap(user -> refreshTokenRepository.findByUserAndRefreshTokenAndInvalidated(user, request.getRefreshToken(), false)
                        .map(refreshToken -> {
                            LocalDateTime createdDateTime = refreshToken.getCreatedDateTime();
                            LocalDateTime expiryTime = createdDateTime.plusSeconds(refreshTokenValidityMilliseconds / 1000);
                            if (expiryTime.isBefore(LocalDateTime.now())) {
                                refreshToken.setInvalidated(true);
                                refreshTokenRepository.save(refreshToken);
                                return null;
                            }
                            return jwtUtils.generateJwtToken(authentication);
                        }))
                .orElseThrow(() -> new UsernameNotFoundException("Tài khoản không tồn tại"));

        if (newToken == null) {
            throw new RefreshTokenNotFoundException();
        }
        JwtResponse jwtResponse = JwtResponse.builder()
                .jwt(newToken)
                .build();

        Cookie jwtCookie = new Cookie("jwtToken", jwtResponse.getJwt());
        jwtCookie.setPath("/");
        response.addCookie(jwtCookie);
        return JwtResponse.builder()
                .jwt(newToken)
                .build();
    }

    @Transactional
    public void logout() {
        Optional<Long> userIdOptional = SecurityUtils.getCurrentUserLoginId();
        if (userIdOptional.isEmpty()) {
            throw new UsernameNotFoundException("Tài khoản không tồn tại");
        }
        refreshTokenRepository.logOut(userIdOptional.get());
        SecurityContextHolder.clearContext();
    }

    public void createUser(CreateUserRequest request) throws ExistedUserException {
        Optional<User> userOptional = userRepository.findByEmail(request.getEmail());
        if (userOptional.isPresent()) {
            throw new ExistedUserException();
        }

        Set<Role> roles = roleRepository.findByName(Roles.USER).stream().collect(Collectors.toSet());
        String avatar;
        if (request.getAvatar() == null) {
            avatar = "https://firebasestorage.googleapis.com/v0/b/fir-e9a96.appspot.com/o/images%2Fu_60x60-267f0ca0ea48fd3acfd44b95afa64f01.png?alt=media&token=894f32ca-266a-40c1-81c0-eb7f8142f13a";
        } else {
            avatar = request.getAvatar();
        }

        User user = User.builder()
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .roles(roles)
                .avatar(avatar)
                .build();
        userRepository.save(user);
    }

    public void activeAccount(String otpCode) {
        Otp otp = otpRepository.findByOtpCode(otpCode).get();
        User user = otp.getUser();
        user.setActivated(true);
        userRepository.save(user);
    }

    public void sendOtp(String email) {
        emailService.sendOtp(email);
    }


    public Optional<User> findByEmailAndActivated(String email) {
        return userRepository.findByEmailAndActivated(email, true);
    }

    public void checkOtp(String otpCode) throws OtpExpiredException {
        Otp otp = otpRepository.findByOtpCode(otpCode).get();
        if (LocalDateTime.now().isAfter(otp.getExpiredAt())) {
            throw new OtpExpiredException();
        }
    }

    public void resetPassword(ResetPasswordRequest resetPasswordRequest) throws OtpExpiredException {
        Otp otp = otpRepository.findByOtpCode(resetPasswordRequest.getOtpCode()).get();
        if (LocalDateTime.now().isAfter(otp.getExpiredAt())) {
            throw new OtpExpiredException();
        }
        User user = otp.getUser();
        user.setPassword(passwordEncoder.encode(resetPasswordRequest.getNewPassword()));
        userRepository.save(user);
    }

    public CommonResponse<?> searchUser(UserSearchRequest request) {
        try {
            List<UserResponse> users = userCustomRepository.searchUser(request);
            Integer pageIndex = request.getPageIndex();
            Integer pageSize = request.getPageSize();
            PaginationUtils<UserResponse> paginationUtils = new PaginationUtils<>();
            int pageNumber = paginationUtils.getPageNumber(users, pageSize);
            users = paginationUtils.searchData(users, pageIndex, pageSize);
            return CommonResponse.builder()
                    .pageNumber(pageNumber)
                    .data(users)
                    .build();
        } catch (Exception e) {
            throw new NotFoundException("Page index out of bound!");
        }
    }

    public void lockedOrUnlockedUser(Long userId) {
        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isEmpty()) {
            throw new NotFoundException("User not found");
        }
        User user = userOptional.get();
        user.setLocked(!user.isLocked());
        userRepository.save(user);
    }

    public UserResponse findUserById(Long userId) {
        return userCustomRepository.getUserById(userId);
    }

    public void updateUser(UserRequest request) {
        Long id = SecurityUtils.getCurrentUserLoginId().get();
        User user = userRepository.findById(id).get();
        Gender gender = Gender.convertGender(request.getGender());

        user.setAvatar(request.getAvatar().equals("") ? user.getAvatar() : request.getAvatar());
        user.setAbout(request.getAbout());
        user.setFullName(request.getFullName());
        user.setDob(request.getDob());
        user.setGender(gender);
        user.setPhone(request.getPhone());
        userRepository.save(user);
    }

    public void changePassword(ChangePasswordRequest request) throws BadRequestException {
        Long currentUserLoginId = SecurityUtils.getCurrentUserLoginId().get();
        User user = userRepository.findById(currentUserLoginId).get();
        String newPassword = request.getNewPassword();
//        String reNewPassword = request.getRePassword();
//        String curentPassword = passwordEncoder.encode(request.getCurrentPassword());
        if (!passwordEncoder.matches(request.getCurrentPassword(), user.getPassword())) {
            throw new BadRequestException();
        }
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }

    public List<LocationResponse> getAllProvince() {
        List<Province> provinces = provinceRepository.findAll();
        List<LocationResponse> result = new ArrayList<>();
        provinces.forEach(p -> {
            LocationResponse locationResponse = LocationResponse.builder()
                    .provinceCode(p.getCode())
                    .provinceName(p.getName())
                    .build();
            result.add(locationResponse);
        });
        return result;
    }

    public List<LocationResponse> getDistricts(String provinceCode) throws BadRequestException {
        Optional<List<District>> districtOptional = districtRepository.findAllByProvinceCode(provinceCode);
        if (districtOptional.isEmpty()) {
            throw new BadRequestException();
        }
        List<LocationResponse> result = new ArrayList<>();
        districtOptional.get().forEach(p -> {
            LocationResponse locationResponse = LocationResponse.builder()
                    .districtCode(p.getCode())
                    .districtName(p.getName())
                    .build();
            result.add(locationResponse);
        });
        return result;
    }

    public List<LocationResponse> getWards(String districtCode) throws BadRequestException {
        Optional<List<Ward>> wardOptional = wardRepository.findAllByDistrictCode(districtCode);
        if (wardOptional.isEmpty()) {
            throw new BadRequestException();
        }
        List<LocationResponse> result = new ArrayList<>();
        wardOptional.get().forEach(p -> {
            LocationResponse locationResponse = LocationResponse.builder()
                    .wardCode(p.getCode())
                    .wardName(p.getName())
                    .build();
            result.add(locationResponse);
        });
        return result;
    }

    public void updateAddress(WardRequest wardRequest) {
        Long id = SecurityUtils.getCurrentUserLoginId().get();
        User user = userRepository.findById(id).get();
        user.setAddress(wardRepository.findByCode(wardRequest.getWardCode()));
        user.setStreet(wardRequest.getStreet());
        userRepository.save(user);
    }
}