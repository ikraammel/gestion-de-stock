package com.ikram.gestiondestock.services.strategy;

import com.ikram.gestiondestock.dto.UtilisateurDto;
import com.ikram.gestiondestock.exception.ErrorCodes;
import com.ikram.gestiondestock.exception.InvalidOperationException;
import com.ikram.gestiondestock.services.UtilisateurService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Slf4j
@Service("utilisateurPhotoLocalStrategy")
public class SaveUtilisateurPhoto implements Strategy<UtilisateurDto> {

    private static final String PHOTO_DIR = "uploads/utilisateurs/";

    private final UtilisateurService utilisateurService;

    public SaveUtilisateurPhoto(UtilisateurService utilisateurService) {
        this.utilisateurService = utilisateurService;
    }

    @Override
    public UtilisateurDto savePhoto(Integer id, InputStream photo, String titre) {

        if (photo == null || titre == null || titre.isBlank()) {
            throw new InvalidOperationException(
                    "Photo ou titre invalide",
                    ErrorCodes.PHOTO_SAVE_FAILED
            );
        }

        UtilisateurDto utilisateurDto = utilisateurService.findById(id);

        String safeTitle = titre.replaceAll("[^a-zA-Z0-9-_]", "_");
        String fileName = id + "_" + safeTitle + ".jpg";

        Path filePath = Paths.get(PHOTO_DIR, fileName);

        try {
            Files.createDirectories(filePath.getParent());
            Files.copy(photo, filePath, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            log.error("Erreur sauvegarde photo Utilisateur {}", id, e);
            throw new InvalidOperationException(
                    "Erreur lors de l'enregistrement de la photo Utilisateur",
                    ErrorCodes.PHOTO_SAVE_FAILED
            );
        }

        utilisateurDto.setPhoto("/photos/utilisateurs/" + fileName);
        return utilisateurService.save(utilisateurDto);
    }
}


