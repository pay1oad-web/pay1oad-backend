package com.pay1oad.homepage.controller.admin;


import com.pay1oad.homepage.dto.ResponseDTO;
import com.pay1oad.homepage.dto.admin.AdminRequestDTO;
import com.pay1oad.homepage.response.ResForm;
import com.pay1oad.homepage.response.code.status.InSuccess;
import com.pay1oad.homepage.service.admin.AdminService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Operation(summary = "권한을 변경하는 API", description = "userId에 변경할 사람의 id, memberAuth에 변경할 권한을 넣어주세요.")
    @PostMapping("/change_auth")
    public ResForm<String> changeAuth(@Valid @RequestBody AdminRequestDTO.ToChangeMemberAuthDTO toChangeMemberAuthDTO) {
        adminService.changeMemberAuth(toChangeMemberAuthDTO);
        return ResForm.onSuccess(InSuccess.AUTH_CHANGED, "권한 정보가 수정되었습니다.");

    }
}
