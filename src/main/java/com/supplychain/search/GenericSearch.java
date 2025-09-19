package com.supplychain.search;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.jpa.domain.Specification;

import com.supplychain.mapper.GenericMapper;

import jakarta.persistence.criteria.Predicate;

public class GenericSearch<DTO, ENTITY> {

    private final GenericMapper<DTO, ENTITY> mapper;

    public GenericSearch(GenericMapper<DTO, ENTITY> mapper) {
	this.mapper = mapper;
    }

    public Specification<ENTITY> buildSpecification(DTO dto) {
	ENTITY entity = mapper.toEntity(dto); // MapStruct mapping

	return (root, query, cb) -> {
	    List<Predicate> predicates = new ArrayList<>();

	    // فقط فیلدهای DTO که null نیستند را بررسی می‌کنیم
	    for (var field : dto.getClass().getDeclaredFields()) {
		field.setAccessible(true);
		try {
		    Object value = field.get(dto);
		    if (value != null) {
			predicates.add(cb.equal(root.get(field.getName()), value));
		    }
		} catch (IllegalAccessException ignored) {
		}
	    }

	    return cb.and(predicates.toArray(new Predicate[0]));
	};
    }
}