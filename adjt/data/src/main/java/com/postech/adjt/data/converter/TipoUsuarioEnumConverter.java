package com.postech.adjt.data.converter;

import com.postech.adjt.domain.enums.TipoUsuarioEnum;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class TipoUsuarioEnumConverter implements AttributeConverter<TipoUsuarioEnum, String> {

    @Override
    public String convertToDatabaseColumn(TipoUsuarioEnum tipoUsuario) {
        return tipoUsuario != null ? tipoUsuario.toString() : null;
    }

    @Override
    public TipoUsuarioEnum convertToEntityAttribute(String dbData) {
        return dbData != null ? TipoUsuarioEnum.fromString(dbData) : null;
    }
}