package ua.svinkov.facultative.util;

import org.springframework.data.domain.Sort;

import java.util.Optional;

import static ua.svinkov.facultative.util.Constants.DEFAULT_SORTING_FIELD;
import static ua.svinkov.facultative.util.Constants.DEFAULT_SORTING_ORDER;

public class SortHelper {

    private SortHelper(){}
    
    public static Sort buildSort(Optional<String> sortField, Optional<String> sortDirection) {
        return sortDirection.orElse(DEFAULT_SORTING_ORDER).equalsIgnoreCase(Sort.Direction.ASC.name()) ?
                Sort.by(sortField.orElse(DEFAULT_SORTING_FIELD)).ascending() :
                Sort.by(sortField.orElse(DEFAULT_SORTING_FIELD)).descending();

    }

}
