package com.bookstore.function.config;

import org.hibernate.boot.model.FunctionContributions;
import org.hibernate.boot.model.FunctionContributor;
import org.hibernate.query.sqm.function.NamedSqmFunctionDescriptor;
import org.hibernate.query.sqm.produce.function.StandardArgumentsValidators;
import org.hibernate.query.sqm.produce.function.StandardFunctionReturnTypeResolvers;
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
      
        // Register the 'field' function
        NamedSqmFunctionDescriptor fieldDescriptor = new NamedSqmFunctionDescriptor(
            "field",                                                   // Function name used in HQL/JPQL
            true,                                                      // Always include parentheses
            StandardArgumentsValidators.min(2),                        // Enforce a minimum of 2 parameters               
            StandardFunctionReturnTypeResolvers.invariant(intType),    // Return type is always Integer
            null                                                       // No argument type overrides needed
        );

        fc.getFunctionRegistry().register("field", fieldDescriptor);
        
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
