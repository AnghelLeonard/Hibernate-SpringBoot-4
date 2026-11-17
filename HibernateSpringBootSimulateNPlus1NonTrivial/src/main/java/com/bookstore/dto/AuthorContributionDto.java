package com.bookstore.dto;

import java.math.BigDecimal;

public record AuthorContributionDto(
        String penName,
        String role,
        BigDecimal royaltySplit,
        String agencyName
) {}
