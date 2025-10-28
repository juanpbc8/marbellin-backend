package com.marbellin.iam.controller;

import com.marbellin.iam.dto.RegisterRequest;
import com.marbellin.iam.entity.UserEntity;
import com.marbellin.iam.entity.enums.RoleEnum;
import com.marbellin.iam.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(name = "Authentication", description = "Endpoints for user authentication, registration, and session management"
)
public class AuthController {

    private final UserService userService;

    @Operation(
            summary = "Register a new user account",
            description = "Creates a new user with role CUSTOMER. Does not create a Customer profile yet."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "User registered successfully"),
            @ApiResponse(responseCode = "409", description = "Email already in use", content = @Content)
    })
    @PostMapping("/register")
    public ResponseEntity<String> register(@Valid @RequestBody RegisterRequest request) {
        userService.createUser(request.email(), request.password(), RoleEnum.CUSTOMER);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body("User registered successfully");
    }

    @Operation(
            summary = "Login with email and password",
            description = """
                    Managed automatically by Spring Security.<br>
                    Send credentials using `application/x-www-form-urlencoded`:<br>
                    <pre>
                    username=email@example.com
                    password=yourPassword
                    </pre>
                    If valid, a session cookie (JSESSIONID) will be issued.
                    """
    )
    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login() {
        // Spring Security handles the real authentication
        return ResponseEntity.ok(Map.of("message", "Login successful"));
    }

    @Operation(
            summary = "Logout current user",
            description = "Invalidates the current session and removes the JSESSIONID cookie."
    )
    @PostMapping("/logout")
    public ResponseEntity<Map<String, String>> logout() {
        return ResponseEntity.ok(Map.of("message", "Logout successful"));
    }

    @Operation(
            summary = "Get information of the authenticated user",
            description = "Returns the authenticated user's email and roles based on the current session."
    )
    @GetMapping("/me")
    public ResponseEntity<?> me(@AuthenticationPrincipal UserDetails userDetails) {
        if (userDetails == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "No active session"));
        }

        UserEntity user = userService.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new IllegalStateException("User not found"));

        return ResponseEntity.ok(Map.of(
                "id", user.getId(),
                "email", user.getEmail(),
                "roles", user.getRoles().stream()
                        .map(role -> role.getRoleName().name())
                        .toList(),
                "enabled", user.isEnabled()
        ));
    }

    @Operation(
            summary = "Check if the session is active",
            description = "Returns `true` if the user is authenticated (session active), otherwise `false`."
    )
    @GetMapping("/status")
    public ResponseEntity<Map<String, Object>> sessionStatus(
            @AuthenticationPrincipal UserDetails userDetails) {

        boolean loggedIn = userDetails != null;
        return ResponseEntity.ok(Map.of(
                "authenticated", loggedIn,
                "user", loggedIn ? userDetails.getUsername() : null
        ));
    }
}
