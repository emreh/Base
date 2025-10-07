package com.supplychain.base.search.specification;

import java.time.LocalDateTime;

import org.springframework.stereotype.Component;

import com.supplychain.base.search.dto.FilterRequest;
import com.supplychain.base.search.enums.FieldType;
import com.supplychain.base.search.enums.Operator;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.From;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

@Component
public class ParseRequest<T> {

    public Predicate builders(Operator operator, Root<T> root, CriteriaBuilder cb, FilterRequest request,
	    Predicate predicate) {

	FieldType type = request.getFieldType();
	Object value = request.getValue() != null ? type.parse(request.getValue().toString()) : null;
	Object valueTo = request.getValueTo() != null ? type.parse(request.getValueTo().toString()) : null;

	Expression<?> key = parse(root, request);

	return switch (operator) {
	case EQUAL -> cb.equal(key, value);
	case NOT_EQUAL -> cb.notEqual(key, value);
	case LIKE -> cb.like(key.as(String.class), "%" + value + "%");
	case IN -> {
	    CriteriaBuilder.In<Object> inClause = cb.in(key);
	    for (Object v : request.getValues()) {
		inClause.value(type.parse(v.toString()));
	    }
	    yield inClause;
	}
	case BETWEEN -> buildBetween(cb, key, type, value, valueTo);
	case GREATER_THAN -> buildComparison(cb, key, type, value, cb::gt, cb::greaterThan);
	case GREATER_THAN_OR_EQUAL -> buildComparison(cb, key, type, value, cb::ge, cb::greaterThanOrEqualTo);
	case LESS_THAN -> buildComparison(cb, key, type, value, cb::lt, cb::lessThan);
	case LESS_THAN_OR_EQUAL -> buildComparison(cb, key, type, value, cb::le, cb::lessThanOrEqualTo);
	};
    }

    private Predicate buildBetween(CriteriaBuilder cb, Expression<?> key, FieldType type, Object value,
	    Object valueTo) {
	if (type == FieldType.DATE) {
	    return cb.and(cb.greaterThanOrEqualTo(key.as(LocalDateTime.class), (LocalDateTime) value),
		    cb.lessThanOrEqualTo(key.as(LocalDateTime.class), (LocalDateTime) valueTo));
	}

	if (type != FieldType.CHAR && type != FieldType.BOOLEAN) {
	    return cb.and(cb.ge(key.as(Number.class), (Number) value), cb.le(key.as(Number.class), (Number) valueTo));
	}

	return null;
    }

    private Predicate buildComparison(CriteriaBuilder cb, Expression<?> key, FieldType type, Object value,
	    ComparisonFn<Number> numberFn, ComparisonFn<LocalDateTime> dateFn) {
	if (type == FieldType.DATE) {
	    return dateFn.apply(key.as(LocalDateTime.class), (LocalDateTime) value);
	}

	if (type != FieldType.CHAR && type != FieldType.BOOLEAN) {
	    return numberFn.apply(key.as(Number.class), (Number) value);
	}

	return null;
    }

    private Expression<?> parse(Root<T> root, FilterRequest request) {
	String[] parts = request.getKey().split("\\.");
	From<?, ?> join = root;

	for (int i = 0; i < parts.length - 1; i++) {
	    join = join.join(parts[i]);
	}

	return join.get(parts[parts.length - 1]);
    }

    @FunctionalInterface
    private interface ComparisonFn<T> {
	Predicate apply(Expression<T> expr, T value);
    }
}
