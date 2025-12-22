package com.ikram.gestiondestock.interceptor;


import org.slf4j.MDC;
import org.springframework.util.StringUtils;


import org.hibernate.resource.jdbc.spi.StatementInspector;

public class Interceptor implements StatementInspector {

    @Override
    public String inspect(String sql) {
        if (StringUtils.hasLength(sql) && sql.toLowerCase().startsWith("select")) {
            // Récupérer le nom de l’entité dans la requête
            final String entityName = sql.substring(7, sql.indexOf("."));
            final String idEntreprise = MDC.get("idEntreprise");

            if (StringUtils.hasLength(entityName)
                    && !entityName.toLowerCase().contains("entreprise")
                    && !entityName.toLowerCase().contains("roles")
                    && StringUtils.hasLength(idEntreprise)) {

                if (sql.contains("where")) {
                    sql = sql + " and " + entityName + ".entrepriseId = " + idEntreprise;
                } else {
                    sql = sql + " where " + entityName + ".entrepriseId = " + idEntreprise;
                }
            }
        }
        return sql;
    }
}
