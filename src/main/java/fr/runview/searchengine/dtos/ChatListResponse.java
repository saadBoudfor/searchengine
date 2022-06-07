package fr.runview.searchengine.dtos;

import fr.runview.searchengine.domain.Cat;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Builder
@Data
public class ChatListResponse {
    private int totalPages;
    private int currentPage;
    private List<Cat> cats;
}
