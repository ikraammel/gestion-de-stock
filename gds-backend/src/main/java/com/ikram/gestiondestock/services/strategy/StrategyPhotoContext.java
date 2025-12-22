package com.ikram.gestiondestock.services.strategy;

import com.ikram.gestiondestock.exception.ErrorCodes;
import com.ikram.gestiondestock.exception.InvalidOperationException;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.Map;

@Service
public class StrategyPhotoContext {

    private final Map<String, Strategy<?>> strategies;

    public StrategyPhotoContext(Map<String, Strategy<?>> strategies) {
        this.strategies = strategies;
    }

    public Object savePhoto(String strategyName, Integer id, InputStream photo, String titre) {

        Strategy<?> strategy = strategies.get(strategyName);

        if (strategy == null) {
            throw new InvalidOperationException(
                    "Stratégie inconnue : " + strategyName,
                    ErrorCodes.UNKNOWN_CONTEXT
            );
        }

        return strategy.savePhoto(id, photo, titre);
    }
}
