package esg.esgdocbuilder.model.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaginationResponse<T> implements Serializable {
    private List<T> content;
    private Pagination pagination;

    @Builder
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Pagination implements Serializable {
        private Long total;
        private int limit;
        private int page;
        private int pages;

    }
}