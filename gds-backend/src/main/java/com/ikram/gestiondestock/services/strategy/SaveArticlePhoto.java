package com.ikram.gestiondestock.services.strategy;

import com.ikram.gestiondestock.dto.ArticleDto;
import com.ikram.gestiondestock.exception.ErrorCodes;
import com.ikram.gestiondestock.exception.InvalidOperationException;
import com.ikram.gestiondestock.model.Article;
import com.ikram.gestiondestock.services.ArticleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Slf4j
@Service("articlePhotoLocalStrategy")
public class SaveArticlePhoto implements Strategy<ArticleDto> {

    private static final String PHOTO_DIR = "uploads/articles/";

    private final ArticleService articleService;

    public SaveArticlePhoto(ArticleService articleService) {
        this.articleService = articleService;
    }

    @Override
    public ArticleDto savePhoto(Integer id, InputStream photo, String titre) {

        if (photo == null || titre == null || titre.isBlank()) {
            throw new InvalidOperationException(
                    "Photo ou titre invalide",
                    ErrorCodes.PHOTO_SAVE_FAILED
            );
        }

        ArticleDto article = articleService.findById(id);

        String safeTitle = titre.replaceAll("[^a-zA-Z0-9-_]", "_");
        String fileName = id + "_" + safeTitle + ".jpg";

        Path filePath = Paths.get(PHOTO_DIR, fileName);

        try {
            Files.createDirectories(filePath.getParent());
            Files.copy(photo, filePath, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            log.error("Erreur sauvegarde photo article {}", id, e);
            throw new InvalidOperationException(
                    "Erreur lors de l'enregistrement de la photo",
                    ErrorCodes.PHOTO_SAVE_FAILED
            );
        }

        // ✔ URL relative exposée par Spring
        article.setPhoto("/photos/articles/" + fileName);

        return articleService.save(article);
    }
}
