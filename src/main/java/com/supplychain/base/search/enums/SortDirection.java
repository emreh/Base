package com.supplychain.base.search.enums;

import com.supplychain.base.search.dto.SortRequest;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Order;
import jakarta.persistence.criteria.Root;

public enum SortDirection {

    ASC {
	public <T> Order build(Root<T> root, CriteriaBuilder cb, SortRequest request) {
	    return cb.asc(root.get(request.getKey()));
	}
    },
    DESC {
	public <T> Order build(Root<T> root, CriteriaBuilder cb, SortRequest request) {
	    return cb.desc(root.get(request.getKey()));
	}
    };

    public abstract <T> Order build(Root<T> root, CriteriaBuilder cb, SortRequest request);
}
