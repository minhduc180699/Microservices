package com.saltlux.deepsignal.web.util;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import org.modelmapper.ModelMapper;

public class ObjectMapperUtils {

    private static final ModelMapper modelMapper;

    static {
        modelMapper = new ModelMapper();
    }

    private ObjectMapperUtils() {}

    public static <D, T> D map(T entity, Class<D> outClass) {
        if (entity == null) return null;
        return modelMapper.map(entity, outClass);
    }

    public static <D, T> List<D> mapAll(Collection<T> entityList, Class<D> outCLass) {
        if (entityList == null) return null;
        return entityList.stream().map(entity -> map(entity, outCLass)).collect(Collectors.toList());
    }

    public static <S, D> D map(S source, D destination) {
        modelMapper.map(source, destination);
        return destination;
    }
}
