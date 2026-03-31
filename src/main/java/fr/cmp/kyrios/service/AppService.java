package fr.cmp.kyrios.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import fr.cmp.kyrios.dao.AppDao;
import fr.cmp.kyrios.dao.ReferenceDao;
import fr.cmp.kyrios.mapper.AppMapper;
import fr.cmp.kyrios.model.App.dto.AppDTOCreate;
import fr.cmp.kyrios.model.App.dto.AppDTOResponse;

@Service
public class AppService {
    @Autowired
    private AppDao appJdbcRepository;

    @Autowired
    private ReferenceDao referenceDao;

    public List<AppDTOResponse> listAll() {
        return appJdbcRepository.findAll().stream()
                .map(AppMapper::toDto)
                .toList();
    }

    public AppDTOResponse getById(int id) {
        AppDao.AppReadRow row = appJdbcRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Application avec l'ID " + id + " non trouvee"));
        return AppMapper.toDto(row);
    }

    @Transactional
    public AppDTOResponse create(AppDTOCreate dto) {
        if (appJdbcRepository.existsByName(dto.getName())) {
            throw new IllegalArgumentException("Une application avec le nom '" + dto.getName() + "' existe déjà");
        }

        if (!referenceDao.existsDirectionById(dto.getDirectionId())) {
            throw new IllegalArgumentException("Direction avec l'ID " + dto.getDirectionId() + " non trouvee");
        }

        int appId = appJdbcRepository.insert(
                dto.getName(),
                dto.getDirectionId(),
                dto.getDescription(),
                LocalDateTime.now());

        return getById(appId);
    }

    @Transactional
    public AppDTOResponse update(int id, AppDTOCreate dto) {
        AppDao.AppReadRow app = appJdbcRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Application avec l'ID " + id + " non trouvée"));

        if (!app.name().equals(dto.getName()) && appJdbcRepository.existsByNameExcludingId(dto.getName(), id)) {
            throw new IllegalArgumentException("Une application avec le nom '" + dto.getName() + "' existe déjà");
        }

        if (!referenceDao.existsDirectionById(dto.getDirectionId())) {
            throw new IllegalArgumentException("Direction avec l'ID " + dto.getDirectionId() + " non trouvee");
        }

        appJdbcRepository.update(id, dto.getName(), dto.getDirectionId(), dto.getDescription());

        return getById(id);
    }

    @Transactional
    public void delete(int id) {
        if (appJdbcRepository.findById(id).isEmpty()) {
            throw new IllegalArgumentException("Application avec l'ID " + id + " non trouvee");
        }
        appJdbcRepository.deleteById(id);
    }

}
