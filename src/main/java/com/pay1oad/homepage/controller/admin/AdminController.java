package com.pay1oad.homepage.controller.admin;


import com.pay1oad.homepage.dto.ResponseDTO;
import com.pay1oad.homepage.dto.admin.AdminRequestDTO;
import com.pay1oad.homepage.service.admin.AdminService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "http://localhost:3000")
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/admin")
public class AdminController {
    private final AdminService adminService;

    @PostMapping("/change_auth")
    public ResponseEntity<?> changeAuth(@Valid @RequestBody AdminRequestDTO.ToChangeMemberAuthDTO toChangeMemberAuthDTO) {
        adminService.changeMemberAuth(toChangeMemberAuthDTO);

        return ResponseEntity.ok().body("Member Auth changed");

    }
}
