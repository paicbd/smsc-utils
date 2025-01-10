package com.paicbd.smsc.dto;

import com.paicbd.smsc.utils.DataType;

public record FieldMapping(
        String sourceProperty,
        String targetProperty,
        DataType dataType
) {
}