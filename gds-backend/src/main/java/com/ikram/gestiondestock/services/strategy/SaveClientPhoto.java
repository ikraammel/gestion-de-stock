package com.ikram.gestiondestock.services.strategy;

import com.ikram.gestiondestock.dto.ClientDto;
import com.ikram.gestiondestock.exception.ErrorCodes;
import com.ikram.gestiondestock.exception.InvalidOperationException;
import com.ikram.gestiondestock.services.ClientService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Slf4j
@Service("clientPhotoLocalStrategy")
public class SaveClientPhoto implements Strategy<ClientDto> {

    private static final String PHOTO_DIR = "uploads/clients/";

    private final ClientService clientService;

    public SaveClientPhoto(ClientService clientService) {
        this.clientService = clientService;
    }

    @Override
    public ClientDto savePhoto(Integer id, InputStream photo, String titre) {

        if (photo == null || titre == null || titre.isBlank()) {
            throw new InvalidOperationException(
                    "Photo ou titre invalide",
                    ErrorCodes.PHOTO_SAVE_FAILED
            );
        }

        ClientDto client = clientService.findById(id);

        String safeTitle = titre.replaceAll("[^a-zA-Z0-9-_]", "_");
        String fileName = id + "_" + safeTitle + ".jpg";

        Path filePath = Paths.get(PHOTO_DIR, fileName);

        try {
            Files.createDirectories(filePath.getParent());
            Files.copy(photo, filePath, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            log.error("Erreur sauvegarde photo client {}", id, e);
            throw new InvalidOperationException(
                    "Erreur lors de l'enregistrement de la photo client",
                    ErrorCodes.PHOTO_SAVE_FAILED
            );
        }

        client.setPhoto("/photos/clients/" + fileName);
        return clientService.save(client);
    }
}

