package com.ubb.userservice.service.auth;

import com.ubb.userservice.common.dto.NotificationProcessorSenderRequestDto;
import com.ubb.userservice.common.dto.auth.AuthRequestDto;
import com.ubb.userservice.common.dto.auth.CurrentUserResponseDto;
import com.ubb.userservice.common.dto.auth.SignInRequestDto;
import com.ubb.userservice.common.enums.ErrorCode;
import com.ubb.userservice.common.enums.NotificationChannel;
import com.ubb.userservice.common.enums.NotificationType;
import com.ubb.userservice.common.security.AppUserPrincipal;
import com.ubb.userservice.config.exception.BadRequestException;
import com.ubb.userservice.config.exception.UnAuthorizedException;
import com.ubb.userservice.config.properties.MessagingProperties;
import com.ubb.userservice.mapper.UserMapper;
import com.ubb.userservice.model.UserDocument;
import com.ubb.userservice.repository.UserRepository;
import com.ubb.userservice.service.notification.NotificationProcessorSenderService;
import com.ubb.userservice.service.usersettings.UserSettingsService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Arrays;
import java.util.Collections;

@Slf4j
@AllArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final AuthenticationManager authenticationManager;
    private final JwtMakerService jwtMakerService;
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final UserSettingsService userSettingsService;
    private final NotificationProcessorSenderService notificationProcessorSenderService;
    private final MessagingProperties messagingProperties;

    @Override
    public String login(AuthRequestDto authRequestDto) {
        try {
            final var authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authRequestDto.getEmail(), authRequestDto.getPassword()));
            final var userDetails = (AppUserPrincipal) authentication.getPrincipal();
            return jwtMakerService.generateJwt(userDetails.getId(), userDetails.getEmail());
        } catch (BadCredentialsException e) {
            log.error("Invalid username or password!");
            throw new UnAuthorizedException(ErrorCode.UNAUTHORIZED);
        }
    }

    @Override
    public CurrentUserResponseDto getCurrentUser(final AppUserPrincipal appUserPrincipal) {
        return userMapper.fromAppUserPrincipal(appUserPrincipal);
    }

    @Override
    public void signIn(SignInRequestDto signInRequestDto) {
        if (userRepository.existsByEmail(signInRequestDto.getEmail())) {
            log.error("An user with email {} already exist", signInRequestDto.getEmail());
            throw new BadRequestException(ErrorCode.EMAIL_IN_USE);
        }

        var userDocument = userMapper.fromSignInRequestDto(signInRequestDto);
        userDocument.setRoles(Collections.singleton(UserDocument.RoleItem.USER));
        userDocument.setPassword(passwordEncoder.encode(signInRequestDto.getPassword()));
        userDocument = userRepository.save(userDocument);

        // save user settings too
        userSettingsService.createDefaultUserSettingsForUser(userDocument.getId());

        // send notification to the new user
        final var notificationDetails = NotificationProcessorSenderRequestDto
                .builder()
                .notificationType(NotificationType.WELCOME)
                .notificationChannels(Collections.singleton(NotificationChannel.EMAIL))
                .details(
                        Arrays.asList(
                                NotificationProcessorSenderRequestDto.NotificationDetailsItemDto.builder()
                                        .key("firstName")
                                        .value(userDocument.getFirstName())
                                        .build(),
                                NotificationProcessorSenderRequestDto.NotificationDetailsItemDto.builder()
                                        .key("lastName")
                                        .value(userDocument.getLastName())
                                        .build(),
                                NotificationProcessorSenderRequestDto.NotificationDetailsItemDto.builder()
                                        .key("email")
                                        .value(userDocument.getEmail())
                                        .build()
                        )
                )
                .build();
        notificationProcessorSenderService.sendMessage(
                messagingProperties.getQueues().getNotifications(),
                notificationDetails
        );
    }
}
