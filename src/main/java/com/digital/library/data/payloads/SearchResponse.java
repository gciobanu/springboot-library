package com.digital.library.data.payloads;

import com.digital.library.data.model.Book;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class SearchResponse {
    List<Book> books;

    int numberOfResults;
    int numberOfPages;
}
