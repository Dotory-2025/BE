package com.dotoryteam.dotory.domain.lifestyle.controller;

import com.dotoryteam.dotory.domain.lifestyle.dto.LifestyleManageDTO;
import com.dotoryteam.dotory.domain.lifestyle.service.LifestyleService;
import com.dotoryteam.dotory.global.common.dto.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/admin/lifestyle")
public class LifestyleAdminController {
    private final LifestyleService lifestyleService;

    @GetMapping("/all")
    public ResponseEntity<ApiResponse<List<LifestyleManageDTO>>> all() {
        return ApiResponse.ok(lifestyleService.get());
    }

    @PostMapping("/")
    public ResponseEntity<ApiResponse<Void>> createLifestyle(@RequestBody LifestyleManageDTO add) {
        lifestyleService.addLifestyle(add);

        return ApiResponse.ok();
    }

    @PutMapping("/")
    public ResponseEntity<ApiResponse<Void>> updateLifestyle(@RequestBody LifestyleManageDTO add) {
        lifestyleService.updateLifestyle(add);

        return ApiResponse.ok();
    }

    // 미리 확인하고 삭제 해주세요. 가급적이면 수정을 사용해주세요
    @DeleteMapping("/")
    public ResponseEntity<ApiResponse<Void>> deleteLifestyle(@RequestParam Long id) {
        lifestyleService.deleteLifestyle(id);

        return ApiResponse.ok();
    }
}
