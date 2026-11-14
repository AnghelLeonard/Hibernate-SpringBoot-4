package com.bookstore.service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.metamodel.Attribute;
import jakarta.persistence.metamodel.EntityType;
import jakarta.persistence.metamodel.Metamodel;
import jakarta.persistence.metamodel.PluralAttribute;
import jakarta.persistence.metamodel.SingularAttribute;
import java.util.Comparator;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class JpaMetamodelService {

    @PersistenceContext
    private EntityManager entityManager;

    public void printJpaMetamodel() {
        Metamodel metamodel = entityManager.getMetamodel();

        List<EntityType<?>> entities = metamodel.getEntities().stream()
                .sorted(Comparator.comparing(EntityType::getName))
                .toList();

        System.out.println("\n================================================================================");
        System.out.println(" JPA METAMODEL INSPECTION (Total Entities: " + entities.size() + ")");
        System.out.println("================================================================================");

        for (EntityType<?> entity : entities) {
            System.out.printf("%nENTITY: %s (Java Class: %s)%n",
                    entity.getName(),
                    entity.getJavaType().getName()
            );

            // Inspect primary key (@Id or @EmbeddedId)
            if (entity.hasSingleIdAttribute()) {
                Class<?> idJavaType = entity.getIdType().getJavaType();
                SingularAttribute<?, ?> idAttr = entity.getId(idJavaType);
                System.out.printf("  Identifier   : %s (%s)%n",
                        idAttr.getName(),
                        idJavaType.getSimpleName()
                );
            } else {
                System.out.println("  Identifier   : Composite (@IdClass)");
            }

            // Inspect all persistent attributes
            System.out.println("  Attributes:");
            System.out.printf("    %-16s %-22s %-18s %s%n",
                    "NAME", "JAVA TYPE", "PERSISTENCE TYPE", "DETAILS");
            System.out.println("    ----------------------------------------------------------------------------");

            for (Attribute<?, ?> attr : entity.getAttributes()) {
                System.out.printf("    %-16s %-22s %-18s %s%n",
                        attr.getName(),
                        resolveJavaTypeName(attr),
                        attr.getPersistentAttributeType(),
                        resolveDetails(attr)
                );
            }
        }

        System.out.println("\n================================================================================\n");
    }

    private String resolveJavaTypeName(Attribute<?, ?> attr) {
        if (attr instanceof PluralAttribute<?, ?, ?> pluralAttr) {
            return String.format("%s<%s>",
                    pluralAttr.getJavaType().getSimpleName(),
                    pluralAttr.getElementType().getJavaType().getSimpleName()
            );
        }
        return attr.getJavaType().getSimpleName();
    }

    private String resolveDetails(Attribute<?, ?> attr) {
        if (attr instanceof SingularAttribute<?, ?> singularAttr) {
            if (singularAttr.isId()) {
                return "Primary Key (@Id)";
            }
            if (singularAttr.isVersion()) {
                return "Optimistic Lock (@Version)";
            }
            return singularAttr.isOptional() ? "Optional (Nullable)" : "Required (Non-null)";
        }
        if (attr.isCollection()) {
            return "Collection Association";
        }
        return "-";
    }
}
