package com.ikram.gestiondestock.services.strategy;

import com.ikram.gestiondestock.dto.EntrepriseDto;
import com.ikram.gestiondestock.exception.ErrorCodes;
import com.ikram.gestiondestock.exception.InvalidOperationException;
import com.ikram.gestiondestock.services.EntrepriseService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Slf4j
@Service("entreprisePhotoLocalStrategy")
public class SaveEntreprisePhoto implements Strategy<EntrepriseDto> {

    private static final String PHOTO_DIR = "uploads/entreprises/";

    private final EntrepriseService entrepriseService;

    public SaveEntreprisePhoto(EntrepriseService entrepriseService) {
        this.entrepriseService = entrepriseService;
    }

    @Override
    public EntrepriseDto savePhoto(Integer id, InputStream photo, String titre) {

        if (photo == null || titre == null || titre.isBlank()) {
            throw new InvalidOperationException(
                    "Photo ou titre invalide",
                    ErrorCodes.PHOTO_SAVE_FAILED
            );
        }

        EntrepriseDto entrepriseDto = entrepriseService.findById(id);

        String safeTitle = titre.replaceAll("[^a-zA-Z0-9-_]", "_");
        String fileName = id + "_" + safeTitle + ".jpg";

        Path filePath = Paths.get(PHOTO_DIR, fileName);

        try {
            Files.createDirectories(filePath.getParent());
            Files.copy(photo, filePath, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            log.error("Erreur sauvegarde photo entreprise {}", id, e);
            throw new InvalidOperationException(
                    "Erreur lors de l'enregistrement de la photo",
                    ErrorCodes.PHOTO_SAVE_FAILED
            );
        }

        // ✔ URL relative exposée par Spring
        entrepriseDto.setPhoto("/photos/entreprises/" + fileName);

        return entrepriseService.save(entrepriseDto);
    }
}
