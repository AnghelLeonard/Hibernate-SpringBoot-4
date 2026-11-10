package com.bookstore.function.config;

import org.hibernate.boot.model.FunctionContributions;
import org.hibernate.boot.model.FunctionContributor;
import org.hibernate.type.BasicType;
import org.hibernate.type.StandardBasicTypes;
import org.hibernate.type.spi.TypeConfiguration;

public class MyFunctions implements FunctionContributor {

    @Override
    public void contributeFunctions(FunctionContributions fc) {

        TypeConfiguration typeConfiguration = fc.getTypeConfiguration();
        BasicType<String> stringType = typeConfiguration
                .getBasicTypeRegistry().resolve(StandardBasicTypes.STRING);
        BasicType<Integer> intType = typeConfiguration
                .getBasicTypeRegistry().resolve(StandardBasicTypes.INTEGER);
      
        // Register the 'width_bucket' function
        fc.getFunctionRegistry().registerPattern(
            "width_bucket",
            "width_bucket(?1, ?2, ?3, ?4)",
            intType
        );      
        
        // Register the 'slugify' function
        fc.getFunctionRegistry().registerPattern(
            "slugify", 
            "slugify(?1)", 
            stringType
        );
        
        // Register 'apply_discount' function
        fc.getFunctionRegistry().registerPattern(
            "apply_discount", 
            "apply_discount(?1, ?2)", 
            intType
        );
    }
}
