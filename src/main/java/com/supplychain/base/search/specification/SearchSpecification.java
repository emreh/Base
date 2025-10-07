package com.supplychain.base.search.specification;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import com.supplychain.base.search.dto.FilterRequest;
import com.supplychain.base.search.dto.SearchRequest;
import com.supplychain.base.search.dto.SortRequest;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Order;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class SearchSpecification<T> implements Specification<T> {

    private static final long serialVersionUID = -9153865343320750644L;

    private final transient SearchRequest request;
    private final transient ParseRequest<T> parser;

    @Override
    public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
	List<Predicate> predicates = new ArrayList<>();

	for (FilterRequest filter : request.getFilters()) {
	    log.debug("Filter: {} {} {}", filter.getKey(), filter.getOperator(), filter.getValue());
	    Predicate p = parser.builders(filter.getOperator(), root, cb, filter, null);

	    if (p != null)
		predicates.add(p);
	}

	List<Order> orders = new ArrayList<>();

	for (SortRequest sort : request.getSorts()) {
	    orders.add(sort.getDirection().build(root, cb, sort));
	}

	query.orderBy(orders);
	return cb.and(predicates.toArray(new Predicate[0]));
    }

    public static Pageable getPageable(Integer page, Integer size) {
	int p = (page != null) ? page : 0;
	int s = (size != null) ? size : 100;
	return PageRequest.of(p, s);
    }
}
