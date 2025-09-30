package com.youthfi.finance.domain.stock.domain.service;

import com.youthfi.finance.domain.stock.domain.entity.Sector;
import com.youthfi.finance.domain.stock.domain.repository.SectorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class SectorService {

    private final SectorRepository sectorRepository;

    /**
     * 섹터 생성
     */
    @Transactional
    public Sector createSector(String sectorName) {
        Sector sector = Sector.builder()
                .sectorName(sectorName)
                .build();

        return sectorRepository.save(sector);
    }

    /**
     * 섹터 조회 (ID)
     */
    public Optional<Sector> getSectorById(Long sectorId) {
        return sectorRepository.findById(sectorId);
    }

    /**
     * 섹터 조회 (이름)
     */
    public Optional<Sector> getSectorByName(String sectorName) {
        return sectorRepository.findBySectorName(sectorName);
    }

    /**
     * 섹터 검색 (부분 일치)
     */
    public List<Sector> searchSectorsByName(String sectorName) {
        return sectorRepository.findBySectorNameContaining(sectorName);
    }

    /**
     * 모든 섹터 조회
     */
    public List<Sector> getAllSectors() {
        return sectorRepository.findAll();
    }

    /**
     * 섹터 수정
     */
    @Transactional
    public Sector updateSector(Long sectorId, String sectorName) {
        Sector sector = sectorRepository.findById(sectorId)
                .orElseThrow(() -> new RuntimeException("섹터를 찾을 수 없습니다: " + sectorId));

        sector.updateSectorName(sectorName);
        return sectorRepository.save(sector);
    }

    /**
     * 섹터 삭제
     */
    @Transactional
    public void deleteSector(Long sectorId) {
        Sector sector = sectorRepository.findById(sectorId)
                .orElseThrow(() -> new RuntimeException("섹터를 찾을 수 없습니다: " + sectorId));

        sectorRepository.delete(sector);
    }

    /**
     * 섹터 존재 여부 확인
     */
    public boolean existsSector(Long sectorId) {
        return sectorRepository.existsById(sectorId);
    }

    /**
     * 섹터명 존재 여부 확인
     */
    public boolean existsSectorByName(String sectorName) {
        return sectorRepository.findBySectorName(sectorName).isPresent();
    }
}
