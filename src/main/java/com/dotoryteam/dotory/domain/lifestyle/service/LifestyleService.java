package com.dotoryteam.dotory.domain.lifestyle.service;

import com.dotoryteam.dotory.domain.lifestyle.dto.LifestyleManageDTO;
import com.dotoryteam.dotory.domain.lifestyle.entity.Lifestyle;
import com.dotoryteam.dotory.domain.lifestyle.exception.AlreadyExistLifestyleCodeException;
import com.dotoryteam.dotory.domain.lifestyle.exception.AlreadyExistLifestyleNameException;
import com.dotoryteam.dotory.domain.lifestyle.exception.LifestyleNotFoundException;
import com.dotoryteam.dotory.domain.lifestyle.repository.LifestyleRepository;
import com.dotoryteam.dotory.domain.lifestyle.repository.MemberLifestyleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LifestyleService {
    private final LifestyleRepository lifestyleRepository;
    private final MemberLifestyleRepository memberLifestyleRepository;

    public List<Lifestyle> convert(List<String> lifestyleCodes) {
        return lifestyleRepository.findByCodesIn(lifestyleCodes);
    }

    public List<LifestyleManageDTO> get() {
        return lifestyleRepository.findAll().stream()
                .map(l ->
                        LifestyleManageDTO.builder()
                                .id(l.getId())
                                .lifestyleName(l.getName())
                                .code(l.getCode())
                                .build()
                )
                .toList();
    }

    public void addLifestyle(LifestyleManageDTO add) {
        if (lifestyleRepository.existsByCode(add.getCode()))
            throw new AlreadyExistLifestyleCodeException();

        if (lifestyleRepository.existsByName(add.getLifestyleName()))
            throw new AlreadyExistLifestyleNameException();

        Lifestyle lifestyle = Lifestyle.builder()
                .name(add.getLifestyleName())
                .code(add.getCode())
                .build();

        lifestyleRepository.save(lifestyle);
    }

    @Transactional
    public void updateLifestyle(LifestyleManageDTO update) {
        if (update.getId() == null)
            throw new LifestyleNotFoundException();

        Lifestyle lifestyle = lifestyleRepository.findByLifestyleId(update.getId())
                .orElseThrow(LifestyleNotFoundException::new);

        lifestyle.updateLifestyle(update.getLifestyleName(), update.getCode());
    }

    @Transactional
    public void deleteLifestyle(Long id) {
        Lifestyle lifestyle = lifestyleRepository.findByLifestyleId(id)
                .orElseThrow(LifestyleNotFoundException::new);

        lifestyleRepository.deleteByLifestyleId(id);
        memberLifestyleRepository.bulkDeleteByDeletedLifestyles(id);        //  lifestyle 삭제로 인해 이미 선택한 사람들 키워드 저장 정보도 삭제
    }
}
