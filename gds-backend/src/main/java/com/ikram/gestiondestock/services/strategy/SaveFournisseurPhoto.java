package com.ikram.gestiondestock.services.strategy;

import com.ikram.gestiondestock.dto.FournisseurDto;
import com.ikram.gestiondestock.exception.ErrorCodes;
import com.ikram.gestiondestock.exception.InvalidOperationException;
import com.ikram.gestiondestock.services.FournisseurService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Slf4j
@Service("fournisseurPhotoLocalStrategy")
public class SaveFournisseurPhoto implements Strategy<FournisseurDto> {

    private static final String PHOTO_DIR = "uploads/fournisseurs/";

    private final FournisseurService fournisseurService;

    public SaveFournisseurPhoto(FournisseurService fournisseurService) {
        this.fournisseurService = fournisseurService;
    }

    @Override
    public FournisseurDto savePhoto(Integer id, InputStream photo, String titre) {

        if (photo == null || titre == null || titre.isBlank()) {
            throw new InvalidOperationException(
                    "Photo ou titre invalide",
                    ErrorCodes.PHOTO_SAVE_FAILED
            );
        }

        FournisseurDto fournisseurDto = fournisseurService.findById(id);

        String safeTitle = titre.replaceAll("[^a-zA-Z0-9-_]", "_");
        String fileName = id + "_" + safeTitle + ".jpg";

        Path filePath = Paths.get(PHOTO_DIR, fileName);

        try {
            Files.createDirectories(filePath.getParent());
            Files.copy(photo, filePath, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            log.error("Erreur sauvegarde photo fournisseur {}", id, e);
            throw new InvalidOperationException(
                    "Erreur lors de l'enregistrement de la photo fournisseur",
                    ErrorCodes.PHOTO_SAVE_FAILED
            );
        }

        fournisseurDto.setPhoto("/photos/fournisseurs/" + fileName);
        return fournisseurService.save(fournisseurDto);
    }
}

