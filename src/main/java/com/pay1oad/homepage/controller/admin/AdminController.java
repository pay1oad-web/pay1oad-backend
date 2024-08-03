package com.pay1oad.homepage.controller.admin;


import com.pay1oad.homepage.dto.ResponseDTO;
import com.pay1oad.homepage.dto.admin.AdminRequestDTO;
import com.pay1oad.homepage.dto.admin.AdminResponseDTO;
import com.pay1oad.homepage.dto.login.MemberDTO;
import com.pay1oad.homepage.model.login.Member;
import com.pay1oad.homepage.service.admin.AdminService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "http://localhost:3000")
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/admin")
public class AdminController {
    private final AdminService adminService;

    @PostMapping("/change_auth")
    public ResponseEntity<?> changeAuth(@RequestBody AdminRequestDTO.ToChangeMemberAuthDTO toChangeMemberAuthDTO) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

            boolean hasRequiredRole = authentication.getAuthorities().stream()
                    .map(GrantedAuthority::getAuthority)
                    .anyMatch(role -> role.equals("ROLE_ADMIN") || role.equals("ROLE_OPERATOR"));

            if (!hasRequiredRole) {
                throw new AccessDeniedException("Access denied");
            }

            adminService.changeMemberAuth(toChangeMemberAuthDTO);

            return ResponseEntity.ok().body("Member Auth changed");

        }catch(Exception e){
            ResponseDTO responseDTO=ResponseDTO.builder().error(e.getMessage()).build();
            return ResponseEntity
                    .badRequest()
                    .body(responseDTO);
        }
    }
}
